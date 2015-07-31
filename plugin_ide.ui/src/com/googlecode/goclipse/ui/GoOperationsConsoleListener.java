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
import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleHandler;

public class GoOperationsConsoleListener extends AbstractToolsConsoleHandler {
	
	@Override
	public void handleProcessStart(ProcessStartInfo processStartInfo) {
		new ProcessUIConsoleHandler(processStartInfo) {
			@Override
			protected void printProcessStart(IOConsoleOutputStream outStream) {
				super.printProcessStart(outStream);
			}
			
			@Override
			protected String getProcessTerminatedMessage(int exitCode) {
				return " " + super.getProcessTerminatedMessage(exitCode);
			};
			
		}.handle();
	}
	
}