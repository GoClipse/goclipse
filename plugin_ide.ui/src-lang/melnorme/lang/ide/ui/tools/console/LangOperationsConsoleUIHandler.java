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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.IOConsoleOutputStream;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask.ProcessStartHelper;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.lang.ide.ui.utils.StatusMessageDialog2;
import melnorme.lang.ide.ui.utils.StatusMessageDialogWithIgnore;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.ide.ui.utils.WorkbenchUtils;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;
import melnorme.utilbox.status.StatusException;
import melnorme.utilbox.status.StatusLevel;


public abstract class LangOperationsConsoleUIHandler implements ILangOperationsListener {
	
	public static final String MSG_IgnoreSimilar = "Ignore similar errors during this session.";
	
	public LangOperationsConsoleUIHandler() {
		super();
	}
	
	protected final Set<String> mutedMessages = Collections.synchronizedSet(new HashSet<>());
	
	@Override
	public void notifyMessage(String msgId, StatusLevel statusLevel, String title, String message) {
		SWTUtil.runInSWTThread(new Runnable() {
			@Override
			public void run() {
				if(msgId != null && mutedMessages.contains(msgId)) { 
					return;
				}
				StatusException statusMessage = new StatusException(statusLevel.toSeverity(), message);
				
				Shell shell = WorkbenchUtils.getActiveWorkbenchShell();
				StatusMessageDialog2 dialog;
				
				if(msgId == null) {
					dialog = new StatusMessageDialog2(shell, title, statusMessage);
				} else {
					dialog = new StatusMessageDialogWithIgnore(shell, title, statusMessage, MSG_IgnoreSimilar) {
						@Override
						protected void setIgnoreFutureMessages() {
							mutedMessages.add(msgId);
						};
					};
				}
				
				if(UIOperationsStatusHandler.isIgnoringHandling()) {
					Display.getCurrent().asyncExec(
						() -> dialog.okPressed()
					);
				}
				dialog.open();
			}
		});
	}
	
	/* -----------------  ----------------- */
	
	@SuppressWarnings("unused")
	protected String getBuildConsoleName(IProject project) {
		return LangUIPlugin_Actual.BUILD_ConsoleName;
	}
	
	protected ToolsConsole createBuildConsole(String name) {
		return new ToolsConsole(name, LangImages.BUILD_CONSOLE_ICON.getDescriptor());
	}
	
	protected ToolsConsole getBuildConsole(IProject project, boolean clearConsole) {
		String operationConsoleName = getBuildConsoleName(project);
		return ConsoleUtils.getOrCreateToolsConsole(operationConsoleName, clearConsole, ToolsConsole.class, 
			() -> createBuildConsole(operationConsoleName));
	}
	
	
	/* -----------------  ----------------- */
	
	@Override
	public IToolOperationMonitor beginOperation(ProcessStartKind kind, boolean clearConsole, 
			boolean activateConsole) {
		
		IToolOperationMonitor opHandler = doBeginOperation(kind, clearConsole);
		
		if(activateConsole){
			opHandler.activate();
		}
		
		return opHandler;
	}
	
	protected IToolOperationMonitor doBeginOperation(ProcessStartKind kind, boolean clearConsole) {
		switch (kind) {
		case BUILD: {
			ToolsConsole console = getBuildConsole(null, clearConsole);
			return createConsoleHandler(kind, console, console.stdOut, console.stdErr);
		}
		case CHECK_BUILD: {
			ToolsConsole console = getBuildConsole(null, clearConsole);
			return createConsoleHandler(kind, console, console.stdOut, console.stdErr_silent);
		}
		case ENGINE_SERVER: {
			EngineToolsConsole console = EngineToolsConsole.getConsole(clearConsole);
			return createConsoleHandler(kind, console, console.serverStdOut, console.serverStdErr);
		}
		case ENGINE_TOOLS: {
			EngineToolsConsole console = EngineToolsConsole.getConsole(clearConsole);
			return createConsoleHandler(kind, console, console.stdOut, console.stdErr);
		}
		}
		throw assertFail();
	}
	
	protected OperationConsoleMonitor createConsoleHandler(ProcessStartKind kind, ToolsConsole console, 
			IOConsoleOutputStream stdOut, IOConsoleOutputStream stdErr) {
		return new OperationConsoleMonitor(kind, console, stdOut, stdErr);
	}
	
	/* -----------------  ----------------- */
	
	public class OperationConsoleMonitor implements IToolOperationMonitor {
		
		protected final ProcessStartKind kind;
		protected final ToolsConsole console;
		protected final IOConsoleOutputStream infoOut;
		protected final IOConsoleOutputStream stdOut;
		protected final IOConsoleOutputStream stdErr;
		
		public boolean errorOnNonZeroExitValueForBuild = false;
		
		public OperationConsoleMonitor(ProcessStartKind kind, ToolsConsole console, 
				IOConsoleOutputStream stdOut, IOConsoleOutputStream stdErr) {
			this.kind = assertNotNull(kind);
			this.console = assertNotNull(console);
			this.infoOut = console.infoOut;
			this.stdOut = stdOut;
			this.stdErr = stdErr;
		}
		
		@Override
		public void writeInfoMessage(String operationMessage) {
			console.writeOperationInfo(operationMessage);
		}
		
		@Override
		public void handleProcessStart(String prefixText, String suffixText, ProcessBuilder pb, 
			ProcessStartHelper processStartHelper) 
		{
			String infoPrefaceText = getPrefaceText(prefixText, suffixText, pb);
			
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
		
		@Override
		public void activate() {
			console.activate();
			// Try again - because of poort concurrency guarantes, the console view might have been activated
			// but with the wrong console page, so send another request, in UI thread.
			Display.getDefault().asyncExec(() -> console.activate());
		}
		
	}
	
	protected String getPrefaceText(String prefixText, String suffixText, ProcessBuilder pb) {
		List<String> commandLine = pb.command();
		
		prefixText = prefixText == null ? ">> Running: " : prefixText;
		
		String argsLabel = DebugPlugin.renderArguments(ArrayUtil.createFrom(commandLine, String.class), null);
		String infoPrefaceText = prefixText + argsLabel;
		return infoPrefaceText + StringUtil.nullAsEmpty(suffixText) + "\n";
	}
	
	protected String getProcessTerminatedMessage(int exitCode) {
		return "  ^^^ Terminated, exit code: " + exitCode +  " ^^^\n";
	}
	
}