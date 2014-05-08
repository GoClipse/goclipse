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
package com.googlecode.goclipse.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;

import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.util.swt.jface.ColorManager;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.source.ISharedTextColors;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;

import com.googlecode.goclipse.builder.GoBuildManager.GoBuildListener;

public class GoBuilderConsoleListener implements GoBuildListener {
	
	public static GoBuildConsole recreateMessageConsole(String name, boolean recreateConsole) {
		GoBuildConsole console = ConsoleUtils.findConsole(name, GoBuildConsole.class);
		if(console != null) {
			if(!recreateConsole) {
				return console;
			}
			
			ConsolePlugin.getDefault().getConsoleManager().removeConsoles(array(console));
		}
		// create a new one
		console = new GoBuildConsole(name);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(array(console));
		return console;
	}
	
	protected GoBuildConsole getBuildConsole(IProject project, boolean clearConsole) {
		String consoleQualifier = getConsoleQualifier(project); 
		// We recreate a message console to have a clear console. 
		// console.clearConsole() is not used because of poor concurrency behavior: if more than one cleanConsole
		// is requested per a console lifetime, these aditional clears may appear out of order with regards
		// to input written to the console output streams.
		// since org.eclipse.ui.console_3.5.200.v20130514-0954
		boolean recreateConsole = clearConsole;
		return recreateMessageConsole("Go build " + consoleQualifier, recreateConsole);
	}
	
	protected String getConsoleQualifier(IProject project) {
		if(project == null) {
			return "(Global)";
		}
		return "["+ project.getName() +"]";
	}
	
	public static class GoBuildConsole extends MessageConsole {
		
		protected final IOConsoleOutputStream metaOut;
		protected final IOConsoleOutputStream stdOut;
		protected final IOConsoleOutputStream stdErr;
		
		public GoBuildConsole(String name) {
			super(name, GoPluginImages.GO_CONSOLE_ICON.getDescriptor());
			
			metaOut = newOutputStream();
			
			stdOut = newOutputStream();
			stdErr = newOutputStream();
			stdErr.setActivateOnWrite(true);
			
			// BM: it's not clear to me if a Color can be created outside UI thread, so do asyncExec
			// I would think one cant, but some Platform code (ProcessConsole) does freely create Color instances
			// on the UI thread, so maybe the asyncExec is not necessary.
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					metaOut.setColor(getColorManager().getColor(new RGB(0, 0, 180)));
					stdErr.setColor(getColorManager().getColor(new RGB(200, 0, 0)));
				}
			});
		}
		
		protected ISharedTextColors getColorManager() {
			return ColorManager.getDefault();
		}
		
	}
	
	@Override
	public void handleBuildStarted(IProject project) {
		GoBuildConsole console = getBuildConsole(project, true);
		
		try {
			String projName = project.getName();
			console.metaOut.write("************  Running Go build for project: " + projName + "  ************\n");
		} catch (IOException e) {
			return;
		}
	}
	
	@Override
	public void handleBuildTerminated(IProject project) {
		GoBuildConsole console = getBuildConsole(project, false);
		
		try {
			console.metaOut.write("************  Build terminated.  ************\n");
		} catch (IOException e) {
			return;
		}
	}
	
	@Override
	public void handleProcessStarted(ProcessBuilder pb, IProject project, 
			ExternalProcessNotifyingHelper processHelper) {
		GoBuildConsole console = getBuildConsole(project, false);
		
		try {
			writeProcessStartPrefix(pb, console);
			
			processHelper.getOutputListeningHelper().addListener(new ProcessOutputToConsoleListener(console));
		} catch (IOException e) {
			return;
		}
	}
	
	
	protected void writeProcessStartPrefix(ProcessBuilder pb, GoBuildConsole console) throws IOException {
		console.metaOut.write(StringUtil.collToString(pb.command(), " ") + "\n");
	}
	
	@Override
	public void handleProcessStartFailure(ProcessBuilder pb, IProject project, IOException processStartException) {
		GoBuildConsole console = getBuildConsole(project, false);
		
		try {
			writeProcessStartPrefix(pb, console);
			console.metaOut.write(">>>  Failed to start process: \n");
			console.metaOut.write(processStartException.getMessage());
		} catch (IOException consoleIOE) {
			return;
		}
	}
	
	public static class ProcessOutputToConsoleListener implements IProcessOutputListener {
		
		private final GoBuildConsole console;
		
		public ProcessOutputToConsoleListener(GoBuildConsole console) {
			this.console = console;
		}
		
		@Override
		public void notifyStdOutListeners(byte[] buffer, int offset, int readCount) {
			try {
				console.stdOut.write(buffer, offset, readCount);
			} catch (IOException e) {
				// Ignore, it could simply mean the console page has been closed
			}
		}
		
		@Override
		public void notifyStdErrListeners(byte[] buffer, int offset, int readCount) {
			try {
				console.stdErr.write(buffer, offset, readCount);
			} catch (IOException e) {
				// Ignore, it could simply mean the console page has been closed
			}		
		}
		
		@Override
		public void notifyProcessTerminatedAndRead(int exitCode) {
			try {
				console.stdOut.flush();
				console.stdErr.flush();
				console.metaOut.write("--------  Terminated, exit code: " + exitCode +  "  --------\n");
				console.metaOut.flush();
			} catch (IOException e) {
				// Ignore
			}
		}
		
	}
	
}