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

import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.tools.console.LangOperationsConsoleUIHandler;
import melnorme.lang.ide.ui.tools.console.ToolsConsole;
import melnorme.lang.ide.ui.tools.console.ToolsConsole.IOConsoleOutputStreamExt;

public class GoOperationsConsoleUIHandler extends LangOperationsConsoleUIHandler {
	
	@Override
	protected ToolsConsole createBuildConsole(String name) {
		return new ToolsConsole(name, LangImages.BUILD_CONSOLE_ICON.getDescriptor()) {
			
			@Override
			protected void ui_bindActivateOnErrorsListeners() {
				// dont activate on stderr output, because Go build often uses "-v" flag
				stdErr.console().setActivateOnWrite(false);
			}
			
		};
	}
	
	@Override
	protected OperationConsoleMonitor createConsoleHandler(ProcessStartKind kind, ToolsConsole console,
			IOConsoleOutputStreamExt stdOut, IOConsoleOutputStreamExt stdErr) {
		OperationConsoleMonitor monitor = super.createConsoleHandler(kind, console, stdOut, stdErr);
		monitor.errorOnNonZeroExitValueForBuild = true;
		return monitor;
	}
	
	@Override
	protected String getProcessTerminatedMessage(int exitCode) {
		return " " + super.getProcessTerminatedMessage(exitCode);
	};
	
}