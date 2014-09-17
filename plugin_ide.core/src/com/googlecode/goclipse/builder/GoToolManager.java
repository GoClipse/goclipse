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

import static melnorme.lang.ide.core.utils.ResourceUtils.getLocation;
import static melnorme.utilbox.core.CoreUtil.listFrom;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import melnorme.lang.ide.core.operations.AbstractToolsManager;
import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager extends AbstractToolsManager<IGoBuildListener> {
	
	protected static GoToolManager instance = new GoToolManager();
	
	public static GoToolManager getDefault() {
		return instance;
	}
	
	protected void notifyBuildStarting(IProject project) {
		for (IGoBuildListener processListener : getListeners()) {
			processListener.handleBuildStarted(project);
		}
	}
	
	protected void notifyBuildTerminated(IProject project) {
		for (IGoBuildListener processListener : getListeners()) {
			processListener.handleBuildTerminated(project);
		}
	}
	
	/* ----------------- ----------------- */
	
	public RunGoToolTask createRunToolTask(ProcessBuilder pb, IProject project, IProgressMonitor pm) {
		return new RunGoToolTask(pb, project, pm);
	}
	
	public class RunGoToolTask extends RunExternalProcessTask<IExternalProcessListener> {
		public RunGoToolTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor) {
			super(pb, project, cancelMonitor, GoToolManager.this);
		}
	}
	
	/* -----------------  ----------------- */
	
	public static Map<String, String> getGoToolEnvironment() {
		Map<String, String> goEnv = new HashMap<String, String>();
		
		goEnv.put(GoConstants.GOROOT, PreferenceConstants.GO_ROOT.get());
		goEnv.put(GoConstants.GOOS,   PreferenceConstants.GO_OS.get());
		goEnv.put(GoConstants.GOARCH, PreferenceConstants.GO_ARCH.get());
		goEnv.put(GoConstants.GOPATH, PreferenceConstants.GO_PATH.get());
		
		return goEnv;
	}
	
	@Override
	protected void setupDefaultEnvironment(ProcessBuilder pb) {
		pb.environment().putAll(GoToolManager.getGoToolEnvironment());
	}
	
	/* -----------------  ----------------- */

	public ExternalProcessResult runGoTool(String goCommand, IProject project, IProgressMonitor pm,
			String processInput) throws CoreException {
		ProcessBuilder pb = createDefaultProcessBuilder(listFrom(goCommand), getLocation(project));
		RunGoToolTask runTask = new RunGoToolTask(pb, project, pm);
		EclipseExternalProcessHelper processHelper = runTask.startProcess();
		processHelper.writeInput(processInput);
		return processHelper.strictAwaitTermination();
	}
	
	public ExternalProcessResult runBuildTool(IProject project, IProgressMonitor pm, 
			File workingDir, List<String> commandLine, String goPath) throws CoreException {
		
		ProcessBuilder pb = createDefaultProcessBuilder(commandLine, workingDir); 
		
		if(goPath != null) {
			pb.environment().put(GoConstants.GOPATH, goPath);
		}
		
		return runBuildTool(project, pm, pb);
	}
	
	public ExternalProcessResult runBuildTool(final IProject project, IProgressMonitor pm,
			ProcessBuilder pb) throws CoreException {
		// Note: project can be null
		RunGoToolTask processTask = createRunToolTask(pb, project, pm);
		
		return processTask.startProcess().strictAwaitTermination();
	}
	
}