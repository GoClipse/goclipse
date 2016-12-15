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
import static melnorme.utilbox.core.CoreUtil.list;

import java.nio.file.Path;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;

import com.googlecode.goclipse.core.GoCoreMessages;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.CheckSrcFolderRootFilesWithNoPackage;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.ToolMarkersHelper;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildOperationCreator;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation.BuildOperationParameters;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.bundle.BuildTargetNameParser2;
import melnorme.lang.tooling.bundle.BundleInfo;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
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
import melnorme.utilbox.status.StatusLevel;

public class GoBuildManager extends BuildManager {
	
	public GoBuildManager(LangBundleModel bundleModel, ToolManager toolManager) {
		super(bundleModel, toolManager);
	}
	
	public static final String BUILD_TYPE_Build = "build";
	public static final String BUILD_TYPE_BuildTests = "build-tests";
	public static final String BUILD_TYPE_Lint = "lint";
	public static final String BUILD_TYPE_RunTests = "[run-tests]";
	
	public static final Indexable<BuildType> BUILD_TYPES = createDefaultBuildTypes();
	public static final Indexable<String> BUILD_TYPES_Names = 
			BUILD_TYPES.map((buildType) -> buildType.getName());
	
	public static ArrayList2<BuildType> createDefaultBuildTypes() {
		return ArrayList2.create(
			new GoDefaultBuildType(BUILD_TYPE_Build),
			new GoTestBuildType(),
			new GoLintBuildType(),
			new GoRunTestsBuildType()
		);
	}
	
	@Override
	protected Indexable<BuildType> getBuildTypes_do() {
		return BUILD_TYPES;
	}
	
	@Override
	public BuildTargetNameParser getBuildTargetNameParser() {
		return new BuildTargetNameParser2();
	}
	
	@Override
	protected void addDefaultBuildTarget(ArrayList2<BuildTarget> buildTargets, IProject project, BundleInfo bundleInfo,
			BuildConfiguration buildConfig, BuildType buildType, BuildTargetData btd) {
//		if(buildType.getName().equals(BUILD_TYPE_BuildCheck)) {
//			btd.autoBuildEnabled = true;
//		}
		super.addDefaultBuildTarget(buildTargets, project, bundleInfo, buildConfig, buildType, btd);
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class AbstractGoBuildType extends BuildType {
		
		public AbstractGoBuildType(String name) {
			super(name);
		}
		
		@Override
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, BundleInfo bundleInfo)
				throws CommonException {
			return new BuildConfiguration(buildConfigName, null);
		}
		
		protected GoPackageName getValidGoPackageName(String goPackageString) throws CommonException {
			return GoPackageName.createValid(goPackageString);
		}
		
		@Override
		public String getDefaultCommandArguments(BuildTarget bt) throws CommonException {
			ArrayList2<String> buildArgs = getDefaultCommandArguments_list(bt);
			return DebugPlugin.renderArguments(buildArgs.toArray(String.class), null);
		}
		
		protected ArrayList2<String> getDefaultCommandArguments_list(BuildTarget bt) throws CommonException {
			Indexable<String> baseCommand = getBuildCommand2();
			ArrayList2<String> commandLine = baseCommand.toArrayList().addElements("-v", "-gcflags", "-N -l");
			addPackageSpecCommand(bt, commandLine);
			return commandLine;
		}
		
		protected void addPackageSpecCommand(BuildTarget bt, ArrayList2<String> buildCommand) 
				throws CommonException {
			String goPackageSpec = getGoPackageSpec(
				bt.getProject(), 
				bt.getBuildConfigName()
			);
			buildCommand.addElements(goPackageSpec);
		}
		
		protected abstract Indexable<String> getBuildCommand2();
		
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
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			try {
				String buildConfigName = bt.getBuildConfigName();
				
				Path path = PathUtil.createPath(buildConfigName);
				if(path.getNameCount() == 0 || path.endsWith("...")) {
					return null;
				}
				
			} catch(CommonException e) {
				return null;
			}
			
			Location binFolderLocation = GoProjectEnvironment.getBinFolderLocation(bt.getProject());
			
			String binFilePath = getBinFilePath(getValidGoPackageName(bt.getBuildConfigName()));
			String exePath = binFolderLocation.resolve(binFilePath + MiscUtil.getExecutableSuffix()).toString();
			return new LaunchArtifact(bt.getBuildConfigName(), exePath);
		}
		
		protected Location getBinFolderLocation(BuildTarget buildTarget) throws CommonException {
			return GoProjectEnvironment.getBinFolderLocation(buildTarget.getProject());
		}
		
		protected String getBinFilePath(GoPackageName goPackageName) throws CommonException {
			return goPackageName.getLastSegment();
		}
		
	}
	
	public static class GoDefaultBuildType extends AbstractGoBuildType {
		
		public GoDefaultBuildType(String name) {
			super(name);
		}
		
		@Override
		protected Indexable<String> getBuildCommand2() {
			return list("install");
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) throws CommonException {
			return new GoBuildTargetOperation(buildOpParams);
		}
		
	}
	
	public static class GoLintBuildType extends GoDefaultBuildType {
		public GoLintBuildType() {
			super(BUILD_TYPE_Lint);
		}
		
		@Override
		public String getDefaultCommandLine(BuildTarget bt) throws CommonException {
			return getDefaultCommandArguments(bt);
		}

		@Override
		protected ArrayList2<String> getDefaultCommandArguments_list(BuildTarget bt) throws CommonException {
			ArrayList2<String> commandLine = ArrayList2.create("gometalinter", "-t");
			addPackageSpecCommand(bt, commandLine);
			return commandLine;
		}
	}
	
	public static class GoBuildTargetOperation extends BuildTargetOperation {
		
		protected final GoEnvironment goEnv;
		protected final Location sourceBaseDir;
		protected Location workingDirectory;
		
		public GoBuildTargetOperation(BuildOperationParameters buildOpParams) throws CommonException {
			super(buildOpParams);
			
			Location projectLoc = getProjectLocation();
			
			goEnv = GoProjectEnvironment.getValidatedGoEnvironment(project);
			sourceBaseDir = goEnv.getGoPath().getSourceRootforLocation(projectLoc);	
			if(sourceBaseDir == null) {
				throw new CommonException(GoCoreMessages.ERROR_GOPATH_DoesNotContainProject());
			}
			
			if(sourceBaseDir.getParent().equals(projectLoc)) {
				checkForGoFilesInSourceRoot(sourceBaseDir);
			}
			
			workingDirectory = sourceBaseDir;
		}
		
		@Override
		public ProcessBuilder getToolProcessBuilder() throws CommonException, OperationCancellation {
			ProcessBuilder pb = super.getToolProcessBuilder();
			if(workingDirectory != null) {
				pb.directory(workingDirectory.toFile());
			}
			goEnv.setupProcessEnv(pb, true);
			return pb;
		}
		
		protected void checkForGoFilesInSourceRoot(Location sourceBaseDir) throws CommonException {
			CheckSrcFolderRootFilesWithNoPackage srcCheck = new CheckSrcFolderRootFilesWithNoPackage();
			
			if(!sourceBaseDir.toFile().exists()) {
				throw new CommonException(GoCoreMessages.ERROR_ProjectDoesNotHaveSrcFolder(sourceBaseDir.getParent()));
			}
			
			srcCheck.checkDir(sourceBaseDir);
			
			if(srcCheck.containsGoSources) {
				LangCore.getToolManager().notifyMessage(StatusLevel.WARNING, "Go build: Warning!", 
					GoCoreMessages.ERROR_SrcRootContainsGoFiles(sourceBaseDir));
			}
		}
		
		@Override
		protected void processBuildOutput(ExternalProcessResult buildAllResult, IOperationMonitor om) 
				throws CommonException, OperationCancellation {
			GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
				@Override
				protected void handleParseError(CommonException ce) {
					LangCore.logError(ce.getMessage(), ce.getCause());
				}
			};
			ArrayList<ToolSourceMessage> buildMessages = buildOutput.doParseResult(buildAllResult);
			
			new ToolMarkersHelper(true).addErrorMarkers(buildMessages, workingDirectory, om);
		}
		
	}
	
	public static class GoTestBuildType extends AbstractGoBuildType {
		
		public GoTestBuildType() {
			super(BUILD_TYPE_BuildTests);
		}
		
		@Override
		protected Indexable<String> getBuildCommand2() {
			return list("test", "-c");
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
		
		protected boolean isMultipleGoPackagesCommandInvocation(Indexable<String> commandLine) {
			if(commandLine.size() <= 1) {
				return false;
			}
			String lastArg = commandLine.get(commandLine.size()-1);
			return isMultipleGoPackages(lastArg);
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) throws CommonException {
			return new GoBuildTargetOperation(buildOpParams) {
				{
					// We need to change working directory to bin, 
					// because our commands create executable files in the working directory.
					workingDirectory = GoProjectEnvironment.getBinFolderLocation(project);
				}
				
				@Override
				public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
					Indexable<String> commandLineOriginal = new ArrayList2<>(getEffectiveProccessCommandLine());
					
					ProcessBuilder pb = getToolProcessBuilder();
					
					if(!isMultipleGoPackagesCommandInvocation(commandLineOriginal)) {
						runBuildToolAndProcessOutput(pb, om);
						return;
					}
					
					ArrayList2<String> argumentsTemplate = new ArrayList2<>(commandLineOriginal);
					int lastArgIx = commandLineOriginal.size() - 1;
					String goPackageToBuild = StringUtil.trimEnd(argumentsTemplate.get(lastArgIx), "...");
					
					GoWorkspaceLocation goWorkspace = goEnv.getGoPath().findGoPathEntry(getProjectLocation());
					Collection2<GoPackageName> sourcePackages = goWorkspace.findSubPackages(goPackageToBuild);
					
					for (GoPackageName goPackage : sourcePackages) {
						argumentsTemplate.set(lastArgIx, goPackage.getFullNameAsString());
						
						pb.command(argumentsTemplate);
						runBuildToolAndProcessOutput(pb, om);
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
		protected Indexable<String> getBuildCommand2() {
			return list("test");
		}
		
		@Override
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			return null;
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) throws CommonException {
			return new GoBuildTargetOperation(buildOpParams);
		}
		
	}
	
	protected static GoEnvironment getValidGoEnvironment(IProject project) throws CommonException {
		return GoProjectEnvironment.getValidatedGoEnvironment(project);
	}
	
	protected static void addSourcePackagesToCmdLine(final IProject project, ArrayList2<String> goBuildCmdLine,
			GoEnvironment goEnvironment) throws CommonException {
		Collection2<GoPackageName> sourcePackages = GoProjectEnvironment.findSourcePackages(project, goEnvironment);
		for (GoPackageName goPackageName : sourcePackages) {
			goBuildCmdLine.add(goPackageName.getFullNameAsString());
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected BuildOperationCreator createBuildOperationCreator(
		IToolOperationMonitor opMonitor, IProject project
	) throws CommonException {
		return new BuildOperationCreator(project, opMonitor) {
			@Override
			protected void addCompositeBuildOperationMessage() throws CommonException {
				super.addCompositeBuildOperationMessage();
				
				GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(project);
				
				addOperation(newMessageOperation("  with GOPATH: " + goEnv.getGoPathString() + "\n"));
			}
		};
	}
	
}