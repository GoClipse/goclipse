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

import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleListener;
import melnorme.lang.ide.ui.tools.console.ToolsConsole;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.console.IOConsoleOutputStream;

import com.googlecode.goclipse.core.operations.IGoOperationsListener;
import com.googlecode.goclipse.tooling.env.GoEnvironmentConstants;

public class GoOperationsConsoleListener extends AbstractToolsConsoleListener implements IGoOperationsListener {
	
	protected static final String GO_BUILD_CONSOLE_NAME = "Go build";
	
	@Override
	protected ToolsConsole createConsole(String name) {
		return new GoBuildConsole(name);
	}
	
	public static class GoBuildConsole extends ToolsConsole {
		public GoBuildConsole(String name) {
			super(name, GoPluginImages.GO_CONSOLE_ICON.getDescriptor());
		}
	}
	
	@Override
	public void handleBuildStarted(IProject project, boolean clearConsole) {
		getOperationConsole(null, clearConsole).writeOperationInfo((project == null) ?
			"************  Building Go workspace  ************\n" :
			"------- Building Go project: " + project.getName() + "  -------\n");
	}
	
	@Override
	public void handleBuildTerminated(IProject project) {
		getOperationConsole(null, false).writeOperationInfo(
			"************  Build terminated.  ************\n");
	}
	
	@Override
	protected String getOperationConsoleName(IProject project) {
		return "Go build" + getProjectNameSuffix(project);
	}
	
	@Override
	protected String getProjectNameSuffix(IProject project) {
		if(project == null) {
			return "";
		}
		return super.getProjectNameSuffix(project);
	}
	
	@Override
	public void handleProcessStartResult(ProcessBuilder pb, IProject project,
			ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		new ProcessUIConsoleHandler(pb, project, ">> Running: ", false, processHelper, ce) {
			@Override
			protected void printProcessStartResult(IOConsoleOutputStream outStream, String prefix, ProcessBuilder pb,
					CommonException ce) {
				super.printProcessStartResult(outStream, prefix, pb, ce);
				printGoPathString(outStream, pb);
			}
		};
	}
	
	@Override
	public void engineDaemonStart(ProcessBuilder pb, CommonException ce, 
			ExternalProcessNotifyingHelper processHelper) {
		
		new EngineServerProcessUIConsoleHandler(pb, null, 
			"##########  Starting gocode server:  ##########\n" + "   ", processHelper, ce);
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, CommonException ce,
			ExternalProcessNotifyingHelper processHelper) {
		
		new EngineClientProcessUIConsoleHandler(pb, null, 
			">> Running: ", processHelper, ce) {
			@Override
			protected void printProcessStartResult(IOConsoleOutputStream outStream, String prefix,
					ProcessBuilder pb, CommonException ce) {
				super.printProcessStartResult(outStream, prefix, pb, ce);
				printGoPathString(outStream, pb);
			}
		};
		
	}
	
	protected void printGoPathString(IOConsoleOutputStream outStream, ProcessBuilder pb) {
		String gopathString = pb.environment().get(GoEnvironmentConstants.GOPATH);
		if(gopathString != null) {
			try {
				outStream.write("  with GOPATH: " + gopathString + "\n");
			} catch (IOException e) {
				// Do nothing
			}
		}
	}
	
}