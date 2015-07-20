/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.launch;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class ProcessLaunchInfoValidator extends ProjectBuildExecutableFileValidator {
	
	public abstract String getProgramArguments_Attribute() throws CoreException;
	public abstract String getWorkingDirectory_Attribute() throws CoreException;
	public abstract Map<String, String> getEnvironmentVars() throws CoreException;
	public abstract boolean getAppendEnvironmentVars() throws CoreException;
	
	/* -----------------  ----------------- */
	
	public IPath getWorkingDirectory() throws CoreException, StatusException {
		IPath path = getDefinedWorkingDirectory();
		if(path == null) {
			return getDefaultWorkingDirectory();
		}
		return path;
	}
	
	public IPath getDefinedWorkingDirectory() throws CoreException {
		String path = getWorkingDirectory_Attribute();
		return path.isEmpty() ? null : new org.eclipse.core.runtime.Path(path);
	}
	
	public IPath getDefaultWorkingDirectory() throws CoreException, StatusException {
		return getProject().getLocation();
	}
	
	/* -----------------  ----------------- */

	public String[] getProgramArguments() throws CoreException {
		return DebugPlugin.parseArguments(getProgramArguments_Attribute());
	}
	
	
	/* ======================   ====================== */
	
	public ProcessLaunchInfo getValidatedProcessLaunchInfo() throws CoreException, CommonException {
		
		IProject project = getProject();
		BuildTarget buildTarget = getBuildTarget();
		Location programLoc = getExecutableFileLocation(buildTarget);
		String[] processArgs = getProgramArguments();
		IPath workingDirectory = getWorkingDirectory();
		Map<String, String> configEnv = getEnvironmentVars();
		boolean appendEnv = getAppendEnvironmentVars();
		
		return new ProcessLaunchInfo(
			project, 
			buildTarget, 
			programLoc,
			processArgs, 
			workingDirectory, 
			configEnv, 
			appendEnv);
	}
	
}