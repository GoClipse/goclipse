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

import java.io.File;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolsManager;
import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.lang.ide.core.utils.process.IExternalProcessListener;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

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

	public ExternalProcessResult runGoTool(IProject project, IProgressMonitor pm, ProcessBuilder pb,
			String processInput, boolean throwOnNonZero) throws CoreException {
		File workingDir = getLocation(project);
		if(workingDir != null) {
			pb.directory(workingDir);
		}
		RunGoToolTask runTask = new RunGoToolTask(pb, project, pm);
		EclipseExternalProcessHelper processHelper = runTask.startProcess();
		processHelper.writeInput(processInput);
		ExternalProcessResult processResult = processHelper.strictAwaitTermination();
		
		if(throwOnNonZero) {
			if (processResult.exitValue != 0) {
				String command = pb.command().get(0);
				throw LangCore.createCoreException(
					command + " completed with non-zero exit value (" + processResult.exitValue + ")", null);
			}
		}
		
		return processResult;
	}
	
	public ExternalProcessResult runBuildTool(GoEnvironment goEnv, IProject project, IProgressMonitor pm, 
			File workingDir, List<String> commandLine) throws CoreException {
		
		ProcessBuilder pb = goEnv.createProcessBuilder(commandLine);
		if(workingDir != null) {
			pb.directory(workingDir);
		}
		
		return runBuildTool(project, pm, pb);
	}
	
	public ExternalProcessResult runBuildTool(IProject project, IProgressMonitor pm, 
			ProcessBuilder pb) throws CoreException {
		// Note: project can be null
		RunGoToolTask processTask = createRunToolTask(pb, project, pm);
		
		return processTask.startProcess().strictAwaitTermination();
	}
	
	/* ----------------- ----------------- */
	
}