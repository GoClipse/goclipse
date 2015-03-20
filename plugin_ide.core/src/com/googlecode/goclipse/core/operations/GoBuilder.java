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
package com.googlecode.goclipse.core.operations;

import static melnorme.lang.ide.core.utils.ResourceUtils.getProjectLocation;

import java.util.Collection;
import java.util.Map;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.LangProjectBuilder;
import melnorme.lang.ide.core.operations.LangProjectBuilderExt;
import melnorme.lang.ide.core.operations.SDKLocationValidator;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.GoProjectPrefConstants;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;


/**
 * Go builder. TODO refactor to use {@link LangProjectBuilderExt}
 */
public class GoBuilder extends LangProjectBuilder {
	
	public static class GoSDKLocationValidator extends SDKLocationValidator {
		
		public GoSDKLocationValidator() {
			super("GOROOT:");
		}
		
		@Override
		protected String getSDKExecutable_append() {
			return "bin/go"; 
		}
	}
	
	public GoBuilder() {
	}
	
	@Override
	protected LocationValidator getSDKLocationValidator() {
		return new GoSDKLocationValidator();
	}
	
	@Override
	protected String getSDKToolPath() throws CoreException {
		String pathString = GoEnvironmentPrefs.GO_ROOT.get(); // We use a different pref other than the LANG default
		try {
			return getSDKLocationValidator().getValidatedField(pathString).toPathString();
		} catch (StatusException se) {
			throw LangCore.createCoreException(se);
		}
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		return super.build(kind, args, monitor);
	}
	
	@Override
	protected void handleFirstOfKind() {
		GoToolManager.getDefault().notifyBuildStarting(null, true);
	}
	
	@Override
	protected void handleLastOfKind() {
		GoToolManager.getDefault().notifyBuildTerminated(null);
	}
	
	@Override
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException, OperationCancellation {
		
		GoToolManager.getDefault().notifyBuildStarting(project, false);
		
		GoEnvironment goEnv = getValidGoEnvironment(project);
		
		ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
		goBuildCmdLine.addElements("install", "-v");
		goBuildCmdLine.addElements(GoProjectPrefConstants.GO_BUILD_EXTRA_OPTIONS.getParsedArguments(project));
		goBuildCmdLine.addElements("./...");
//		addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		
		Location projectLocation = getProjectLocation(project);
		
		Location sourceRootDir;
		if(GoProjectEnvironment.isProjectInsideGoPath(project, goEnv.getGoPath())) {
			sourceRootDir = projectLocation;
		} else {
			sourceRootDir = projectLocation.resolve_valid("src");
		}
		
		ExternalProcessResult buildAllResult = 
			GoToolManager.getDefault().runBuildTool(goEnv, monitor, sourceRootDir, goBuildCmdLine);
		
		GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
			@Override
			protected void handleLineParseError(CommonException ce) {
				LangCore.logError(ce.getMessage(), ce.getCause());
			}
		};
		buildOutput.parseOutput(buildAllResult);
		
		addErrorMarkers(buildOutput.getBuildErrors(), sourceRootDir);
		
		return null;
	}
	
	protected GoEnvironment getValidGoEnvironment(IProject project) throws CoreException {
		try {
			return GoProjectEnvironment.getValidatedGoEnvironment(project);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected ArrayList2<String> getGoToolCommandLine() throws CoreException {
		String compilerPath = GoEnvironmentPrefs.COMPILER_PATH.get();
		
		if(compilerPath.isEmpty()) {
			throw LangCore.createCoreException("Compiler Path not defined.", null);
		}
		return new ArrayList2<>(compilerPath);
	}
	
	protected void addSourcePackagesToCmdLine(final IProject project, ArrayList2<String> goBuildCmdLine,
			GoEnvironment goEnvironment) throws CoreException {
		Collection<GoPackageName> sourcePackages = GoProjectEnvironment.getSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
	}
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		try {
			EclipseUtils.checkMonitorCancelation(monitor);
			doClean(monitor);
		} catch (OperationCancellation e) {
			// return
		}
	}
	
	protected void doClean(IProgressMonitor monitor) throws CoreException, OperationCancellation {
		deleteProjectBuildMarkers();
		
		IProject project = getProject();
		GoEnvironment goEnv = getValidGoEnvironment(project);
		
		ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
		goBuildCmdLine.addElements("clean", "-i", "-x");
		addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		
		GoToolManager.getDefault().runBuildTool(goEnv, monitor, getProjectLocation(project), goBuildCmdLine);
	}
	
}