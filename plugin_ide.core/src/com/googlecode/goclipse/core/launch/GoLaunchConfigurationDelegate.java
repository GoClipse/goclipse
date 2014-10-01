/*******************************************************************************
 * Copyright (c) 2011, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 		Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.launch;


import melnorme.lang.ide.launching.AbstractLangLaunchConfigurationDelegate;
import melnorme.lang.tooling.ProcessUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoLaunchConfigurationDelegate extends AbstractLangLaunchConfigurationDelegate {
	
	@Override
	protected IPath getProgramFullPath(ILaunchConfiguration configuration) throws CoreException {
		IPath programRelativePath = getProgramRelativePath(configuration);
		
		return concertSourcePathToExecutablePath(configuration, programRelativePath);
	}
	
	// For Go this can be not an executable but a Go source,
	// so we need to figure out the corresponding executable.
	// Original @author steel
	protected IPath concertSourcePathToExecutablePath(ILaunchConfiguration configuration, IPath programRelativePath)
			throws CoreException {
		
		IProject project = getProject(configuration);
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		java.nio.file.Path programFullPath = project.getLocation().append(programRelativePath).toFile().toPath();
		java.nio.file.Path goWorkspaceEntry = goEnv.getGoPath().getGoPathEntryForSourceModule(programFullPath);
		
		if (goWorkspaceEntry != null) {
			
			String cmdName = programRelativePath.removeFileExtension().lastSegment();
			String executableName = cmdName + ProcessUtils.getExecutableSuffix();
			
			return GoProjectEnvironment.getBinFolder(goWorkspaceEntry).append(executableName);
		}
		return project.getFile(programRelativePath).getLocation();
	}

	@Override
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new Launch(configuration, mode, null);
	}
	
	@Override
	protected ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode);
	}
	
}