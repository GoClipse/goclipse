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
package com.googlecode.goclipse.ui;

import java.io.IOException;

import org.eclipse.ui.console.IOConsoleOutputStream;

import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;

import melnorme.lang.ide.core.operations.ProcessStartInfo;
import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleHandler;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

public class GoOperationsConsoleListener extends AbstractToolsConsoleHandler {
	
	@Override
	protected boolean useGlobalConsole() {
		return true;
	}
	
	@Override
	public void handleProcessStart(ProcessStartInfo processStartInfo) {
		new ProcessUIConsoleHandler(processStartInfo) {
			@Override
			protected void printProcessStart(IOConsoleOutputStream outStream) {
				super.printProcessStart(outStream);
				printGoPathString(outStream, processStartInfo.pb);
			}
			
			@Override
			protected String getProcessTerminatedMessage(int exitCode) {
				return " " + super.getProcessTerminatedMessage(exitCode);
			};
			
		}.handle();
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, CommonException ce, 
			ExternalProcessNotifyingHelper ph) {
		ProcessStartInfo processStartInfo = new ProcessStartInfo(null, pb, ">> Running: ", ph, ce);
		
		new EngineClientProcessUIConsoleHandler(processStartInfo) {
			@Override
			protected void printProcessStart(IOConsoleOutputStream outStream) {
				super.printProcessStart(outStream);
				printGoPathString(outStream, pb);
			}
		}.handle();
		
	}
	
	protected void printGoPathString(IOConsoleOutputStream outStream, ProcessBuilder pb) {
		String gopathString = pb.environment().get(GoEnvironmentConstants.GOPATH);
		if(gopathString != null) {
			try {
				outStream.write("   with GOPATH: " + gopathString + "\n");
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	
}