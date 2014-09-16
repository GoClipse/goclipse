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
import java.util.List;

import melnorme.lang.ide.core.operations.DaemonEnginePreferences;
import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleListener;
import melnorme.lang.ide.ui.tools.console.DaemonTool_ConsoleListener;
import melnorme.lang.ide.ui.tools.console.ProcessOutputToConsoleListener;
import melnorme.lang.ide.ui.tools.console.ToolsConsole;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.builder.IGoBuildListener;

public class GoBuilderConsoleListener extends AbstractToolsConsoleListener implements IGoBuildListener {
	
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
	
	@Override
	public void handleProcessStarted(ProcessBuilder pb, IProject project, 
			ExternalProcessNotifyingHelper processHelper) {
		GoBuildConsole console = getOperationConsole(project, false);
		
		try {
			writeProcessStartPrefix(pb, console);
			
			processHelper.getOutputListenersHelper().addListener(new ProcessOutputToConsoleListener(console));
		} catch (IOException e) {
			return;
		}
	}
	
	protected void writeProcessStartPrefix(ProcessBuilder pb, GoBuildConsole console) throws IOException {
		console.infoOut.write(StringUtil.collToString(pb.command(), " ") + "\n");
	}
	
	@Override
	public void handleProcessStartFailure(ProcessBuilder pb, IProject project, IOException processStartException) {
		GoBuildConsole console = getOperationConsole(project, false);
		
		try {
			writeProcessStartPrefix(pb, console);
			console.infoOut.write(">>>  Failed to start process: \n");
			console.infoOut.write(processStartException.getMessage());
		} catch (IOException consoleIOE) {
			return;
		}
	}
	
	@Override
	public void engineDaemonStarted(ProcessBuilder pb, ExternalProcessNotifyingHelper processHelper) {
		if(DaemonEnginePreferences.DAEMON_CONSOLE_ENABLE.get()) {
			List<String> commandLine = pb.command();
			
			DaemonTool_ConsoleListener.getConsole().writeOperationInfo(">>> Starting gocode server:\n");
			DaemonTool_ConsoleListener.getConsole().writeOperationInfo("   " +
					StringUtil.collToString(commandLine, " ") + "\n");
			processHelper.getOutputListenersHelper().addListener(new DaemonTool_ConsoleListener());
		}
	}
	
	@Override
	public void engineDaemonFailedToStart(CoreException ce) {
		UIOperationExceptionHandler.handleError("Could not start gocode server:", ce.getCause());
	}
	
}