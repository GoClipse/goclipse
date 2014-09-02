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
package melnorme.lang.ide.ui.build;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;

import melnorme.lang.ide.ui.LangOperationConsole_Actual;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.ConsolePlugin;


public abstract class LangOperationConsoleListener {
	
	public static LangOperationConsole_Actual recreateMessageConsole(String name, boolean recreateConsole) {
		LangOperationConsole_Actual console = ConsoleUtils.findConsole(name, LangOperationConsole_Actual.class);
		if(console != null) {
			if(!recreateConsole) {
				return console;
			}
			
			ConsolePlugin.getDefault().getConsoleManager().removeConsoles(array(console));
		}
		// create a new one
		console = new LangOperationConsole_Actual(name);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(array(console));
		return console;
	}
	
	public LangOperationConsoleListener() {
		super();
	}
	
	protected LangOperationConsole_Actual getOperationConsole(IProject project, boolean clearConsole) {
		// We recreate a message console to have a clear console. 
		// console.clearConsole() is not used because of poor concurrency behavior: if more than one cleanConsole
		// is requested per a console lifetime, these aditional clears may appear out of order with regards
		// to input written to the console output streams.
		// since org.eclipse.ui.console_3.5.200.v20130514-0954
		boolean recreateConsole = clearConsole;
		return recreateMessageConsole(getOperationConsoleName(project), recreateConsole);
	}
	
	protected abstract String getOperationConsoleName(IProject project);
	
	protected String getProjectNameSuffix(IProject project) {
		if(project == null) {
			return "(Global)";
		}
		return "["+ project.getName() +"]";
	}
	
	
	public static class ProcessOutputToConsoleListener implements IProcessOutputListener {
		
		private final LangOperationConsole console;
		
		public ProcessOutputToConsoleListener(LangOperationConsole console) {
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