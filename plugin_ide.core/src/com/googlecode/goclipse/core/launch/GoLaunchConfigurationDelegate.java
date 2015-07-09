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


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.launching.LangLaunchConfigurationDelegate;
import melnorme.lang.ide.launching.ProcessLaunchInfoValidator;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.misc.Location;

public class GoLaunchConfigurationDelegate extends LangLaunchConfigurationDelegate {
	
	@Override
	protected ProcessLaunchInfoValidator getLaunchValidator(ILaunchConfiguration config) {
		return new LangLaunchConfigurationValidator(config) {
			// Return the absolute path of the executable to launch.
			
			@Override
			protected IPath getExecutableAbsolutePath() throws CoreException {
				IPath path = getExecutablePath();
				if(path.isAbsolute()) {
					return path;
				}
				
				IProject project = getProject_nonNull();
				Location goPackageLocation = ResourceUtils.eloc(project.getLocation().append(path)); 
				
				GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
				Location goPathEntry = goEnv.getGoPath().findGoPathEntryForSourcePath(goPackageLocation);
				
				if (goPathEntry == null) {
					throw GoCore.createCoreException("Could not find specified Go package: " + path, null);
				} else {
					String cmdName = goPackageLocation.path.getFileName().toString(); // get last segment
					String executableName = cmdName + ProcessUtils.getExecutableSuffix();
					
					return GoProjectEnvironment.getBinFolder(goPathEntry).append(executableName);
				}
			}
		};
	}
	
}