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


import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.launching.AbstractLangLaunchConfigurationDelegate;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoLaunchConfigurationDelegate extends AbstractLangLaunchConfigurationDelegate {
	
	// Return the absolute path of the executable to launch.
	@Override
	protected IPath getProgramFullPath(ILaunchConfiguration configuration) throws CoreException {
		IPath path = getLaunchablePath(configuration, false);
		if(path.isAbsolute()) {
			return path;
		}
		
		IProject project = getProject(configuration);
		IResource launchResource = project.findMember(path);
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		
		Location goPackageLocation = ResourceUtils.getResourceLocation(launchResource);
		Location goPathEntry = goEnv.getGoPath().findGoPathEntryForSourcePath(goPackageLocation);
		
		if (goPathEntry == null) {
			throw GoCore.createCoreException("Given Go package not found: " + path, null);
		} else {
			String cmdName = goPackageLocation.path.getFileName().toString(); // get last segment
			String executableName = cmdName + ProcessUtils.getExecutableSuffix();
			
			return GoProjectEnvironment.getBinFolder(goPathEntry).append(executableName);
		}
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