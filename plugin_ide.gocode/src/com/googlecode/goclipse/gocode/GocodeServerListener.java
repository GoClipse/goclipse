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
package com.googlecode.goclipse.gocode;

import java.io.IOException;

import org.eclipse.core.runtime.Platform;

import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;

public class GocodeServerListener implements IProcessOutputListener {
	
	protected GocodeMessageConsole getConsole() {
		return GocodeMessageConsole.getConsole();
	}
	
	@Override
	public void notifyStdOutListeners(byte[] buffer, int offset, int readCount) {
		if(!Platform.inDebugMode()) {
			return;
		}
				
		try {
			getConsole().stdOut.write(buffer, offset, readCount);
		} catch (IOException e) {
			// Ignore, it could simply mean the console page has been closed
		}
	}
	
	@Override
	public void notifyStdErrListeners(byte[] buffer, int offset, int readCount) {
		// XXX: this implementation is buggy if the chunk ends in the middle of a multi-byte unicode character
		String string = new String(buffer, offset, readCount, StringUtil.UTF8);
		GocodePlugin.logWarning("gocode error:\n" + string);
		
		if(!Platform.inDebugMode()) {
			return;
		}
		
		try {
			getConsole().stdErr.write(buffer, offset, readCount);
		} catch (IOException e) {
			// Ignore, it could simply mean the console page has been closed
		}
	}
	
	@Override
	public void notifyProcessTerminatedAndRead(int exitCode) {
//		try {
//			getConsole().stdOut.flush();
//			getConsole().stdErr.flush();
//		} catch (IOException e) {
//			GoCore.logError(e);
//		}
	}
	
}