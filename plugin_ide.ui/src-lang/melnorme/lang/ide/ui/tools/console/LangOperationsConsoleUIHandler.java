/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.tools.console;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.operations.MessageEventInfo;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask.ProcessStartHelper;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;


public abstract class LangOperationsConsoleUIHandler implements ILangOperationsListener {
	
	public LangOperationsConsoleUIHandler() {
		super();
	}
	
	@Override
	public void notifyMessage(final StatusLevel statusLevel, final String title, final String message) {
		SWTUtil.runInSWTThread(new Runnable() {
			@Override
			public void run() {
				UIOperationsStatusHandler.displayStatusMessage(title, statusLevel, message);
			}
		});
	}
	
	/* -----------------  ----------------- */
	
	@SuppressWarnings("unused")
	protected final String getBuildConsoleName(IProject project) {
		return LangUIPlugin_Actual.BUILD_ConsoleName;
	}
	
	protected ToolsConsole createBuildConsole(String name) {
		return new ToolsConsole(name, LangImages.BUILD_CONSOLE_ICON.getDescriptor());
	}
	
	protected ToolsConsole getBuildConsole(IProject project, boolean clearConsole) {
		String operationConsoleName = getBuildConsoleName(project);
		return ConsoleUtils.getOrCreateToolsConsole2(operationConsoleName, clearConsole, ToolsConsole.class, 
			() -> createBuildConsole(operationConsoleName));
	}
	
	
	/* -----------------  ----------------- */
	
	@Override
	public void handleStartBuildOperation(OperationInfo opInfo) {
		assertTrue(opInfo.isStarted() == false);
		
		boolean clearConsole = true;
		IProject project = opInfo.getProject();
		
		ToolsConsole console = getBuildConsole(project, clearConsole);
		
		if(opInfo.explicitConsoleNotify) {
			console.activate();
		}
		
		opInfo.putProperty(TOOL_INFO__KEY_CONSOLE, console);
	}
	
	public static final String TOOL_INFO__KEY_CONSOLE = "CONSOLE";
	
	protected ToolsConsole getOperationConsole(OperationInfo opInfo) {
		assertNotNull(opInfo);
		return opInfo.getProperty(TOOL_INFO__KEY_CONSOLE, ToolsConsole.class);
	}
	
	@Override
	public void handleMessage(MessageEventInfo messageEvent) {
		ToolsConsole console = getOperationConsole(messageEvent.opInfo);
		console.writeOperationInfo(messageEvent.operationMessage);
	}
	
	@Override
	public ILangOperationConsoleHandler getOperationUIHandler(ProcessStartKind kind, OperationInfo opInfo) {
		switch (kind) {
		case BUILD: {
			assertNotNull(opInfo); /* FIXME: review need for this contract */
			ToolsConsole console = getOperationConsole(opInfo);
			assertNotNull(console);
			return createConsoleHandler(kind, console, console.stdOut, console.stdErr);
		}
		case ENGINE_SERVER: {
			if(ToolchainPreferences.DAEMON_CONSOLE_ENABLE.get() == false) {
				// return no-op handler.
				return new ILangOperationConsoleHandler() {
					@Override
					public void handleProcessStart(String prefixText, ProcessBuilder pb,
							ProcessStartHelper processStartHelper) {
					}
				};
			}
			
			EngineToolsConsole console = EngineToolsConsole.getConsole();
			return createConsoleHandler(kind, console, console.serverStdOut, console.serverStdErr);
		}
		case ENGINE_TOOLS: {
			EngineToolsConsole console = EngineToolsConsole.getConsole();
			return createConsoleHandler(kind, console, console.stdOut, console.stdErr);
		}
		}
		throw assertFail();
	}
	
	protected LangOperationConsoleHandler createConsoleHandler(ProcessStartKind kind, ToolsConsole console, 
			IOConsoleOutputStream stdOut, IOConsoleOutputStream stdErr) {
		return new LangOperationConsoleHandler(kind, console, stdOut, stdErr);
	}
	
	/* -----------------  ----------------- */
	
	public class LangOperationConsoleHandler implements ILangOperationConsoleHandler {
		
		protected final ProcessStartKind kind;
		protected final ToolsConsole console;
		protected final IOConsoleOutputStream infoOut;
		protected final IOConsoleOutputStream stdOut;
		protected final IOConsoleOutputStream stdErr;
		
		public boolean errorOnNonZeroExitValueForBuild = false;
		
		public LangOperationConsoleHandler(ProcessStartKind kind, ToolsConsole console, 
				IOConsoleOutputStream stdOut, IOConsoleOutputStream stdErr) {
			this.kind = kind;
			this.console = console;
			this.infoOut = console.infoOut;
			this.stdOut = stdOut;
			this.stdErr = stdErr;
		}
		
		@Override
		public void handleProcessStart(String prefixText, ProcessBuilder pb, ProcessStartHelper processStartHelper) {
			String infoPrefaceText = getPrefaceText(prefixText, pb);
			
			try {
				if(infoPrefaceText != null) {
					infoOut.write(infoPrefaceText);
				}
			} catch (IOException e) {
				// Do nothing
			}
			
			connectProcessOutputListener(processStartHelper);
		}
		
		protected void connectProcessOutputListener(ProcessStartHelper processStartHelper) {
			try {
				processStartHelper.addProcessListener(createOutputListener());
			} catch(CommonException ce) {
				
				String text = "  FAILED: " + ce.getMessage();
				Throwable cause = ce.getCause();
				if(cause != null) {
					text += "   Reason: " + cause.getMessage() + "\n";
				}
				try {
					infoOut.write(text);
				} catch (IOException e) {
					// Do nothing
				}
				
			}
		}
		
		protected IProcessOutputListener createOutputListener() {
			return new ConsoleOutputProcessListener(stdOut, stdErr) {
				@Override
				public void notifyProcessTerminatedAndRead(int exitCode) {
					super.notifyProcessTerminatedAndRead(exitCode);
					handleProcessTerminated(exitCode);
				}
			};
		}
		
		protected void handleProcessTerminated(int exitCode) {
			boolean activateOnErrors = kind == ProcessStartKind.BUILD && 
					ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.get();
			
			if(errorOnNonZeroExitValueForBuild && exitCode != 0 && activateOnErrors) {
				console.activate();
			}
			
			try {
				infoOut.write(getProcessTerminatedMessage(exitCode));
				infoOut.flush();
			} catch (IOException e) {
				// Ignore
			}
		}
		
	}
	
	protected String getPrefaceText(String prefixText, ProcessBuilder pb) {
		List<String> commandLine = pb.command();
		
		prefixText = prefixText == null ? ">> Running: " : prefixText;
		
		String argsLabel = DebugPlugin.renderArguments(ArrayUtil.createFrom(commandLine, String.class), null);
		String infoPrefaceText = prefixText + argsLabel + "\n";
		return infoPrefaceText;
	}
	
	protected String getProcessTerminatedMessage(int exitCode) {
		return "  ^^^ Terminated, exit code: " + exitCode +  " ^^^\n";
	}
	
}