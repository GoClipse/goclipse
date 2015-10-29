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
package com.googlecode.goclipse.core.operations;

import static melnorme.lang.ide.core.utils.ResourceUtils.loc;
import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.misc.PathUtil.createResolvedPath;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.googlecode.goclipse.core.GoCoreMessages;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.CheckSrcFolderRootFilesWithNoPackage;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.ToolMarkersUtil;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildOperationCreator;
import melnorme.lang.ide.core.operations.build.CommonBuildTargetOperation;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoBuildManager extends BuildManager {
	
	public GoBuildManager(LangBundleModel bundleModel) {
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
	
	/* -----------------  ----------------- */
	
	public static abstract class AbstractGoBuildType extends BuildType {
		
		public AbstractGoBuildType(String name) {
			super(name);
		}
		
		@Override
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, ProjectBuildInfo buildInfo)
				throws CommonException {
			return new BuildConfiguration(buildConfigName, null);
		}
		
		protected GoPackageName getValidGoPackageName(String goPackageString) throws CommonException {
			return GoPackageName.createValid(goPackageString);
		}
		
		@Override
		protected void getDefaultBuildOptions(ValidatedBuildTarget vbt, ArrayList2<String> buildArgs) 
				throws CommonException {
			String goPackageSpec = getGoPackageSpec(
				vbt.getProject(), 
				vbt.getBuildConfigName());
			buildArgs.addElements(getBuildCommand());
			buildArgs.addElements("-v", "-gcflags", "-N -l", goPackageSpec);
		}
		
		protected abstract String[] getBuildCommand();
		
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
		public LaunchArtifact getMainLaunchArtifact(ValidatedBuildTarget vbt) throws CommonException {
			try {
				String buildConfigName = vbt.getBuildConfigName();
				
				Path path = PathUtil.createPath(buildConfigName);
				if(path.getNameCount() == 0 || path.endsWith("...")) {
					return null;
				}
				
			} catch(CommonException e) {
				return null;
			}
			
			Location binFolderLocation = getBinFolderLocation(vbt);
			
			String binFilePath = getBinFilePath(getValidGoPackageName(vbt.getBuildConfigName()));
			String exePath = binFolderLocation.resolve(binFilePath + MiscUtil.getExecutableSuffix()).toString();
			return new LaunchArtifact(vbt.getBuildConfigName(), exePath);
		}
		
		protected Location getBinFolderLocation(ValidatedBuildTarget validatedBuildTarget) throws CommonException {
			return GoProjectEnvironment.getBinFolderLocation(validatedBuildTarget.getProject());
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
		protected String[] getBuildCommand() {
			return array("install");
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget, 
				OperationInfo opInfo, Path buildToolPath) throws CommonException, CoreException {
			return new GoBuildTargetOperation(validatedBuildTarget, opInfo, buildToolPath);
		}
		
	}
	
	public static class GoBuildTargetOperation extends CommonBuildTargetOperation {
		
		protected final GoEnvironment goEnv;
		protected final Location sourceRootDir;
		
		public GoBuildTargetOperation(ValidatedBuildTarget validatedBuildTarget, OperationInfo opInfo, 
				Path buildToolPath) throws CommonException, CoreException {
			super(validatedBuildTarget.buildMgr, validatedBuildTarget, opInfo, buildToolPath);
			
			Location projectLocation = getProjectLocation();
			
			goEnv = getValidGoEnvironment(project);
			if(GoProjectEnvironment.isProjectInsideGoPathSourceFolder(project, goEnv.getGoPath())) {
				sourceRootDir = projectLocation;
			} else {
				sourceRootDir = projectLocation.resolve_valid("src");
				
				checkGoFilesInSourceRoot();
			}
		}
		
		@Override
		protected ProcessBuilder getProcessBuilder2(String[] toolArguments) throws CommonException {
			AbstractToolManager toolMgr = getToolManager();
			ProcessBuilder pb = toolMgr.createToolProcessBuilder(getBuildToolPath(), sourceRootDir, toolArguments);
			
			goEnv.setupProcessEnv(pb, true);
			return pb;
		}
		
		protected void checkGoFilesInSourceRoot() throws CommonException {
			CheckSrcFolderRootFilesWithNoPackage srcCheck = new CheckSrcFolderRootFilesWithNoPackage();
			
			if(!sourceRootDir.toFile().exists()) {
				throw new CommonException(GoCoreMessages.ERROR_ProjectDoesNotHaveSrcFolder(sourceRootDir.getParent()));
			}
			
			srcCheck.checkDir(sourceRootDir);
			
			if(srcCheck.containsGoSources) {
				LangCore.getToolManager().notifyMessage(StatusLevel.WARNING, "Go build: Warning!", 
					GoCoreMessages.ERROR_SrcRootContainsGoFiles(sourceRootDir));
			}
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult buildAllResult, IProgressMonitor pm) 
				throws CoreException, CommonException, OperationCancellation {
			GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
				@Override
				protected void handleMessageParseError(CommonException ce) {
					LangCore.logError(ce.getMessage(), ce.getCause());
				}
			};
			buildOutput.parseOutput(buildAllResult);
			
			new ToolMarkersUtil().addErrorMarkers(buildOutput.getBuildErrors(), sourceRootDir, pm);
		}
		
	}
	
	public static class GoTestBuildType extends AbstractGoBuildType {
		
		public GoTestBuildType() {
			super(BUILD_TYPE_BuildTests);
		}
		
		@Override
		protected String[] getBuildCommand() {
			return array("test", "-c");
		}
		
		@Override
		protected String getBinFilePath(GoPackageName goPackageName) throws CommonException {
			if(isMultipleGoPackages(goPackageName.getFullNameAsString())){
				throw new CommonException("Cannot use multiple packages spec `...` when building for a launch.");
			}
			
			return super.getBinFilePath(goPackageName) + ".test";
		}
		
		protected boolean isMultipleGoPackages(String goPackageName) {
			return goPackageName.endsWith("/...") || goPackageName.equals("...");
		}
		
		protected boolean isMultipleGoPackagesArguments(String[] arguments) {
			if(arguments.length == 0) {
				return false;
			}
			String lastArg = arguments[arguments.length-1];
			return isMultipleGoPackages(lastArg);
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget,
				OperationInfo opInfo, Path buildToolPath) throws CommonException, CoreException {
			return new GoBuildTargetOperation(validatedBuildTarget, opInfo, buildToolPath) {
				
				@Override
				protected ProcessBuilder getProcessBuilder2(String[] toolArguments) throws CommonException {
					ProcessBuilder pb = super.getProcessBuilder2(toolArguments);
					pb.directory(getBinFolderLocation(validatedBuildTarget).toFile());
					return pb;
				}
				
				@Override
				public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
					String[] argumentsOriginal = getEffectiveEvaluatedArguments();
					
					if(!isMultipleGoPackagesArguments(argumentsOriginal)) {
						runBuildToolAndProcessOutput(getToolProcessBuilder(argumentsOriginal), pm);
						return;
					}
					
					ArrayList2<String> argumentsTemplate = new ArrayList2<>(argumentsOriginal);
					int lastArgIx = argumentsOriginal.length - 1;
					String goPackageToBuild = StringUtil.trimEnd(argumentsTemplate.get(lastArgIx), "...");
					
					GoWorkspaceLocation goWorkspace = goEnv.getGoPath().findGoPathEntry(getProjectLocation());
					GoPackageName baseGoPackage = goEnv.getGoPath().findGoPackageForLocation(getProjectLocation());
					if(baseGoPackage != null) {
						goPackageToBuild = createResolvedPath(baseGoPackage.toString(), goPackageToBuild).toString(); 
					}
					Collection2<GoPackageName> sourcePackages = goWorkspace.findSubPackages(goPackageToBuild);
					
					for (GoPackageName goPackage : sourcePackages) {
						argumentsTemplate.set(lastArgIx, goPackage.getFullNameAsString());
						
						String[] arguments = argumentsTemplate.toArray(String.class);
						runBuildToolAndProcessOutput(getToolProcessBuilder(arguments), pm);
					}
					
				}
				
			};
			
		}
		
	}
	
	public static class GoRunTestsBuildType extends AbstractGoBuildType {
		
		public GoRunTestsBuildType() {
			super(BUILD_TYPE_RunTests);
		}
		
		@Override
		protected String[] getBuildCommand() {
			return array("test");
		}
		
		@Override
		public LaunchArtifact getMainLaunchArtifact(ValidatedBuildTarget vbt) throws CommonException {
			return null;
		}
		
		@Override
		public CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget, 
				OperationInfo opInfo, Path buildToolPath) throws CommonException, CoreException {
			return new GoBuildTargetOperation(validatedBuildTarget, opInfo, buildToolPath);
		}
		
	}
	
	protected static GoEnvironment getValidGoEnvironment(IProject project) throws CoreException {
		try {
			return GoProjectEnvironment.getValidatedGoEnvironment(project);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected static void addSourcePackagesToCmdLine(final IProject project, ArrayList2<String> goBuildCmdLine,
			GoEnvironment goEnvironment) throws CoreException {
		Collection2<GoPackageName> sourcePackages = GoProjectEnvironment.findSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
	}
	
	@Override
	protected BuildOperationCreator createBuildOperationCreator(OperationInfo opInfo, IProject project) {
		return new BuildOperationCreator(project, opInfo) {
			@Override
			protected void addCompositeBuildOperationMessage() throws CommonException {
				super.addCompositeBuildOperationMessage();
				
				GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
				
				if(goEnv.getGoArch() != null) {
					addOperation(newMessageOperation(opInfo, "  with GOARCH: " + goEnv.getGoArch().asString() + "\n"));
				}
				if(goEnv.getGoOs() != null) {
					addOperation(newMessageOperation(opInfo, "  with GOOS: " + goEnv.getGoOs().asString() + "\n"));
				}
				addOperation(newMessageOperation(opInfo, "  with GOPATH: " + goEnv.getGoPathString() + "\n"));
			}
		};
	}
	
}