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
import melnorme.lang.ide.ui.LangOperationConsole_Actual;
import melnorme.lang.ide.ui.utils.ConsoleUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.ConsolePlugin;


public abstract class AbstractToolsConsoleListener {
	
	public static LangOperationConsole_Actual recreateMessageConsole(String name, boolean recreateConsole) {
		LangOperationConsole_Actual console = ConsoleUtils.findConsole(name, LangOperationConsole_Actual.class);
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
	
	protected static LangOperationConsole_Actual createConsole(String name) {
		return new LangOperationConsole_Actual(name);
	}
	
}