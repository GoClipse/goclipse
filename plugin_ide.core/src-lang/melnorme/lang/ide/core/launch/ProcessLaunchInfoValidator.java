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

import melnorme.lang.ide.core.launch.ProjectBuildArtifactValidator.ProjectBuildExecutableSettings;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ProcessLaunchInfoValidator {
	
	public final ProcessLaunchInfoSettings settings;
	protected final ProjectBuildArtifactValidator buildExecutableValidator;

	public ProcessLaunchInfoValidator(ProcessLaunchInfoSettings settings) {
		this.settings = settings;
		this.buildExecutableValidator = init_ProjectBuildExecutableFileValidator(settings);
	}
	
	protected ProjectBuildArtifactValidator init_ProjectBuildExecutableFileValidator(
			ProcessLaunchInfoSettings settings) {
		return new ProjectBuildArtifactValidator(settings);
	}
	
	public static interface ProcessLaunchInfoSettings extends ProjectBuildExecutableSettings {
		
		public abstract String getProgramArguments_Attribute() throws CoreException;
		public abstract String getWorkingDirectory_Attribute() throws CoreException;
		public abstract Map<String, String> getEnvironmentVars() throws CoreException;
		public abstract boolean getAppendEnvironmentVars() throws CoreException;
		
	}
	
	protected IProject getProject() throws CommonException, CoreException {
		return buildExecutableValidator.getValidProject();
	}
	
	protected BuildTarget getBuildTarget() throws CoreException, CommonException {
		return buildExecutableValidator.getBuildTarget();
	}
	
	protected Location getValidExecutableFileLocation() throws CoreException, CommonException {
		return buildExecutableValidator.getValidExecutableFileLocation();
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
		String path = settings.getWorkingDirectory_Attribute();
		return path.isEmpty() ? null : new org.eclipse.core.runtime.Path(path);
	}
	
	public IPath getDefaultWorkingDirectory() throws CommonException, CoreException {
		return getProject().getLocation();
	}
	
	/* -----------------  ----------------- */

	public String[] getProgramArguments() throws CoreException {
		return DebugPlugin.parseArguments(settings.getProgramArguments_Attribute());
	}
	
	
	/* ======================   ====================== */
	
	public ProcessLaunchInfo getValidatedProcessLaunchInfo() throws CoreException, CommonException {
		
		IProject project = getProject();
		BuildTarget buildTarget = getBuildTarget();
		Location programLoc = getValidExecutableFileLocation();
		String[] processArgs = getProgramArguments();
		IPath workingDirectory = getWorkingDirectory();
		Map<String, String> configEnv = settings.getEnvironmentVars();
		boolean appendEnv = settings.getAppendEnvironmentVars();
		
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