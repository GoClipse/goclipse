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


import static melnorme.utilbox.misc.StringUtil.emptyAsNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.cdt.core.parser.util.StringUtil;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.dsf.debug.service.IDsfDebugServicesFactory;
import org.eclipse.cdt.dsf.gdb.internal.GdbPlugin;
import org.eclipse.cdt.dsf.gdb.launching.GdbLaunch;
import org.eclipse.cdt.dsf.gdb.launching.LaunchUtils;
import org.eclipse.cdt.dsf.gdb.service.GDBBackend;
import org.eclipse.cdt.dsf.service.DsfSession;
import org.eclipse.cdt.utils.spawner.ProcessFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.ISourceLocator;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.debug.core.services.LangDebugServicesExtensions;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.utilbox.misc.ArrayUtil;

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
		protected LangDebugServicesExtensions createServicesExtensions(IDsfDebugServicesFactory parentServiceFactory) {
			return doCreateServicesExtensions(parentServiceFactory);
		}
	}
	
	protected abstract GdbLaunch doCreateGdbLaunch(ILaunchConfiguration configuration, String mode,
			ISourceLocator locator);
	
	protected LangDebugServicesExtensions doCreateServicesExtensions(IDsfDebugServicesFactory parentServiceFactory) {
		return new LangDebugServicesExtensions(parentServiceFactory);
	}
	
	@Override
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode); // Run not supported
	}
	
	@Override
	public ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		
		ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
		
		setAttributes(workingCopy);
		
		workingCopy.doSave();
		
		return gdbLaunchDelegate.getLaunch(configuration, mode);
	}
	
	protected void setAttributes(ILaunchConfigurationWorkingCopy workingCopy) throws CoreException {
		
		// Setup CDT config parameters
		
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
			processLauncher.programFileLocation.toString());
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
			workingCopy.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, ""));
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
			processLauncher.workingDir.toString());
		workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			processLauncher.project.getName());
		
		if(!workingCopy.hasAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN)) {
			workingCopy.setAttribute(ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN, false);
		}
		
		// Note, environment is already setup, because it uses standard attributes:
		// ILaunchManager.ATTR_ENVIRONMENT_VARIABLES and ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES
	}
	
	@Override
	protected void launchProcess(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		String mode = launch.getLaunchMode();
		gdbLaunchDelegate.launch(configuration, mode, launch, monitor);
	}
	
	/* -----------------  ----------------- */
	
	public static class GDBBackend_Lang extends GDBBackend {
		
		protected final ILaunchConfiguration fLaunchConfiguration;
		protected final IProject project;
		
		public GDBBackend_Lang(DsfSession session, ILaunchConfiguration lc) {
			super(session, lc);
			
			this.fLaunchConfiguration = lc;
			this.project = getProject(lc);
		}
		
		@Override
		protected Process launchGDBProcess(String[] commandLine) throws CoreException {
			String[] launchEnvironment = LaunchUtils.getLaunchEnvironment(fLaunchConfiguration);
			if(launchEnvironment != null) {
				// launchEnvironment should be usually be null GDB itself
				LangCore.logWarning("Ignoring previous CDT GDB launch environment");
			}
			
			HashMap<String, String> envMap = new HashMap<>(System.getenv());
			customizeEnvironment(envMap);
			launchEnvironment = convertoToEnvpFormat(envMap);
			
			try {
				return ProcessFactory.getFactory().exec(commandLine, launchEnvironment);
			} catch (IOException e) {
			    String message = "Error while launching command: " + StringUtil.join(commandLine, " ");
			    throw new CoreException(new Status(IStatus.ERROR, GdbPlugin.PLUGIN_ID, -1, message, e));
			}
		}
		
		@SuppressWarnings("unused")
		protected void customizeEnvironment(HashMap<String, String> envMap) {
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public static IProject getProject(ILaunchConfiguration lc) {
		try {
			String prjName = lc.getAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME, (String) null);
			if(emptyAsNull(prjName) != null) {
				return ResourceUtils.getProject(prjName);
			}
		} catch(CoreException e) {
		}
		return null;
	}
	
	public static String[] convertoToEnvpFormat(HashMap<String, String> envMap) {
		List<String> envp = new ArrayList<>(envMap.size());
		for(Entry<String, String> entry : envMap.entrySet()) {
			envp.add(entry.getKey() + "=" + entry.getValue());
		}
		return ArrayUtil.createFrom(envp, String.class);
	}
	
}