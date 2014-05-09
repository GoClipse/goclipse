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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.preferences.PreferenceConstants;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.process.ExternalProcessEclipseHelper;
import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.misc.ListenerListHelper;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager {
	
	protected static GoToolManager instance = new GoToolManager();
	
	public static GoToolManager getDefault() {
		return instance;
	}
	
	public interface GoBuildListener extends IExternalProcessListener {
		
		public void handleBuildStarted(IProject project);
		
		public void handleBuildTerminated(IProject project);
		
	}
	
	/* ----------------- listeners ----------------- */
	
	protected final ListenerListHelper<GoBuildListener> processListenersHelper = new ListenerListHelper<>();
	
	public void addBuildProcessListener(GoBuildListener processListener) {
		processListenersHelper.addListener(processListener);
	}
	
	public void removeBuildProcessListener(GoBuildListener processListener) {
		processListenersHelper.removeListener(processListener);
	}

	protected void notifyBuildStarting(IProject project) {
		for (GoBuildListener processListener : processListenersHelper.getListeners()) {
			processListener.handleBuildStarted(project);
		}
	}
	
	protected void notifyBuildTerminated(IProject project) {
		for (GoBuildListener processListener : processListenersHelper.getListeners()) {
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
	
	public RunExternalProcessTask getRunGoToolTask(IProject project, IProgressMonitor pmonitor, ProcessBuilder pb) {
		return new RunExternalProcessTask(pb, project, pmonitor, processListenersHelper);
	}
	
	public RunExternalProcessTask newRunGoToolTask_defaultEnv(IProject project, IProgressMonitor pmonitor,
			ProcessBuilder pb) {
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
		return new RunExternalProcessTask(pb, project, pmonitor, processListenersHelper);
	}

	public ExternalProcessEclipseHelper runGoTool(String goCommand, IProject project, IProgressMonitor pm,
			String processInput) throws CoreException {
		ProcessBuilder pb = new ProcessBuilder(goCommand);
		ExternalCommand.setWorkingFolder(pb, goCommand);
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
		RunExternalProcessTask runTask = new RunExternalProcessTask(pb, project, pm, processListenersHelper);
		return runTask.startProcessAndAwait(processInput);
	}
	
}