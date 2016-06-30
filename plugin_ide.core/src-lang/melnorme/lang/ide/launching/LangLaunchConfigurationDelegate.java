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
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.variables.IStringVariableManager;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import melnorme.lang.ide.core.EclipseCore;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.launch.CompositeBuildTargetSettings;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class LangLaunchConfigurationDelegate extends LaunchConfigurationDelegate {
	
	public LangLaunchConfigurationDelegate() {
		super();
	}
	
	protected CoreException error(String message, Throwable exception) throws CoreException {
		throw EclipseCore.createCoreException(message, exception);
	}
	
	protected CoreException errorMsg(String messagePattern, Object... arguments) throws CoreException {
		throw error(MessageFormat.format(messagePattern, arguments), null);
	}
	
	/* ----------------- Launch ----------------- */
	
	protected CompositeBuildTargetSettings buildTargetSettings;
	protected EclipseProcessLauncher processLauncher;
	
	@Override
	public final ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
		try {
			buildTargetSettings = getBuildTargetSettings(configuration);
			processLauncher = getValidLaunchInfo(configuration, buildTargetSettings);
		} catch(CommonException ce) {
			throw EclipseCore.createCoreException(ce);
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
	
	protected CompositeBuildTargetSettings getBuildTargetSettings(ILaunchConfiguration configuration)
			throws CoreException, CommonException {
		BuildTargetLaunchCreator launchSettings = new BuildTargetLaunchCreator(configuration);
		
		BuildTargetSource buildTargetSource = new BuildTargetSource() {
			@Override
			public ProjectValidator getProjectValidator() {
				return new ProjectValidator();
			};
			
			@Override
			public String getProjectName() {
				return launchSettings.projectName;
			}
			
			@Override
			public String getBuildTargetName() {
				return launchSettings.getTargetName();
			}
		};
		
		return new CompositeBuildTargetSettings(buildTargetSource) {
			@Override
			public String getExecutablePath() {
				return launchSettings.getExecutablePath();
			}
			
			@Override
			public CommandInvocation getBuildCommand() {
				return launchSettings.getBuildCommand();
			}
		};
	}
	
	protected EclipseProcessLauncher getValidLaunchInfo(
			ILaunchConfiguration configuration, CompositeBuildTargetSettings buildTargetSettings)
			throws CommonException, CoreException {
		
		BuildManager buildManager = LangCore.getBuildManager();
		
		IProject project = buildTargetSettings.getBuildTargetSupplier().getValidProject();
		
		BuildTarget buildTarget = buildTargetSettings.getValidBuildTarget();
		CommonOperation buildOperation = buildTarget == null ? 
				null : buildManager.newBuildTargetOperation(project, buildTarget);
		
		String workingDirectoryString = evaluateStringVars(
			configuration.getAttribute(LaunchConstants.ATTR_WORKING_DIRECTORY, (String) null));

		IPath workingDirectory = workingDirectoryString == null ? 
				project.getLocation() : 
				new Path(workingDirectoryString);
		
		Location programLoc = buildTarget.getValidExecutableLocation(); // not null
		
		String programArguments = configuration.getAttribute(LaunchConstants.ATTR_PROGRAM_ARGUMENTS, "");
		String commandLine = programLoc.toString() + " " + programArguments;
		
		Map<String, String> configEnv = configuration.getAttribute(ILaunchManager.ATTR_ENVIRONMENT_VARIABLES, 
			new HashMap<>(0));
		boolean appendEnv = configuration.getAttribute(ILaunchManager.ATTR_APPEND_ENVIRONMENT_VARIABLES, true);
		
		CommandInvocation programInvocation = new CommandInvocation(commandLine, new HashMap2<>(configEnv), appendEnv);
		
		return new EclipseProcessLauncher(
			project, 
			buildOperation, 
			programLoc,
			workingDirectory, 
			programInvocation,
			LaunchConstants.PROCESS_TYPE_ID
		);
	}
	
	/* -----------------  Launch check ----------------- */ 
	
	@Override
	public final boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		
		if (monitor != null) {
			monitor.subTask(LaunchMessages.LCD_PreparingLaunch);
		}
		
		doPreLaunchCheck(configuration, mode, monitor);
		
		return super.preLaunchCheck(configuration, mode, monitor);
	}
	
	protected IStringVariableManager getVariableManager() {
		return VariablesPlugin.getDefault().getStringVariableManager();
	}
	
	protected String evaluateStringVars(String expression) throws CoreException {
		if(expression == null) {
			return null;
		}
		return getVariableManager().performStringSubstitution(expression);
	}
	
	protected boolean doPreLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) 
			throws CoreException {
		return super.preLaunchCheck(configuration, mode, monitor);
	}
	
	@Override
	protected IProject[] getProjectsForProblemSearch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		// XXX: This could perhaps be improved in the future
		return array(processLauncher.project);
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		try {
			return doBuildForLaunch(configuration, mode, monitor);
		} catch(CommonException e) {
			throw EclipseCore.createCoreException(e);
		} catch(OperationCancellation e) {
			throw new OperationCanceledException();
		}
	}
	
	protected boolean doBuildForLaunch(ILaunchConfiguration configuration, String mode,
			IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		CommonOperation buildOperation = processLauncher.buildOperation;
		if(buildOperation != null) {
			buildOperation.execute(EclipseUtils.om(pm));
			return false;
		} else {
			return super.buildForLaunch(configuration, mode, pm);
		}
	}
	
	@Override
	protected IProject[] getBuildOrder(ILaunchConfiguration configuration, String mode) throws CoreException {
		return array(processLauncher.project);
	}
	
	
	/* ----------------- Actual launch ----------------- */
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		SubMonitor subMonitor = SubMonitor.convert(monitor, 
			LaunchMessages.LCD_StartingLaunchConfiguration(configuration.getName()), 10);
		try {
			launchProcess(configuration, launch, subMonitor.split(10));
			
		} catch (CommonException ce) {
			throw EclipseCore.createCoreException(ce);
		}
	}
	
	@SuppressWarnings("unused")
	protected void launchProcess(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException, CommonException {
		processLauncher.launchProcess(launch);
	}
	
}