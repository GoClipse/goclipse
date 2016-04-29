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
import org.eclipse.debug.core.DebugPlugin;

import com.googlecode.goclipse.core.GoCoreMessages;
import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.GoBuildOutputProcessor;
import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.CheckSrcFolderRootFilesWithNoPackage;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoWorkspaceLocation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationMonitor;
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
	
	public GoBuildManager(LangBundleModel bundleModel, ToolManager toolManager) {
		super(bundleModel, toolManager);
	}
	
	public static final String BUILD_TYPE_Build = "build";
	public static final String BUILD_TYPE_BuildCheck = "check";
	public static final String BUILD_TYPE_BuildTests = "build-tests";
	public static final String BUILD_TYPE_RunTests = "[run-tests]";
	
	public static final Indexable<BuildType> BUILD_TYPES = createDefaultBuildTypes();
	public static final Indexable<String> BUILD_TYPES_Names = 
			BUILD_TYPES.map((buildType) -> buildType.getName());
	
	public static ArrayList2<BuildType> createDefaultBuildTypes() {
		return ArrayList2.create(
			new GoDefaultBuildType(BUILD_TYPE_Build),
			new GoTestBuildType(),
			new GoCheckBuildType(),
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
	protected BuildTarget createDefaultBuildTarget(IProject project, BundleInfo newBundleInfo,
			BuildConfiguration buildConfig, BuildType buildType, BuildTargetData buildTargetData) {
		if(buildType.getName().equals(BUILD_TYPE_BuildCheck)) {
			buildTargetData.autoBuildEnabled = true;
		}
		return super.createDefaultBuildTarget(project, newBundleInfo, buildConfig, buildType, buildTargetData);
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
			ArrayList2<String> buildArgs = getPackageSpecCommand(bt, getBuildCommand());
			return DebugPlugin.renderArguments(buildArgs.toArray(String.class), null);
		}
		
		protected ArrayList2<String> getPackageSpecCommand(BuildTarget bt, String... buildCommands) 
				throws CommonException {
			ArrayList2<String> buildArgs = new ArrayList2<>();
			buildArgs.addElements(buildCommands);
			
			String goPackageSpec = getGoPackageSpec(
				bt.getProject(), 
				bt.getBuildConfigName()
			);
			buildArgs.addElements("-v", "-gcflags", "-N -l", goPackageSpec);
			return buildArgs;
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
			
			Location binFolderLocation = getBinFolderLocation(bt);
			
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
		protected String[] getBuildCommand() {
			return array("build");
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) throws CommonException {
			return new GoBuildTargetOperation(buildOpParams);
		}
		
	}
	
	public static class GoCheckBuildType extends GoDefaultBuildType {
		public GoCheckBuildType() {
			super(BUILD_TYPE_BuildCheck);
		}
		
		@Override
		protected String[] getBuildCommand() {
			return array("install");
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
			sourceBaseDir = GoProjectEnvironment.getAssociatedSourceFolder(goEnv.getGoPath(), projectLoc);
			
			if(sourceBaseDir.getParent().equals(projectLoc)) {
				checkGoFilesInSourceRoot();
			}
			
			workingDirectory = sourceBaseDir;
		}
		
		@Override
		public ProcessBuilder getToolProcessBuilder() throws CommonException, OperationCancellation {
			ProcessBuilder pb = super.getToolProcessBuilder();
			goEnv.setupProcessEnv(pb, true);
			return pb;
		}
		
		protected void checkGoFilesInSourceRoot() throws CommonException {
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
		protected void processBuildOutput(ExternalProcessResult buildAllResult, IProgressMonitor pm) 
				throws CommonException, OperationCancellation {
			GoBuildOutputProcessor buildOutput = new GoBuildOutputProcessor() {
				@Override
				protected void handleParseError(CommonException ce) {
					LangCore.logError(ce.getMessage(), ce.getCause());
				}
			};
			buildOutput.parseOutput(buildAllResult);
			
			new ToolMarkersHelper().addErrorMarkers(buildOutput.getBuildErrors(), workingDirectory, pm);
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
					/* FIXME: review this possible bug for GOPATH sub-projects */
//					workingDirectory = getBinFolderLocation(bt);
					workingDirectory = GoProjectEnvironment.getBinFolderLocation(project);
				}
				
				@Override
				public void execute(IProgressMonitor pm) throws CommonException, OperationCancellation {
					Indexable<String> commandLineOriginal = getEffectiveProccessCommandLine();
					
					ProcessBuilder pb = getToolProcessBuilder();
					
					if(!isMultipleGoPackagesCommandInvocation(commandLineOriginal)) {
						runBuildToolAndProcessOutput(pb, pm);
						return;
					}
					
					ArrayList2<String> argumentsTemplate = new ArrayList2<>(commandLineOriginal);
					int lastArgIx = commandLineOriginal.size() - 1;
					String goPackageToBuild = StringUtil.trimEnd(argumentsTemplate.get(lastArgIx), "...");
					
					GoWorkspaceLocation goWorkspace = goEnv.getGoPath().findGoPathEntry(getProjectLocation());
					GoPackageName baseGoPackage = goEnv.getGoPath().findGoPackageForLocation(getProjectLocation());
					if(baseGoPackage != null) {
						goPackageToBuild = createResolvedPath(baseGoPackage.toString(), goPackageToBuild).toString(); 
					}
					Collection2<GoPackageName> sourcePackages = goWorkspace.findSubPackages(goPackageToBuild);
					
					for (GoPackageName goPackage : sourcePackages) {
						argumentsTemplate.set(lastArgIx, goPackage.getFullNameAsString());
						
						pb.command(argumentsTemplate);
						runBuildToolAndProcessOutput(pb, pm);
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
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			return null;
		}
		
		@Override
		public BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) throws CommonException {
			return new GoBuildTargetOperation(buildOpParams);
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
	
	/* -----------------  ----------------- */
	
	@Override
	protected BuildOperationCreator createBuildOperationCreator(IOperationMonitor opMonitor, IProject project) {
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