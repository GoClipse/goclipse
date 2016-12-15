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

import melnorme.lang.ide.ui.tools.console.ToolsConsole.IOConsoleOutputStreamExt;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

public class ConsoleOutputProcessListener implements IProcessOutputListener {
	
	protected final IOConsoleOutputStreamExt processStdOut;
	protected final IOConsoleOutputStreamExt processStdErr;
	
	public ConsoleOutputProcessListener(IOConsoleOutputStreamExt processStdOut, IOConsoleOutputStreamExt processStdErr) {
		this.processStdOut = processStdOut;
		this.processStdErr = processStdErr;
	}
	
	@Override
	public void notifyStdOutListeners(byte[] buffer, int offset, int readCount) {
		processStdOut.write(buffer, offset, readCount);
	}
	
	@Override
	public void notifyStdErrListeners(byte[] buffer, int offset, int readCount) {
		processStdErr.write(buffer, offset, readCount);
	}
	
	@Override
	public void notifyProcessTerminatedAndRead(int exitCode) {
		processStdOut.flush();
		processStdErr.flush();
	}
	
}