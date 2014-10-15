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

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.IOException;
import java.util.List;

import melnorme.lang.ide.core.operations.ILangOperationsListener;
import melnorme.lang.ide.ui.utils.ConsoleUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsoleOutputStream;


public abstract class AbstractToolsConsoleListener implements ILangOperationsListener {
	
	public ToolsConsole recreateMessageConsole(String name, boolean recreateConsole) {
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
	
	public AbstractToolsConsoleListener() {
		super();
	}
	
	protected ToolsConsole getOperationConsole(IProject project, boolean clearConsole) {
		// We recreate a message console to have a clear console. 
		// console.clearConsole() is not used because of poor concurrency behavior: if more than one cleanConsole
		// is requested per a console lifetime, these aditional clears may appear out of order with regards
		// to input written to the console output streams.
		// since org.eclipse.ui.console_3.5.200.v20130514-0954
		boolean recreateConsole = clearConsole;
		return recreateMessageConsole(getOperationConsoleName(project), recreateConsole);
	}
	
	protected abstract String getOperationConsoleName(IProject project);
	
	protected abstract ToolsConsole createConsole(String name);
	
	protected String getProjectNameSuffix(IProject project) {
		if(project == null) {
			return "(Global)";
		}
		return "["+ project.getName() +"]";
	}
	
	protected void printProcessStartResult(IOConsoleOutputStream outStream, String prefix, ProcessBuilder pb,
			CommonException ce) {
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
	
	@Override
	public void handleProcessStartResult(ProcessBuilder pb, IProject project,
			ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		final ToolsConsole console = getOperationConsole(project, true);
		
		printProcessStartResult(console.infoOut, ">> Running: ", pb, ce);
		
		if(processHelper != null) {
			processHelper.getOutputListenersHelper().addListener(new ProcessOutputToConsoleListener(console));
		}
	}
	
}