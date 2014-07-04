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
import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

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
	
	/* ----------------- ----------------- */
	
	public RunGoToolTask createRunProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor monitor) {
		return new RunGoToolTask(pb, project, monitor);
	}
	
	public class RunGoToolTask extends RunExternalProcessTask<IGoBuildListener> {
		public RunGoToolTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor) {
			super(pb, project, cancelMonitor, processListenersHelper);
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
	
	public RunGoToolTask newRunGoToolTask_defaultEnv(IProject project, IProgressMonitor pmonitor, ProcessBuilder pb) {
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
		return new RunGoToolTask(pb, project, pmonitor);
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

	public ExternalProcessResult runGoTool(String goCommand, IProject project, IProgressMonitor pm,
			String processInput) throws CoreException {
		ProcessBuilder pb = prepareBuilder(listFrom(goCommand));
		RunGoToolTask runTask = new RunGoToolTask(pb, project, pm);
		EclipseExternalProcessHelper processHelper = runTask.startProcess();
		processHelper.writeInput(processInput);
		return processHelper.strictAwaitTermination();
	}
	
	/** Starts an external go command without notifying any listeners. */
	public EclipseExternalProcessHelper startPrivateGoTool(String goCommand, List<String> args, String processInput)
			throws CoreException {
		ArrayList<String> commandLine = new ArrayList<>(args);
		commandLine.add(0, goCommand);
		ProcessBuilder pb = prepareBuilder(commandLine);
		
		NullProgressMonitor pm = new NullProgressMonitor();
		EclipseExternalProcessHelper processHelper = new EclipseExternalProcessHelper(pb, true, pm);
		processHelper.writeInput(processInput);
		return processHelper;
	}
	
}