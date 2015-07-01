/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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
import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;
import java.util.List;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ProcessStartInfo;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;


public abstract class AbstractToolsConsoleHandler implements ILangOperationsListener {
	
	protected static final String BUILD_CONSOLE_NAME = LangCore_Actual.LANGUAGE_NAME + " build";
	
	public AbstractToolsConsoleHandler() {
		super();
	}
	
	@Override
	public void notifyMessage(final StatusLevel statusLevel, final String title, final String message) {
		SWTUtil.runInSWTThread(new Runnable() {
			@Override
			public void run() {
				UIOperationExceptionHandler.handleStatusMessage(statusLevel, title, message);
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
	
	protected final String getOperationConsoleName(IProject project) {
		return useGlobalConsole() ? getGlobalConsoleName() : getProjectConsoleName(project);
	}
	
	protected boolean useGlobalConsole() {
		return true;
	}
	
	protected String getGlobalConsoleName() {
		return BUILD_CONSOLE_NAME;
	}
	
	protected String getProjectConsoleName(IProject project) {
		return getGlobalConsoleName() + getProjectNameSuffix(project);
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
	public void handleToolOperationStart(OperationInfo opInfo) {
		IProject project = opInfo.getProject();
		
		if(project == null && !useGlobalConsole()) {
			return; // Ignore this message, valid only for global console use
		}
		
		boolean clearConsole = useGlobalConsole() ? 
				project == null :
				opInfo.clearConsole;
		
		final ToolsConsole console = getOperationConsole(project, clearConsole);
		opInfo.putProperty(TOOL_INFO__KEY_CONSOLE, console);
		console.writeOperationInfo(opInfo.operationMessage);
	}
	
	public static final String TOOL_INFO__KEY_CONSOLE = "CONSOLE";
	
	@Override
	public void handleProcessStart(ProcessStartInfo processStartInfo, OperationInfo opInfo) {
		new ProcessUIConsoleHandler(processStartInfo, opInfo).handle();
	}
	
	@Override
	public void engineDaemonStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper ph) {
		String prefixText = headerVeryBig("Starting " + DAEMON_TOOL_Name + " server:  ") + "   ";
		ProcessStartInfo processStartInfo = new ProcessStartInfo(pb, null, prefixText, true, ph, ce);
		
		new EngineServerProcessUIConsoleHandler(processStartInfo).handle();
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, CommonException ce, ExternalProcessNotifyingHelper ph) {
		ProcessStartInfo processStartInfo = new ProcessStartInfo(pb, null, ">> Running: ", false, ph, ce);
		new EngineClientProcessUIConsoleHandler(processStartInfo).handle();
	}
	
	/* -----------------  ----------------- */
	
	protected static void default_printProcessStartResult(IOConsoleOutputStream outStream, String prefix, 
			ProcessBuilder pb, CommonException ce) {
		List<String> commandLine = pb.command();
		String text = prefix + StringUtil.collToString(commandLine, " ") + "\n";
		
		if(ce != null) {
			text += "  FAILED: " + ce.getMessage();
			Throwable cause = ce.getCause();
			if(cause != null) {
				text += "   Reason: " + cause.getMessage() + "\n";
			}
		}
		
		try {
			outStream.write(text);
		} catch (IOException e) {
			// Do nothing
		}
	}
	
	public class ProcessUIConsoleHandler {
		
		protected final ProcessStartInfo info;
		protected final OperationInfo opInfo;
		
		protected ToolsConsole console;
		
		public ProcessUIConsoleHandler(ProcessStartInfo processStartInfo) {
			this(processStartInfo, null);
		}
		
		public ProcessUIConsoleHandler(ProcessStartInfo processStartInfo, OperationInfo opInfo) {
			this.info = assertNotNull(processStartInfo);
			this.opInfo = opInfo;
			
			if(opInfo != null) {
				Object consoleObj = opInfo.getProperty(TOOL_INFO__KEY_CONSOLE);
				if(consoleObj instanceof ToolsConsole) {
					console = (ToolsConsole) consoleObj;
				}
			}
		}
		
		public void handle() {
			if(console == null) {
				console = getConsole();
			}
			handle(console);
		}
		
		protected ToolsConsole getConsole() {
			return getOperationConsole(info.project, info.clearConsole);
		}
		
		public final void handle(ToolsConsole console) {
			printProcessStartResult(console.infoOut);
			
			if(info.processHelper != null) {
				info.processHelper.getOutputListenersHelper().addListener(createOutputListener(console));
			}
		}
		
		protected void printProcessStartResult(IOConsoleOutputStream outStream) {
			default_printProcessStartResult(outStream, info.prefixText, info.pb, info.ce);
		}
		
		protected IProcessOutputListener createOutputListener(final ToolsConsole console) {
			return new ConsoleOutputProcessListener(console.stdOut, console.stdErr) {
				@Override
				public void notifyProcessTerminatedAndRead(int exitCode) {
					super.notifyProcessTerminatedAndRead(exitCode);
					handleProcessTerminated(console, exitCode);
				}
			};
		}
		
		protected void handleProcessTerminated(ToolsConsole console, int exitCode) {
			try {
				console.infoOut.write(getProcessTerminatedMessage(exitCode));
				console.infoOut.flush();
			} catch (IOException e) {
				// Ignore
			}
		}
		
		protected String getProcessTerminatedMessage(int exitCode) {
			return "  ^^^ Terminated, exit code: " + exitCode +  " ^^^\n";
		}
		
	}
	
	public class EngineServerProcessUIConsoleHandler extends ProcessUIConsoleHandler {
		
		protected final DaemonToolMessageConsole console;
		
		public EngineServerProcessUIConsoleHandler(ProcessStartInfo info) {
			super(info);
			
			console = DaemonToolMessageConsole.getConsole();
		}
		
		@Override
		public void handle() {
			if(ToolchainPreferences.DAEMON_CONSOLE_ENABLE.get() == false) {
				return;
			}
			handle(console);
		}
		
		@Override
		protected ConsoleOutputProcessListener createOutputListener(ToolsConsole console_) {
			return new ConsoleOutputProcessListener(console.serverStdOut, console.serverStdErr);
		}
	
	}
	
	public class EngineClientProcessUIConsoleHandler extends EngineServerProcessUIConsoleHandler {
		
		public EngineClientProcessUIConsoleHandler(ProcessStartInfo info) {
			super(info);
		}
		
		@Override
		protected ConsoleOutputProcessListener createOutputListener(ToolsConsole console_) {
			return new ConsoleOutputProcessListener(console.stdOut, console.stdErr);
		}
	}
	
}