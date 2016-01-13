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
package com.googlecode.goclipse.ui;

import org.eclipse.ui.console.IOConsoleOutputStream;

import melnorme.lang.ide.core.operations.ProcessStartInfo;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.tools.console.LangOperationsConsoleUIHandler;
import melnorme.lang.ide.ui.tools.console.ToolsConsole;
import melnorme.lang.ide.ui.tools.console.ToolsConsolePrefs;

public class GoOperationsConsoleUIHandler extends LangOperationsConsoleUIHandler {
	
	@Override
	protected ToolsConsole createConsole(String name) {
		return new ToolsConsole(name, LangImages.TOOLS_CONSOLE_ICON.getDescriptor()) {
			
			@Override
			protected void ui_bindActivateOnErrorsListeners() {
				// dont activate on stderr output, because Go build often uses "-v" flag
				stdErr.setActivateOnWrite(false);
			}
			
		};
	}
	/* FIXME: */
	
//	@Override
//	public void handleProcessStart(ProcessStartInfo processStartInfo) {
//		new ProcessUIConsoleHandler(processStartInfo) {
//			@Override
//			protected void printProcessStart(IOConsoleOutputStream outStream) {
//				super.printProcessStart(outStream);
//			}
//			
//			@Override
//			protected void handleProcessTerminated(ToolsConsole console, int exitCode) {
//				Boolean activateOnErrors = ToolsConsolePrefs.ACTIVATE_ON_ERROR_MESSAGES.get();
//				if(exitCode != 0 && activateOnErrors) {
//					console.activate();
//				}
//				super.handleProcessTerminated(console, exitCode);
//			};
//			
//			@Override
//			protected String getProcessTerminatedMessage(int exitCode) {
//				return " " + super.getProcessTerminatedMessage(exitCode);
//			};
//			
//		}.handle();
//	}
	
	@Override
	public void handleProcessStart(ProcessStartKind kind, ProcessStartInfo processStartInfo) {
		super.handleProcessStart(kind, processStartInfo);
	}
	
}