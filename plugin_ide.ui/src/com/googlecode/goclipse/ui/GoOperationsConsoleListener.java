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

import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ProcessStartInfo;
import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleHandler;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;

public class GoOperationsConsoleListener extends AbstractToolsConsoleHandler {
	
	@Override
	protected String getOperationConsoleName(IProject project) {
		return BUILD_CONSOLE_NAME;
	}
	
	@Override
	public void handleProcessStart(ProcessStartInfo processStartInfo, OperationInfo opInfo) {
		ProcessUIConsoleHandler consoleHandler = new ProcessUIConsoleHandler(processStartInfo) {
			@Override
			protected void printProcessStartResult(IOConsoleOutputStream outStream) {
				super.printProcessStartResult(outStream);
				printGoPathString(outStream, processStartInfo.pb);
			}
			
			@Override
			protected String getProcessTerminatedMessage(int exitCode) {
				return " " + super.getProcessTerminatedMessage(exitCode);
			};
			
		};
		
		if(opInfo == null) {
			consoleHandler.handle();
		} else {
			consoleHandler.handle(opInfo);
		}
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, CommonException ce, 
			ExternalProcessNotifyingHelper ph) {
		ProcessStartInfo processStartInfo = new ProcessStartInfo(pb, null, ">> Running: ", false, ph, ce);
		
		new EngineClientProcessUIConsoleHandler(processStartInfo) {
			@Override
			protected void printProcessStartResult(IOConsoleOutputStream outStream) {
				super.printProcessStartResult(outStream);
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