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

import java.text.MessageFormat;
import java.util.Collection;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.LangProjectBuilderExt;
import melnorme.lang.tooling.data.LocationValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
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
import com.googlecode.goclipse.tooling.GoSDKLocationValidator;
import com.googlecode.goclipse.tooling.env.GoEnvironment;


/**
 * Go builder. TODO refactor to use {@link LangProjectBuilderExt}
 */
public class GoBuilder extends LangProjectBuilderExt {
	
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
	protected AbstractRunBuildOperation createBuildOp() {
		return new GoRunBuildOperation();
	}
	
	protected class GoRunBuildOperation extends AbstractRunBuildOperation {
		
		protected static final String ERROR_SrcRootContainsGoFiles = 
				"The Go `src` directory at `{0}` contains .go files. " +
				"This is not allowed, these files will be ignored.\n" + 
				"Instead, all .go files should be in a subdirectory of `src`, " + 
				"so that they will be part of a Go package. " + 
				"This is so they can be built using the `./...` pattern, or imported by other Go files.";
		
		protected GoEnvironment goEnv;
		protected Location sourceRootDir;
		
		
		@Override
		public IProject[] execute(IProject project, IProgressMonitor monitor) throws CoreException,
				OperationCancellation {
			
			Location projectLocation = getProjectLocation();
			
			goEnv = getValidGoEnvironment(project);
			if(GoProjectEnvironment.isProjectInsideGoPath(project, goEnv.getGoPath())) {
				sourceRootDir = projectLocation;
			} else {
				sourceRootDir = projectLocation.resolve_valid("src");
				
				checkGoFilesInSourceRoot();
			}
			
			return super.execute(project, monitor);
		}
		
		protected void checkGoFilesInSourceRoot() throws CoreException {
			CheckSrcFolderRootFilesWithNoPackage srcCheck = new CheckSrcFolderRootFilesWithNoPackage();
			
			srcCheck.checkDir(sourceRootDir);
			
			if(srcCheck.containsGoSources) {
				getToolManager().notifyMessage(StatusLevel.WARNING, "Go build: Warning!", 
					MessageFormat.format(ERROR_SrcRootContainsGoFiles, sourceRootDir));
			}
		}
		
		@Override
		protected ProcessBuilder createBuildPB() throws CoreException, CommonException {
			IProject project = getProject();
			
			ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
			goBuildCmdLine.addElements();
			goBuildCmdLine.addElements(GoProjectPrefConstants.GO_BUILD_OPTIONS.getParsedArguments(project));
			
			return goEnv.createProcessBuilder(goBuildCmdLine, sourceRootDir);
		}
		
		@Override
		protected void doBuild_processBuildResult(ExternalProcessResult buildAllResult) 
				throws CoreException, CommonException {
			GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
				@Override
				protected void handleLineParseError(CommonException ce) {
					LangCore.logError(ce.getMessage(), ce.getCause());
				}
			};
			buildOutput.parseOutput(buildAllResult);
			
			addErrorMarkers(buildOutput.getBuildErrors(), sourceRootDir);
		}
		
	}
	
	protected static GoEnvironment getValidGoEnvironment(IProject project) throws CoreException {
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
	
	/* -----------------  ----------------- */
	
	@Override
	protected ProcessBuilder createCleanPB() throws CoreException, CommonException {
		IProject project = getProject();
		GoEnvironment goEnv = getValidGoEnvironment(project);
		
		ArrayList2<String> goBuildCmdLine = getGoToolCommandLine();
		goBuildCmdLine.addElements("clean", "-i", "-x");
		addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		return goEnv.createProcessBuilder(goBuildCmdLine, getProjectLocation());
	}
	
}