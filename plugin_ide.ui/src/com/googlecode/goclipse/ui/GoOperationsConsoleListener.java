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

import melnorme.lang.ide.ui.tools.console.AbstractToolsConsoleListener;
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
	protected ToolsConsole createConsole(String name) {
		return new GoBuildConsole(name);
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
	public void handleProcessStartResult(ProcessBuilder pb, IProject project,
			ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		new ProcessUIConsoleHandler(pb, project, ">> Running: ", false, processHelper, ce);
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
			">> Running: ", processHelper, ce);
		
	}
	
}