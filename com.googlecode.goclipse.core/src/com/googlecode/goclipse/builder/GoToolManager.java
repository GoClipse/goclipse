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
package com.googlecode.goclipse.builder;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.listFrom;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.process.ExternalProcessEclipseHelper;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager extends AbstractProcessManager<IGoBuildListener> {
	
	protected static GoToolManager instance = new GoToolManager();
	
	public static GoToolManager getDefault() {
		return instance;
	}
	
	protected void notifyBuildStarting(IProject project) {
		for (IGoBuildListener processListener : processListenersHelper.getListeners()) {
			processListener.handleBuildStarted(project);
		}
	}
	
	protected void notifyBuildTerminated(IProject project) {
		for (IGoBuildListener processListener : processListenersHelper.getListeners()) {
			processListener.handleBuildTerminated(project);
		}
	}
	
	/* -----------------  ----------------- */
	
	public static Map<String, String> getGoToolEnvironment() {
		Map<String, String> goEnv = new HashMap<String, String>();
		
		String goroot = LangCore.getPreferences().getString(PreferenceConstants.GOROOT);
		String goos   = LangCore.getPreferences().getString(PreferenceConstants.GOOS);
		String goarch = LangCore.getPreferences().getString(PreferenceConstants.GOARCH);
		String gopath = LangCore.getPreferences().getString(PreferenceConstants.GOPATH);
		
		goEnv.put(GoConstants.GOROOT, goroot);
		goEnv.put(GoConstants.GOOS,   goos);
		goEnv.put(GoConstants.GOARCH, goarch);
		goEnv.put(GoConstants.GOPATH, gopath);
		
		return goEnv;
	}
	
	public RunExternalProcessTask newRunGoToolTask_defaultEnv(IProject project, IProgressMonitor pmonitor,
			ProcessBuilder pb) {
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
		return new RunExternalProcessTask(pb, project, pmonitor, processListenersHelper);
	}
	
	public ProcessBuilder prepareBuilder(List<String> commandLine) {
		assertTrue(commandLine.size() > 0);
		String goCommand = commandLine.get(0);
		ProcessBuilder pb = new ProcessBuilder(commandLine);
		GoToolManager.setWorkingFolder(pb, goCommand);
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
		return pb;
	}
	
	public static void setWorkingFolder(ProcessBuilder pBuilder, String command) {
		String workingFolder = Path.fromOSString(command).removeLastSegments(1).toOSString();
		if (workingFolder != null && workingFolder.length() > 0) {
			pBuilder.directory(new File(workingFolder));
		}
	}

	public ExternalProcessEclipseHelper runGoTool(String goCommand, IProject project, IProgressMonitor pm,
			String processInput) throws CoreException {
		ProcessBuilder pb = prepareBuilder(listFrom(goCommand));
		RunExternalProcessTask runTask = new RunExternalProcessTask(pb, project, pm, processListenersHelper);
		return runTask.startProcessAndAwait(processInput);
	}
	
	public ExternalProcessEclipseHelper runPrivateGoTool(String goCommand, List<String> args, String processInput)
			throws CoreException {
		ArrayList<String> commandLine = new ArrayList<>(args);
		commandLine.add(0, goCommand);
		ProcessBuilder pb = prepareBuilder(commandLine);
		
		NullProgressMonitor pm = new NullProgressMonitor();
		ExternalProcessEclipseHelper eclipseHelper = new ExternalProcessEclipseHelper(pb, true, pm);
		eclipseHelper.writeInput(processInput);
		return eclipseHelper;
	}
	
}