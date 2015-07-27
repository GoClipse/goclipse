/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.operations;

import static melnorme.lang.ide.core.utils.ResourceUtils.loc;

import java.nio.file.Path;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import com.googlecode.goclipse.core.GoCoreMessages;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolMarkersUtil;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetRunner;
import melnorme.lang.ide.core.operations.build.BuildTargetRunner.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildTargetRunner.BuildType;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoBuildManager extends BuildManager {
	
	public GoBuildManager(LangBundleModel<? extends AbstractBundleInfo> bundleModel) {
		super(bundleModel);
	}
	
	public static final String BUILD_TYPE_Build = "build";
	public static final String BUILD_TYPE_BuildTests = "build-tests";
	public static final String BUILD_TYPE_RunTests = "[run-tests]";
	
	public static final Indexable<BuildType> BUILD_TYPES = createDefaultBuildTypes();
	public static final Indexable<String> BUILD_TYPES_Names = 
			BUILD_TYPES.map((buildType) -> buildType.getName());
	
	public static ArrayList2<BuildType> createDefaultBuildTypes() {
		return ArrayList2.create(
			new GoDefaultBuildType(),
			new GoTestBuildType(),
			new GoRunTestsBuildType()
		);
	}
	
	@Override
	protected Indexable<BuildType> getBuildTypes_do() {
		return BUILD_TYPES;
	}
	
	@Override
	public BuildTarget getBuildTargetFor(ProjectBuildInfo projectBuildInfo, String targetName) 
			throws CommonException {
		BuildTarget buildTarget = super.getBuildTargetFor(projectBuildInfo, targetName);
		if(buildTarget != null) {
			return buildTarget;
		}
		
		return createBuildTarget(targetName, false, null);
	}
	
	@Override
	public BuildTargetRunner getBuildTargetOperation(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		String targetName = buildTarget.getTargetName();
		String buildConfigName = getBuildConfigString(targetName);
		String buildTypeName = getBuildTypeString(targetName);
		
		/* FIXME: adapt this code in parent */
		BuildConfiguration buildConfiguration = new BuildConfiguration(buildConfigName, null);
		
		return createBuildTargetOperation(project, buildConfiguration, buildTypeName, buildTarget);
	}
	
	@Override
	public BuildTargetRunner createBuildTargetOperation(IProject project, BuildConfiguration buildConfig,
			String buildTypeName, BuildTarget buildSettings) {
		return new BuildTargetRunner(project, buildConfig, buildTypeName, buildSettings.getBuildOptions()) {
			
			@Override
			public CommonBuildTargetOperation getBuildOperation(OperationInfo opInfo, Path buildToolPath,
					boolean fullBuild) {
				return new GoBuildTargetOperation(opInfo, project, buildToolPath, this, fullBuild);
			}
		};
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class AbstractGoBuildType extends BuildType {
		
		public AbstractGoBuildType(String name) {
			super(name);
		}
		
		protected GoPackageName getValidGoPackageName(String goPackageString) throws CommonException {
			return GoPackageName.createValid(goPackageString);
		}
		
		@Override
		public String getDefaultBuildOptions(BuildTargetRunner buildTargetOp) throws CommonException {
			String goPackageSpec = getGoPackageSpec(buildTargetOp.getProject(), 
				buildTargetOp.getBuildConfigName());
			return getBuildCommand() + " -v -gcflags \"-N -l\" " + goPackageSpec;
		}
		
		protected abstract String getBuildCommand();
		
		protected String getGoPackageSpec(IProject project, String goPackageSpec) throws CommonException {
			
			if(goPackageSpec == null || goPackageSpec.isEmpty()) {
				GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
				GoPackageName goPackage = goEnv.getGoPath().findGoPackageForLocation(loc(project.getLocation()));
				
				if(goPackage != null) {
					goPackageSpec = goPackage.toString();
				} else {
					goPackageSpec = ".";
				}
				
				goPackageSpec += "/...";
			}
			return goPackageSpec;
		}
		
		@Override
		public String getArtifactPath(BuildTargetRunner buildTargetOp) throws CommonException {
			Location binFolderLocation = GoProjectEnvironment.getBinFolderLocation(buildTargetOp.getProject());
			
			String binFilePath = getBinFilePath(getValidGoPackageName(buildTargetOp.getBuildConfigName()));
			return binFolderLocation.resolve(binFilePath + MiscUtil.getExecutableSuffix()).toString();
		}
		
		protected String getBinFilePath(GoPackageName goPackageName) throws CommonException {
			return goPackageName.getLastSegment();
		}
		
	}
	
	public static class GoDefaultBuildType extends AbstractGoBuildType {
		
		public GoDefaultBuildType() {
			super(BUILD_TYPE_Build);
		}
		
		@Override
		protected String getBuildCommand() {
			return "install";
		}
		
	}
	
	public static class GoTestBuildType extends AbstractGoBuildType {
		
		public GoTestBuildType() {
			super(BUILD_TYPE_BuildTests);
		}
		
		@Override
		protected String getBuildCommand() {
			return "test -c";
		}
		
		@Override
		protected String getBinFilePath(GoPackageName goPackageName) throws CommonException {
			return super.getBinFilePath(goPackageName) + ".test";
		}
		
	}
	
	public static class GoRunTestsBuildType extends AbstractGoBuildType {
		
		public GoRunTestsBuildType() {
			super(BUILD_TYPE_RunTests);
		}
		
		@Override
		protected String getBuildCommand() {
			return "test";
		}
		
		@Override
		public String getArtifactPath(BuildTargetRunner buildTargetOp) throws CommonException {
			throw new CommonException("This configuration does not produce executable artifacts.");
		}
		
	}
	
	public static class GoBuildTargetOperation extends CommonBuildTargetOperation {
		
		public GoBuildTargetOperation(OperationInfo opInfo, IProject project, 
				Path buildToolPath, BuildTargetRunner buildTargetOp, boolean fullBuild) {
			super(buildTargetOp.getBuildManager(), opInfo, project, buildToolPath, buildTargetOp, fullBuild);
		}
		
		protected GoEnvironment goEnv;
		protected Location sourceRootDir;
		
		@Override
		protected void addMainArguments(ArrayList2<String> commands) {
		}
		
		@Override
		protected ProcessBuilder getProcessBuilder(ArrayList2<String> commands) 
				throws CoreException, CommonException {
			Location projectLocation = ResourceUtils.getProjectLocation(project);
			
			goEnv = getValidGoEnvironment(project);
			if(GoProjectEnvironment.isProjectInsideGoPath(project, goEnv.getGoPath())) {
				sourceRootDir = projectLocation;
			} else {
				sourceRootDir = projectLocation.resolve_valid("src");
				
				checkGoFilesInSourceRoot();
			}
			ProcessBuilder pb = ProcessUtils.createProcessBuilder(commands, sourceRootDir);
			goEnv.setupProcessEnv(pb, true);
			return pb;
		}
		
		protected void checkGoFilesInSourceRoot() throws CoreException {
			CheckSrcFolderRootFilesWithNoPackage srcCheck = new CheckSrcFolderRootFilesWithNoPackage();
			
			srcCheck.checkDir(sourceRootDir);
			
			if(srcCheck.containsGoSources) {
				LangCore.getToolManager().notifyMessage(StatusLevel.WARNING, "Go build: Warning!", 
					GoCoreMessages.ERROR_SrcRootContainsGoFiles(sourceRootDir));
			}
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult buildAllResult) 
				throws CoreException, CommonException, OperationCancellation {
			GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
				@Override
				protected void handleMessageParseError(CommonException ce) {
					LangCore.logError(ce.getMessage(), ce.getCause());
				}
			};
			buildOutput.parseOutput(buildAllResult);
			
			ToolMarkersUtil.addErrorMarkers(buildOutput.getBuildErrors(), sourceRootDir);
		}
		
	}
	
	protected static GoEnvironment getValidGoEnvironment(IProject project) throws CoreException {
		try {
			return GoProjectEnvironment.getValidatedGoEnvironment(project);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected static ArrayList2<String> getToolCommandLine() throws CoreException {
		String compilerPath = GoEnvironmentPrefs.COMPILER_PATH.get();
		
		if(compilerPath.isEmpty()) {
			throw LangCore.createCoreException("Compiler Path not defined.", null);
		}
		return new ArrayList2<>(compilerPath);
	}
	
	protected static void addSourcePackagesToCmdLine(final IProject project, ArrayList2<String> goBuildCmdLine,
			GoEnvironment goEnvironment) throws CoreException {
		Collection<GoPackageName> sourcePackages = GoProjectEnvironment.getSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
	}
	
}