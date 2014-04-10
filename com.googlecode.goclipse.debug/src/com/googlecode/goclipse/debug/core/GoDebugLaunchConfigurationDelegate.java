/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.debug.core;


import melnorme.lang.ide.debug.core.GdbLaunchDelegateExtension;
import melnorme.lang.ide.debug.core.services.DebugServicesExtensions;
import melnorme.lang.ide.launching.ProcessSpawnInfo;

import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.ISourceLocator;

import com.googlecode.goclipse.core.launch.GoLaunchConfigurationDelegate;

public class GoDebugLaunchConfigurationDelegate extends GoLaunchConfigurationDelegate {
	
	protected final GdbLaunchDelegateExtension gdbLaunchDelegate = new GdbLaunchDelegateExtension() {
		@Override
		protected GdbLaunch createGdbLaunch(ILaunchConfiguration configuration, String mode, ISourceLocator locator)
				throws CoreException {
			return new GoGdbLaunch(configuration, mode, locator);
		}
		
		@Override
		protected DebugServicesExtensions createServicesExtensions() {
			return new GoDebugServicesExtensions();
		};
	};
	
	@Override
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode); // Run not supported
	}
	
	@Override
	public ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		
		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
		
		// Setup CDT config parameters
		String fullProgramPath = getProgramFullPath(configuration).toString();
		String workingDirPath = getWorkingDirectoryOrDefault(configuration).toString();
		// Need to pass raw args, because CDT will reevaluate variables.
		String progArgs = getProgramArguments_Attribute(configuration);
		
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME, fullProgramPath);
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, progArgs);
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, workingDirPath);
		// Note, environment is already setup, because it uses standard attributes:
		// ILaunchManager.ATTR_ENVIRONMENT_VARIABLES and ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES
		
		workingCopy.doSave();
		
		ILaunch launch = gdbLaunchDelegate.getLaunch(configuration, mode);
		return launch;
	}
	
	@Override
	protected void launchProcess(ProcessSpawnInfo processSpawnInfo, ILaunchConfiguration configuration,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String mode = launch.getLaunchMode();
		gdbLaunchDelegate.launch(configuration, mode, launch, monitor);
	}
	
}