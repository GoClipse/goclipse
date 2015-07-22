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

import static melnorme.utilbox.core.CoreUtil.array;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.launch.ProcessLaunchInfo;
import melnorme.lang.ide.core.launch.ProcessLaunchInfoValidator;
import melnorme.lang.ide.core.launch.ProcessLaunchInfoValidator.ProcessLaunchInfoSettings;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangLaunchConfigurationDelegate extends LaunchConfigurationDelegate {
	
	public LangLaunchConfigurationDelegate() {
		super();
	}
	
	protected CoreException error(String message, Throwable exception) throws CoreException {
		throw LangCore.createCoreException(message, exception);
	}
	
	protected CoreException errorMsg(String messagePattern, Object... arguments) throws CoreException {
		throw error(MessageFormat.format(messagePattern, arguments), null);
	}
	
	/* ----------------- Launch ----------------- */
	
	protected ProcessLaunchInfo launchInfo;
	
	@Override
	public final ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		try {
			launchInfo = getLaunchValidator(configuration).getValidatedProcessLaunchInfo();
		} catch(CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
		
		if(ILaunchManager.RUN_MODE.equals(mode)) {
			return getLaunchForRunMode(configuration, mode);
		}
		if(ILaunchManager.DEBUG_MODE.equals(mode)) {
			return getLaunchForDebugMode(configuration, mode);
		}
		throw abort_UnsupportedMode(mode);
	}
	
	protected CoreException abort_UnsupportedMode(String mode) throws CoreException {
		return errorMsg(LaunchMessages.LCD_errINTERNAL_UnsupportedMode, mode);
	}
	
	protected ILaunch getLaunchForRunMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		return new Launch(configuration, mode, null);
	}
	
	@SuppressWarnings("unused")
	protected ILaunch getLaunchForDebugMode(ILaunchConfiguration configuration, String mode) throws CoreException {
		throw abort_UnsupportedMode(mode);
	}
	
	/* -----------------  Launch check ----------------- */ 
	
	@Override
	public final boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		
		if (monitor != null) {
			monitor.subTask(LaunchMessages.LCD_PreparingLaunch);
		}
		
		doPreLaunchCheck(launchInfo, configuration, mode, monitor);
		
		return super.preLaunchCheck(configuration, mode, monitor);
	}
	
	protected ProcessLaunchInfoValidator getLaunchValidator(ILaunchConfiguration config) {
		return new ProcessLaunchInfoValidator(new LaunchConfigLaunchInfoSettings(config));
	}
	
	public static class LaunchConfigLaunchInfoSettings implements ProcessLaunchInfoSettings {
		
		protected final ILaunchConfiguration config;
		
		public LaunchConfigLaunchInfoSettings(ILaunchConfiguration config) {
			this.config = config;
		}
		
		protected IStringVariableManager getVariableManager() {
			return VariablesPlugin.getDefault().getStringVariableManager();
		}
		
		protected String evaluateStringVars(String expression) throws CoreException {
			return getVariableManager().performStringSubstitution(expression);
		}
		
		@Override
		public String getProject_Attribute() throws CoreException {
			return evaluateStringVars(config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, ""));
		}
		
		@Override
		public String getBuildTarget_Attribute() throws CoreException {
			return config.getAttribute(LaunchConstants.ATTR_BUILD_TARGET, (String) null);
		}
		
		@Override
		public String getExecutablePath_Attribute() throws CoreException {
			boolean useDefault = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, true);
			return useDefault ? null : evaluateStringVars(config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, ""));
		}
		
		@Override
		public String getProgramArguments_Attribute() throws CoreException {
			return evaluateStringVars(config.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, ""));
		}
		
		@Override
		public String getWorkingDirectory_Attribute() throws CoreException {
			String rawValue = config.getAttribute(LaunchConstants.ATTR_WORKING_DIRECTORY, "");
			return getVariableManager().performStringSubstitution(rawValue, false);
		}
		
		@Override
		public Map<String, String> getEnvironmentVars() throws CoreException {
			return config.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, (Map<String, String>) null);
		}
		
		@Override
		public boolean getAppendEnvironmentVars() throws CoreException {
			return config.getAttribute(ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES, true);
		}
	}
	
	@SuppressWarnings("unused")
	protected boolean doPreLaunchCheck(ProcessLaunchInfo config, ILaunchConfiguration configuration, String mode,
			IProgressMonitor monitor) throws CoreException {
		return super.preLaunchCheck(configuration, mode, monitor);
	}
	
	@Override
	protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		// XXX: This could perhaps be improved in the future
		return array(launchInfo.getProject());
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		try {
			return doBuildForLaunch(launchInfo, configuration, mode, monitor);
		} catch(CommonException e) {
			throw LangCore.createCoreException(e);
		} catch(OperationCancellation e) {
			throw new OperationCanceledException();
		}
	}
	
	protected boolean doBuildForLaunch(ProcessLaunchInfo config, ILaunchConfiguration configuration, String mode,
			IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		BuildTarget buildTarget = config.getBuildTarget();
		if(buildTarget == null) {
			return super.buildForLaunch(configuration, mode, pm);
		} else {
			LangCore.getBuildManager().newBuildTargetOperation(config.getProject(), buildTarget).execute(pm);
			return false;
		}
	}
	
	@Override
	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		return array(launchInfo.project);
	}
	
	
	/* ----------------- Actual launch ----------------- */
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		monitor = (monitor == null) ? new NullProgressMonitor() : monitor;	
		
		EclipseUtils.checkMonitorCancelation_OCE(monitor);
		
		try {
			monitor.beginTask(LaunchMessages.LCD_StartingLaunchConfiguration(configuration.getName()), 10);
			
			launchProcess(configuration, launch, new SubProgressMonitor(monitor, 7));
			
		} catch (CoreException ce) {
			throw ce;
		} finally {
			monitor.done();
		}
	}
	
	@SuppressWarnings("unused")
	protected void launchProcess(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
			
		EclipseProcessLauncher processLauncher = new EclipseProcessLauncher(
			launchInfo.programFileLocation,
			launchInfo.workingDir,
			launchInfo.programArguments,
			launchInfo.environment,
			launchInfo.appendEnv,
			LaunchConstants.PROCESS_TYPE_ID
		);
		
		processLauncher.launchProcess(launch);
	}
	
}