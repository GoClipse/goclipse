/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.debug.core;


import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.ISourceLocator;

import melnorme.lang.ide.debug.core.services.DebugServicesExtensions;
import melnorme.lang.ide.launching.ProcessSpawnInfo;

public abstract class AbstractLangDebugLaunchConfigurationDelegate extends LangLaunchConfigurationDelegate_Actual {
	
	public AbstractLangDebugLaunchConfigurationDelegate() {
	}
	
	protected final GdbLaunchDelegateExtension gdbLaunchDelegate = createGdbLaunchDelegate();
	
	protected GdbLaunchDelegateExtension createGdbLaunchDelegate() {
		return new GdbLaunchDelegateExt();
	}
	
	protected class GdbLaunchDelegateExt extends GdbLaunchDelegateExtension {
		@Override
		protected GdbLaunch createGdbLaunch(ILaunchConfiguration configuration, String mode,
				ISourceLocator locator) throws CoreException {
			return doCreateGdbLaunch(configuration, mode, locator);
		}
		
		@Override
		protected DebugServicesExtensions createServicesExtensions() {
			return doCreateServicesExtensions();
		}
	}
	
	protected abstract GdbLaunch doCreateGdbLaunch(ILaunchConfiguration configuration, String mode,
			ISourceLocator locator);
	
	protected DebugServicesExtensions doCreateServicesExtensions() {
		return new DebugServicesExtensions();
	}
	
	@Override
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode); // Run not supported
	}
	
	@Override
	public ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		
		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
		
		setAttributes(configuration, workingCopy);
		
		workingCopy.doSave();
		
		ILaunch launch = gdbLaunchDelegate.getLaunch(configuration, mode);
		return launch;
	}
	
	protected void setAttributes(ILaunchConfiguration configuration, ILaunchConfigurationWorkingCopy workingCopy)
			throws CoreException {
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
	}
	
	@Override
	protected void launchProcess(ProcessSpawnInfo processSpawnInfo, ILaunchConfiguration configuration, 
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String mode = launch.getLaunchMode();
		gdbLaunchDelegate.launch(configuration, mode, launch, monitor);
	}
	
}