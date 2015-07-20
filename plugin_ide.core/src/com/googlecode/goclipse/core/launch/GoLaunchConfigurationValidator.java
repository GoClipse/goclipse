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
package com.googlecode.goclipse.core.launch;

import static melnorme.lang.ide.core.utils.ResourceUtils.loc;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.launching.LangLaunchConfigurationDelegate.LangLaunchConfigurationValidator;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class GoLaunchConfigurationValidator extends LangLaunchConfigurationValidator {
	
	public GoLaunchConfigurationValidator(ILaunchConfiguration config) {
		super(config);
	}
	
	@Override
	public Path getExecutableFilePath(BuildTarget buildTarget) throws CoreException, CommonException {
		/*FIXME: BUG here review*/
		Path path = super.getExecutableFilePath(buildTarget);
		if(path.isAbsolute()) {
			return path;
		}
		
		IProject project = getProject();
		Location goPackageLocation = loc(project.getLocation()).resolve(path); 
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
		Location goPathEntry = goEnv.getGoPath().findGoPathEntryForSourcePath(goPackageLocation);
		
		if (goPathEntry == null) {
			throw GoCore.createCoreException("Could not find specified Go package: " + path, null);
		} else {
			String cmdName = goPackageLocation.path.getFileName().toString(); // get last segment
			String executableName = cmdName + ProcessUtils.getExecutableSuffix();
			
			return loc(GoProjectEnvironment.getBinFolder(goPathEntry).append(executableName)).toPath();
		}
	}
}