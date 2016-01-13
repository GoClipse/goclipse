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

import static melnorme.lang.ide.core.utils.TextMessageUtils.headerVeryBig;
import static melnorme.lang.ide.ui.LangUIPlugin_Actual.DAEMON_TOOL_Name;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.operations.MessageEventInfo;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ProcessStartInfo;
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
	
	public ToolsConsole getOrRecreateMessageConsole(String name, boolean recreateConsole) {
		ToolsConsole console = ConsoleUtils.findConsole(name, ToolsConsole.class);
		if(console != null) {
			if(!recreateConsole) {
				return console;
			}
			
			ConsolePlugin.getDefault().getConsoleManager().removeConsoles(array(console));
		}
		// create a new one
		console = createConsole(name);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(array(console));
		return console;
	}
	
	protected ToolsConsole getOperationConsole(IProject project, boolean clearConsole) {
		String operationConsoleName = getOperationConsoleName(project);
		// We recreate a message console to have a clear console. 
		// console.clearConsole() is not used because of poor concurrency behavior: if more than one cleanConsole
		// is requested per a console lifetime, these aditional clears may appear out of order with regards
		// to input written to the console output streams.
		// since org.eclipse.ui.console_3.5.200.v20130514-0954
		return getOrRecreateMessageConsole(operationConsoleName, clearConsole);
	}
	
	@SuppressWarnings("unused")
	protected final String getOperationConsoleName(IProject project) {
		return getGlobalConsoleName();
	}
	
	protected String getGlobalConsoleName() {
		return LangUIPlugin_Actual.TOOLS_CONSOLE_NAME;
	}
	
	protected String getProjectNameSuffix(IProject project) {
		if(project == null) {
			return " (Global)";
		}
		return " ["+ project.getName() +"]";
	}
	
	protected ToolsConsole createConsole(String name) {
		return new ToolsConsole(name, LangImages.TOOLS_CONSOLE_ICON.getDescriptor());
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void handleNewOperation(OperationInfo opInfo) {
		assertTrue(opInfo.isStarted() == false);
		
		ToolsConsole console = null;
		
		if(console == null) {
			boolean clearConsole = true;
			
			console = getOperationConsole(opInfo.getProject(), clearConsole);
			if(opInfo.explicitConsoleNotify) {
				console.activate();
			}
			
			opInfo.putProperty(TOOL_INFO__KEY_CONSOLE, console);
		}
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
	
	
	public enum ProcessStartKind {
		NORMAL,
		ENGINE_SERVER,
		ENGINE_CLIENT
	}
	
	@Override
	public void handleProcessStart(ProcessStartInfo processStartInfo) {
		handleProcessStart(ProcessStartKind.NORMAL, processStartInfo);
	}
	
	@Override
	public void engineDaemonStart(ProcessBuilder pb, ProcessStartHelper psh) {
		String prefixText = headerVeryBig("Starting " + DAEMON_TOOL_Name + " server:  ") + "   ";
		ProcessStartInfo processStartInfo = new ProcessStartInfo(null, pb, prefixText, psh);
		
		handleProcessStart(ProcessStartKind.ENGINE_SERVER, processStartInfo);
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, ProcessStartHelper psh) {
		ProcessStartInfo processStartInfo = new ProcessStartInfo(null, pb, ">> Running: ", psh);
		
		handleProcessStart(ProcessStartKind.ENGINE_CLIENT, processStartInfo);
	}
	
	public void handleProcessStart(ProcessStartKind kind, ProcessStartInfo processStartInfo) {
		switch (kind) {
		case NORMAL: {
			ToolsConsole console = getOperationConsole(processStartInfo.opInfo);
			new ProcessUIConsoleHandler(console, processStartInfo).handle();
			break;
		}
		case ENGINE_SERVER:
		case ENGINE_CLIENT: {
			
			if(ToolchainPreferences.DAEMON_CONSOLE_ENABLE.get() == false) {
				return;
			}
			
			DaemonToolMessageConsole console = DaemonToolMessageConsole.getConsole();
			if(kind == ProcessStartKind.ENGINE_CLIENT) {
				new ProcessUIConsoleHandler(console, processStartInfo).handle();
			} else {
				new ProcessUIConsoleHandler(console.infoOut, console.serverStdOut, console.serverStdErr, 
					processStartInfo).handle();
			}
			
			break;
		}
		}
	}
	
	/* -----------------  ----------------- */
	
	public class ProcessUIConsoleHandler {
		
		protected IOConsoleOutputStream infoOut;
		protected IOConsoleOutputStream processStdOut;
		protected IOConsoleOutputStream processStdErr;
		protected String infoPrefaceText;
		protected ProcessStartHelper processStartHelper;
		
		public ProcessUIConsoleHandler(ToolsConsole console, ProcessStartInfo processStartInfo) {
			this(console.infoOut, console.stdOut, console.stdErr, processStartInfo);
		}
		
		public ProcessUIConsoleHandler(IOConsoleOutputStream infoOut, 
				IOConsoleOutputStream processStdOut,
				IOConsoleOutputStream processStdErr, 
				ProcessStartInfo processStartInfo) 
		{
			List<String> commandLine = processStartInfo.pb.command();
			String argsLabel = DebugPlugin.renderArguments(ArrayUtil.createFrom(commandLine, String.class), null);
			String infoPrefaceText = processStartInfo.prefixText + argsLabel + "\n";
			
			init(infoOut, processStdOut, processStdErr, processStartInfo.processStartHelper, infoPrefaceText);
		}
		
		public void init(IOConsoleOutputStream infoOut, IOConsoleOutputStream processStdOut,
				IOConsoleOutputStream processStdErr, ProcessStartHelper processStartHelper, String infoPrefaceText) {
			this.infoOut = infoOut;
			this.processStdOut = processStdOut;
			this.processStdErr = processStdErr;
			
			this.processStartHelper = processStartHelper;
			
			this.infoPrefaceText = infoPrefaceText;
		}
		
		public void handle() {
			try {
				doWriteInfoPreface();
			} catch (IOException e) {
				// Do nothing
			}
			
			connectProcessOutputListener();
		}
		
		protected void doWriteInfoPreface() throws IOException {
			infoOut.write(infoPrefaceText);
		}
		
		protected void connectProcessOutputListener() {
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
			return new ConsoleOutputProcessListener(processStdOut, processStdErr) {
				@Override
				public void notifyProcessTerminatedAndRead(int exitCode) {
					super.notifyProcessTerminatedAndRead(exitCode);
					handleProcessTerminated(exitCode);
				}
			};
		}
		
		protected void handleProcessTerminated(int exitCode) {
			try {
				infoOut.write(getProcessTerminatedMessage(exitCode));
				infoOut.flush();
			} catch (IOException e) {
				// Ignore
			}
		}
		
		protected String getProcessTerminatedMessage(int exitCode) {
			return "  ^^^ Terminated, exit code: " + exitCode +  " ^^^\n";
		}
		
	}
	
}