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

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleListener;
import melnorme.lang.ide.ui.tools.console.ConsoleOuputProcessListener;
import melnorme.lang.ide.ui.tools.console.DaemonToolMessageConsole;
import melnorme.lang.ide.ui.tools.console.ProcessOutputToConsoleListener;
import melnorme.lang.ide.ui.tools.console.ToolsConsole;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.operations.IGoOperationsListener;

public class GoOperationsConsoleListener extends AbstractToolsConsoleListener implements IGoOperationsListener {
	
	@Override
	protected String getOperationConsoleName(IProject project) {
		return "Go build " + getProjectNameSuffix(project);
	}
	
	public static class GoBuildConsole extends ToolsConsole {
		
		public GoBuildConsole(String name) {
			super(name, GoPluginImages.GO_CONSOLE_ICON.getDescriptor());
		}
		
	}
	
	@Override
	public void handleBuildStarted(IProject project) {
		String projName = project.getName();
		getOperationConsole(project, true).writeOperationInfo(
			"************  Running Go build for project: " + projName + "  ************\n");
	}
	
	@Override
	public void handleBuildTerminated(IProject project) {
		getOperationConsole(project, false).writeOperationInfo(
			"************  Build terminated.  ************\n");
	}
	
	/* FIXME: refactor IGoBuildListener usage. */
	
	@Override
	public void handleProcessStartResult(ProcessBuilder pb, IProject project,
			ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		
		GoBuildConsole console = getOperationConsole(project, false);
		
		printProcessStartResult(console.infoOut, ">> Running: ", pb, ce);
		
		if(processHelper != null) {
			processHelper.getOutputListenersHelper().addListener(new ProcessOutputToConsoleListener(console));
		}
	}
	
	@Override
	public void engineDaemonStart(ProcessBuilder pb, CommonException ce, 
			ExternalProcessNotifyingHelper processHelper) {
		
		if(DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.get() == false) {
			return;
		}
		
		DaemonToolMessageConsole console = DaemonToolMessageConsole.getConsole();
		
		printProcessStartResult(console.infoOut, "##########  Starting gocode server:  ##########\n" + "   ", pb, ce);
		
		if(processHelper != null) {
			processHelper.getOutputListenersHelper().addListener(
				new ConsoleOuputProcessListener(console.serverStdOut, console.serverStdErr));
		}
	}
	
	@Override
	public void engineClientToolStart(ProcessBuilder pb, CommonException ce,
			ExternalProcessNotifyingHelper processHelper) {
		
		if(DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.get() == false) {
			return;
		}
		
		DaemonToolMessageConsole console = DaemonToolMessageConsole.getConsole();
		
		printProcessStartResult(console.infoOut, ">> Running: ", pb, ce);
		
		if(processHelper != null) {
			processHelper.getOutputListenersHelper().addListener(
				new ConsoleOuputProcessListener(console.stdOut, console.serverStdErr));
		}
	}
	
}