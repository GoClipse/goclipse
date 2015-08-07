/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ProcessLaunchInfoValidator {
	
	protected final BuildTargetSettingsValidator buildSettingsValidator;
	
	protected final String programArguments;
	protected final String workingDirectory;
	protected final Map<String, String> environmentVars;
	protected final boolean appendEnvironmentVars;
	
	public ProcessLaunchInfoValidator(BuildTargetSettingsValidator buildSettingsValidator, String programArguments,
			String workingDirectory, Map<String, String> environmentVars, boolean appendEnvironmentVars) {
		this.buildSettingsValidator = buildSettingsValidator;
		this.programArguments = programArguments;
		this.workingDirectory = workingDirectory;
		this.environmentVars = environmentVars;
		this.appendEnvironmentVars = appendEnvironmentVars;
	}
	
	/* -----------------  ----------------- */
	
	protected IProject getProject() throws CommonException, CoreException {
		return buildSettingsValidator.getValidProject();
	}
	
	protected BuildTarget getBuildTarget() throws CoreException, CommonException {
		if(buildSettingsValidator.getBuildTargetName() == null){
			return null;
		}
		return buildSettingsValidator.getValidBuildTarget();
	}
	
	protected Location getValidExecutableFileLocation() throws CoreException, CommonException {
		return buildSettingsValidator.getValidExecutableLocation();
	}
	
	/* -----------------  ----------------- */
	
	public IPath getWorkingDirectory() throws CommonException, CoreException {
		IPath path = getDefinedWorkingDirectory();
		if(path == null) {
			return getDefaultWorkingDirectory();
		}
		return path;
	}
	
	public IPath getDefinedWorkingDirectory() throws CoreException {
		return workingDirectory.isEmpty() ? null : new org.eclipse.core.runtime.Path(workingDirectory);
	}
	
	public IPath getDefaultWorkingDirectory() throws CommonException, CoreException {
		return getProject().getLocation();
	}
	
	/* -----------------  ----------------- */

	public String[] getProgramArguments() throws CoreException {
		return DebugPlugin.parseArguments(programArguments);
	}
	
	/* -----------------  ----------------- */
	
	public Map<String, String> getValidEnvironmentVars() throws CoreException {
		return environmentVars;
	}
	
	/* ======================   ====================== */
	
	public ProcessLaunchInfo getValidProcessLaunchInfo() throws CommonException, CoreException {
		
		IProject project = getProject();
		BuildTarget buildTarget = getBuildTarget();
		Location programLoc = getValidExecutableFileLocation();
		
		String[] processArgs = getProgramArguments();
		IPath workingDirectory = getWorkingDirectory();
		Map<String, String> configEnv = getValidEnvironmentVars();
		
		return new ProcessLaunchInfo(
			project, 
			buildTarget, 
			programLoc,
			processArgs, 
			workingDirectory, 
			configEnv, 
			appendEnvironmentVars);
	}
	
}