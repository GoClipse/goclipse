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

import java.io.IOException;

import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

import org.eclipse.ui.console.IOConsoleOutputStream;

public class ConsoleOutputProcessListener implements IProcessOutputListener {
	
	protected final IOConsoleOutputStream processStdOut;
	protected final IOConsoleOutputStream processStdErr;
	
	public ConsoleOutputProcessListener(IOConsoleOutputStream processStdOut, IOConsoleOutputStream processStdErr) {
		this.processStdOut = processStdOut;
		this.processStdErr = processStdErr;
	}
	
	@Override
	public void notifyStdOutListeners(byte[] buffer, int offset, int readCount) {
		try {
			processStdOut.write(buffer, offset, readCount);
		} catch (IOException e) {
			// Ignore, it could simply mean the console page has been closed
		}
	}
	
	@Override
	public void notifyStdErrListeners(byte[] buffer, int offset, int readCount) {
		try {
			processStdErr.write(buffer, offset, readCount);
		} catch (IOException e) {
			// Ignore, it could simply mean the console page has been closed
		}
	}
	
	@Override
	public void notifyProcessTerminatedAndRead(int exitCode) {
		try {
			processStdOut.flush();
			processStdErr.flush();
		} catch (IOException e) {
			// Ignore
		}
	}
	
}