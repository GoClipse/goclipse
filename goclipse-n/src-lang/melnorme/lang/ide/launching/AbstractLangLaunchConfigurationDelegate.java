/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 		DLTK project - initial code
 *      Bruno Medeiros - reimplementation, removed DLTK dependencies
 *******************************************************************************/
package melnorme.lang.ide.launching;

import java.text.MessageFormat;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

public abstract class AbstractLangLaunchConfigurationDelegate extends LaunchConfigurationDelegate {
	
	protected static IStringVariableManager getVariableManager() {
		return VariablesPlugin.getDefault().getStringVariableManager();
	}
	
	/* --------------------------------------------------- */
	
	protected IProject[] fOrderedProjects;
	
	protected CoreException abort(String message, Throwable exception) throws CoreException {
		throw LaunchingCore.createCoreException(exception, LaunchingCore.LAUNCHING_CONFIG_ERROR, message);
	}
	
	protected CoreException fail(String messagePattern, Object... arguments) throws CoreException {
		throw abort(MessageFormat.format(messagePattern, arguments), null);
	}
	
	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		
		if (monitor != null) {
			monitor.subTask(LaunchMessages.LCD_buildPrerequesite);
		}
		
		fOrderedProjects = null;
		IProject project = getProject(configuration);
		
		if (project != null) {
			fOrderedProjects = computeReferencedBuildOrder(new IProject[] { project });
		}
		
		return super.preLaunchCheck(configuration, mode, monitor);
	}
	
	@Override
	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		return fOrderedProjects;
	}
	
	@Override
	protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		return fOrderedProjects;
	}
	
	@Override
	protected boolean isLaunchProblem(IMarker problemMarker) throws CoreException {
		return super.isLaunchProblem(problemMarker);
	}
	
	protected String getProjectAttribute(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, (String) null);
	}
	
	protected String getProcessRelativePath_Attribute(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, (String) null);
	}
	
	protected String getProgramArguments_Attribute(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, "");
	}
	
	protected String getWorkingDirectory_Attribute(ILaunchConfiguration config) throws CoreException {
		return config.getAttribute(LaunchConstants.ATTR_WORKING_DIRECTORY, (String) null);
	}
	
	protected IProject getProject(ILaunchConfiguration configuration) throws CoreException {
		String projectName = getProjectAttribute(configuration);
		if (projectName != null) {
			projectName = projectName.trim();
			if (projectName.length() > 0) {
				return LangCore.getWorkspaceRoot().getProject(projectName);
			}
		}
		return null;
	}
	
	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		if(ILaunchManager.RUN_MODE.equals(mode)) {
			return getLaunchForRunMode(configuration, mode);
		}
		if(ILaunchManager.DEBUG_MODE.equals(mode)) {
			return getLaunchForDebugMode(configuration, mode);
		}
		throw abort_UnsupportedMode(mode);
	}
	
	protected CoreException abort_UnsupportedMode(String mode) throws CoreException {
		return fail(LaunchMessages.LCD_errINTERNAL_UnsupportedMode, mode);
	}
	
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new Launch(configuration, mode, null);
	}
	
	protected abstract ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode)
			throws CoreException;
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		if (monitor.isCanceled()) {
			return;
		}
		
		try {
			monitor.beginTask(
				MessageFormat.format(LaunchMessages.LCD_startingLaunchConfiguration, configuration.getName()),
				10);
			
			final ProcessSpawnInfo config = createProcessSpawnInfo(configuration);
			if (monitor.isCanceled()) {
				return;
			}
			monitor.worked(3);
			
			monitor.subTask(LaunchMessages.LCD_startingProcess);
			launchProcess(config, configuration, launch, new SubProgressMonitor(monitor, 7));
			
		} catch (CoreException ce) {
			throw ce;
		} finally {
			monitor.done();
		}
	}
	
	protected ProcessSpawnInfo createProcessSpawnInfo(ILaunchConfiguration configuration) throws CoreException {
		
		IPath processFullPath = getProgramFullPath(configuration);
		
		String[] processArgs = getProgramArguments(configuration);
		
		IPath workingDirectory = getWorkingDirectoryOrDefault(configuration);
		
		boolean appendEnv =
				configuration.getAttribute(ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES, true);
		Map<String, String> configEnv =
				configuration.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, (Map<?, ?>) null);
		
		return new ProcessSpawnInfo(processFullPath, processArgs, workingDirectory, configEnv, appendEnv);
	}
	
	protected IPath getProgramFullPath(ILaunchConfiguration configuration) throws CoreException {
		IPath programRelativePath = getProgramRelativePath(configuration);
		IProject project = getProject(configuration);
		return project.getFile(programRelativePath).getLocation();
	}
	
	protected IPath getProgramRelativePath(ILaunchConfiguration configuration) throws CoreException {
		String attribValueRaw = getProcessRelativePath_Attribute(configuration);
		if (attribValueRaw == null || attribValueRaw.isEmpty()) {
			fail(LaunchMessages.LCD_errProcessNotSpecified);
		}
		String expandedValue = getVariableManager().performStringSubstitution(attribValueRaw);
		if (expandedValue.isEmpty()) {
			fail(LaunchMessages.LCD_errProcessPathEmtpy);
		}
		return new Path(expandedValue);
	}
	
	protected String[] getProgramArguments(ILaunchConfiguration configuration) throws CoreException {
		String argumentsRaw = getProgramArguments_Attribute(configuration);
		String args = getVariableManager().performStringSubstitution(argumentsRaw);
		return DebugPlugin.parseArguments(args);
	}
	
	protected IPath getWorkingDirectoryOrDefault(ILaunchConfiguration configuration) throws CoreException {
		IPath path = getWorkingDirectory(configuration);
		if (path == null) {
			return getDefaultWorkingDirectory(configuration);
		}
		return path;
	}
	
	protected IPath getWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
		String path = getWorkingDirectory_Attribute(configuration);
		if (path == null || path.isEmpty()) {
			return null;
		} else {
			path = getVariableManager().performStringSubstitution(path, false);
			return new Path(path);
		}
	}
	
	protected IPath getDefaultWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
		// default working directory is the project if this config has a project
		IProject project = getProject(configuration);
		if (project != null) {
			return project.getLocation();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	protected void launchProcess(ProcessSpawnInfo processSpawnInfo, ILaunchConfiguration configuration,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		if(processSpawnInfo.programPath == null) {
			fail(LaunchMessages.LCD_errProcessPathEmtpy);
		}
		
		EclipseProcessLauncher processLauncher = new EclipseProcessLauncher(
				processSpawnInfo.workingDir,
				processSpawnInfo.programPath,
				processSpawnInfo.programArguments,
				processSpawnInfo.environment,
				processSpawnInfo.appendEnv,
				LaunchConstants.PROCESS_TYPE_ID
				);
		
		try {
			processLauncher.launchProcess(launch);
			monitor.worked(7);
		} finally {
			monitor.done();
		}
	}
	
}