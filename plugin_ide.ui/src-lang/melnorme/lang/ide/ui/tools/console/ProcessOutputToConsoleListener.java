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

import java.io.IOException;

import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

public class ProcessOutputToConsoleListener implements IProcessOutputListener {
	
	private final ToolsConsole console;
	
	public ProcessOutputToConsoleListener(ToolsConsole console) {
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
			console.infoOut.write("--------  Terminated, exit code: " + exitCode +  "  --------\n");
			console.infoOut.flush();
		} catch (IOException e) {
			// Ignore
		}
	}
	
}