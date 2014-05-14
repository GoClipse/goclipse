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
import melnorme.utilbox.misc.MiscUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.utils.LaunchUtil;

public class GoLaunchConfigurationDelegate extends AbstractLangLaunchConfigurationDelegate {
	
	@Override
	protected IPath getProgramFullPath(ILaunchConfiguration configuration) throws CoreException {
		IPath programRelativePath = getProgramRelativePath(configuration);
		
		programRelativePath = concertSourcePathToExecutablePath(configuration, programRelativePath);
		
		IProject project = getProject(configuration);
		return project.getFile(programRelativePath).getLocation();
	}
	
	// For Go this can be not an executable but a Go source,
	// so we need to figure out the corresponding executable.
	// Original @author steel
	protected IPath concertSourcePathToExecutablePath(ILaunchConfiguration configuration, IPath programRelativePath)
			throws CoreException {
		
		IProject prj = getProject(configuration);
		
		if (Environment.INSTANCE.isCmdFile(programRelativePath)) {
			IPath binRel = Environment.INSTANCE.getBinOutputFolder(prj);
			IPath exeBase = binRel;
			String cmdName = LaunchUtil.getCmdName(programRelativePath);
			IPath executablePath = LaunchUtil.getExecutablePath(cmdName, prj);
			String executableName = executablePath.lastSegment();
			
			//BM: I don't know what difference it makes these two alternatives:
			if (!MiscUtil.OS_IS_WINDOWS) {
				executablePath = Path.fromOSString(".").append(executableName);
			} else {
				executablePath = Path.fromOSString(executableName);
			}
			
			programRelativePath = exeBase.append(executablePath);
		}
		return programRelativePath;
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