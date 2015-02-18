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
package com.googlecode.goclipse.core.operations;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolsManager;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

/**
 * Manager for running various go tools, usually for build.
 * Note that running such tools under this class will notify Eclipse console listeners.
 */
public class GoToolManager extends AbstractToolsManager<IGoOperationsListener> {
	
	public static GoToolManager getDefault() {
		return (GoToolManager) LangCore.getToolManager();
	}
	
	public void notifyBuildStarting(IProject project, boolean clearConsole) {
		for (IGoOperationsListener processListener : getListeners()) {
			processListener.handleBuildStarted(project, clearConsole);
		}
	}
	
	public void notifyBuildTerminated(IProject project) {
		for (IGoOperationsListener processListener : getListeners()) {
			processListener.handleBuildTerminated(project);
		}
	}
	
	/* -----------------  ----------------- */

	public ExternalProcessResult runBuildTool(GoEnvironment goEnv, IProgressMonitor pm, File workingDir, 
			List<String> commandLine) throws CoreException {
		return runBuildTool(goEnv, pm, workingDir.toPath(), commandLine);
	}
	
	public ExternalProcessResult runBuildTool(GoEnvironment goEnv, IProgressMonitor pm, Path workingDir, 
			List<String> commandLine) throws CoreException {
		
		ProcessBuilder pb = goEnv.createProcessBuilder(commandLine, workingDir);
		return runTool(null, pm, pb);
	}
	
}