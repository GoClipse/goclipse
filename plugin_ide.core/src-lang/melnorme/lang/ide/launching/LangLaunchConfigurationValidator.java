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
package melnorme.lang.ide.launching;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;

public abstract class LangLaunchConfigurationValidator {
	
	protected CoreException error(String message) throws CoreException {
		throw error(message, null);
	}
	
	protected CoreException error(String message, Throwable exception) throws CoreException {
		throw LangCore.createCoreException(message, exception);
	}
	
	protected CoreException errorMsg(String messagePattern, Object... arguments) throws CoreException {
		throw error(MessageFormat.format(messagePattern, arguments), null);
	}
	
	/* ======================   ====================== */
	
	
	public abstract String getProject_Attribute() throws CoreException;
	
	public abstract String getExecutablePath_Attribute() throws CoreException;
	
	public abstract String getProgramArguments_Attribute() throws CoreException;
	
	public abstract String getWorkingDirectory_Attribute() throws CoreException;
	
	public abstract Map<String, String> getEnvironmentVars() throws CoreException;
	
	public abstract boolean getAppendEnvironmentVars() throws CoreException;

	/* -----------------  ----------------- */
	
	protected IProject getProject() throws CoreException {
		String projectName = getProject_Attribute().trim();
		if(projectName.length() > 0) {
			return EclipseUtils.getWorkspaceRoot().getProject(projectName);
		}
		return null;
	}
	
	protected IProject getProject_nonNull() throws CoreException {
		IProject project = getProject();
		if(project == null) {
			error("No project specified");
		}
		return project;
	}
	
	/* -----------------  ----------------- */
	
	protected IPath getExecutablePath() throws CoreException {
		String exePath = getExecutablePath_Attribute();
		if(exePath.isEmpty()) {
			error(LaunchMessages.LCD_errProcessPathEmtpy);
		}
		return new Path(exePath);
	}
	
	protected IPath getProgramFullPath() throws CoreException {
		IPath exePath = getExecutablePath();
		if(exePath.isAbsolute()) {
			return exePath;
		}
		// Otherwise path is relative to project location
		return getProject_nonNull().getFile(exePath).getLocation();
	}
	
	
	/* -----------------  ----------------- */
	
	protected IPath getWorkingDirectory() throws CoreException {
		IPath path = getDefinedWorkingDirectory();
		if(path == null) {
			return getDefaultWorkingDirectory();
		}
		return path;
	}
	
	protected IPath getDefinedWorkingDirectory() throws CoreException {
		String path = getWorkingDirectory_Attribute();
		return path.isEmpty() ? null : new Path(path);
	}
	
	protected IPath getDefaultWorkingDirectory() throws CoreException {
		return getProject_nonNull().getLocation();
	}
	
	/* -----------------  ----------------- */

	protected String[] getProgramArguments() throws CoreException {
		String args = getProgramArguments_Attribute();
		return DebugPlugin.parseArguments(args);
	}
	
	
	/* ======================   ====================== */
	
	protected ProcessLaunchInfo getValidateProcessLaunchInfo() throws CoreException {
		
		IPath processFullPath = getProgramFullPath();
		String[] processArgs = getProgramArguments();
		IPath workingDirectory = getWorkingDirectory();
		Map<String, String> configEnv = getEnvironmentVars();
		boolean appendEnv = getAppendEnvironmentVars();
		IProject project = getProject();
		
		return new ProcessLaunchInfo(processFullPath, processArgs, workingDirectory, configEnv, appendEnv, 
			project, null);
	}

}