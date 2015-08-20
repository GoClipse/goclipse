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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.misc.StringUtil;


public abstract class BuildManager {
	
	public static final SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	public static BuildManager getInstance() {
		return LangCore.getBuildManager();
	}
	
	/* -----------------  ----------------- */
	
	protected final BuildModel buildModel;
	protected final LangBundleModel bundleModel;
	
	public BuildManager(LangBundleModel bundleModel) {
		this(new BuildModel(), bundleModel);
	}
	
	public BuildManager(BuildModel buildModel, LangBundleModel bundleModel) {
		this.buildModel = buildModel;
		this.bundleModel = bundleModel;
		synchronized (init_Lock) { 
			HashMap<String, BundleInfo> projectInfos = bundleModel.connectListener(listener);
			
			for(Entry<String, BundleInfo> entry : projectInfos.entrySet()) {
				IProject project = ResourceUtils.getProject(entry.getKey());
				BundleInfo bundleInfo = entry.getValue();
				bundleProjectAdded(project, bundleInfo);
			}
		}
	}
	
	protected Object init_Lock = new Object();
	
	protected final IProjectModelListener<BundleInfo> listener = new IProjectModelListener<BundleInfo>() {
		
		@Override
		public void notifyUpdateEvent(UpdateEvent<BundleInfo> updateEvent) {
			synchronized (init_Lock) {
				if(updateEvent.newProjectInfo2 != null) {
					bundleProjectAdded(updateEvent.project, updateEvent.newProjectInfo2);
				} else {
					bundleProjectRemoved(updateEvent.project);
				}
			}
		}
	};
	
	public void dispose() {
		bundleModel.removeListener(listener);
	}
	
	public BuildModel getBuildModel() {
		return buildModel;
	}
	
	public static class BuildModel extends ProjectBasedModel<ProjectBuildInfo> {
		
		public BuildModel() {
		}
		
		@Override
		protected SimpleLogger getLog() {
			return BuildManager.log;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public ProjectBuildInfo getBuildInfo(IProject project) {
		return buildModel.getProjectInfo(assertNotNull(project));
	}
	
	public ProjectBuildInfo getValidBuildInfo(IProject project) throws CommonException {
		return getValidBuildInfo(project, true);
	}
	
	public ProjectBuildInfo getValidBuildInfo(IProject project, boolean requireNonEmtpyTargets) 
			throws CommonException {
		new ProjectValidator().checkProjectNotNull(project); 
		ProjectBuildInfo buildInfo = getBuildInfo(project);
		
		if(buildInfo == null || (requireNonEmtpyTargets && buildInfo.getBuildTargets().isEmpty())) {
			throw new CommonException("No build targets available for project.");
		}
		return buildInfo;
	}
	
	/* -----------------  Persistence preference ----------------- */
	
	protected static final StringPreference BUILD_TARGETS_PREF = new StringPreference("build_targets", "");
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer(this);
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_PREF.get(project));
	}
	
	/* ----------------- ProjectBuildInfo ----------------- */
	
	protected void bundleProjectAdded(IProject project, BundleInfo newBundleInfo) {
		loadProjectBuildInfo(project, newBundleInfo);
	}
	
	protected void bundleProjectRemoved(IProject project) {
		buildModel.removeProjectInfo(project);
	}
	
	protected void loadProjectBuildInfo(IProject project, BundleInfo newBundleInfo) {
		assertNotNull(newBundleInfo);
		
		ProjectBuildInfo currentBuildInfo = buildModel.getProjectInfo(project);
		
		if(currentBuildInfo == null) {
			String targetsPrefValue = getBuildTargetsPref(project);
			if(targetsPrefValue != null) {
				try {
					ArrayList2<BuildTarget> buildTargets = createSerializer().readProjectBuildInfo(targetsPrefValue);
					currentBuildInfo = new ProjectBuildInfo(this, project, newBundleInfo, buildTargets);
				} catch(CommonException ce) {
					LangCore.logError(ce);
				}
			}
		}
		
		// Create new build info
		ArrayList2<BuildTarget> buildTargets = createBuildTargetsForNewInfo(newBundleInfo, currentBuildInfo);
		ProjectBuildInfo newBuildInfo = new ProjectBuildInfo(this, project, newBundleInfo, buildTargets);
		setProjectBuildInfo(project, newBuildInfo);
	}
	
	protected ArrayList2<BuildTarget> createBuildTargetsForNewInfo(BundleInfo newBundleInfo, 
			ProjectBuildInfo currentBuildInfo) {
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		boolean isFirstConfig = true;
		
		Indexable<BuildConfiguration> buildConfigs = newBundleInfo.getBuildConfigurations();
		for(BuildConfiguration buildConfig : buildConfigs) {
			
			Indexable<BuildType> buildTypes = getBuildTypes();
			for(BuildType buildType : buildTypes) {
				
				String targetName = getBuildTargetName2(buildConfig.getName(), buildType.getName()); 
				
				buildTargets.add(createBuildTargetFromConfig(currentBuildInfo, isFirstConfig, targetName));
				isFirstConfig = false;
			}
			
		}
		return buildTargets;
	}
	
	protected BuildTarget createBuildTargetFromConfig(ProjectBuildInfo currentBuildInfo, boolean isEnabled,
			String targetName) {
		BuildTarget oldBuildTarget = currentBuildInfo == null ? 
				null : 
				currentBuildInfo.getDefinedBuildTarget(targetName);
		
		BuildTargetData data = new BuildTargetData(targetName, isEnabled, null, null);
		
		if(oldBuildTarget != null) {
			data.enabled = oldBuildTarget.isEnabled();
			data.buildArguments = oldBuildTarget.getBuildArguments();
			data.executablePath = oldBuildTarget.getExecutablePath();
		}
		
		return createBuildTarget(data);
	}
	
	public ProjectBuildInfo setProjectBuildInfo(IProject project, ProjectBuildInfo newProjectBuildInfo) {
		return buildModel.setProjectInfo(project, newProjectBuildInfo);
	}
	
	public ProjectBuildInfo setAndSaveProjectBuildInfo(IProject project, ProjectBuildInfo newProjectBuildInfo) {
		buildModel.setProjectInfo(project, newProjectBuildInfo);
		
		try {
			String data = createSerializer().writeProjectBuildInfo(newProjectBuildInfo);
			BUILD_TARGETS_PREF.set(project, data);
		} catch(CommonException | BackingStoreException e) {
			LangCore.logError("Error persisting project build info: ", e);
		}
		
		return newProjectBuildInfo;
	}
	
	/* ----------------- Build Types ----------------- */
	
	public static abstract class BuildType {
		
		protected final String name;
		
		public BuildType(String name) {
			this.name = assertNotNull(name);
		}
		
		/* -----------------  ----------------- */
		
		public String getName() {
			return name;
		}
		
		public ValidatedBuildTarget getValidatedBuildTarget(IProject project, BuildTarget buildTarget,
				String buildConfigName) throws CommonException {
			return new ValidatedBuildTarget(project, buildTarget, this, buildConfigName);
		}
		
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, ProjectBuildInfo buildInfo)
				throws CommonException {
			return buildInfo.getBuildConfiguration_nonNull(buildConfigName);
		}
		
		public String getDefaultBuildOptions(ValidatedBuildTarget validatedBuildTarget) 
				throws CommonException {
			ArrayList2<String> arguments = new ArrayList2<>();
			getDefaultBuildOptions(validatedBuildTarget, arguments);
			return DebugPlugin.renderArguments(arguments.toArray(String.class), null);
		}
		
		protected abstract void getDefaultBuildOptions(ValidatedBuildTarget vbt, ArrayList2<String> buildArgs) 
				throws CommonException;
		
		public final Indexable<BuildConfiguration> getSubConfigurations(ValidatedBuildTarget vbt) 
				throws CommonException {
			Indexable<BuildConfiguration> subConfigurations = getSubConfigurations_do(vbt);
			assertTrue(subConfigurations.size() >= 1);
			return subConfigurations;
		}
		
		public Indexable<BuildConfiguration> getSubConfigurations_do(ValidatedBuildTarget vbt) throws CommonException {
			return new ArrayList2<>(vbt.getBuildConfiguration());
		}
		
		public abstract CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget,
				OperationInfo opInfo, Path buildToolPath)
				throws CommonException, CoreException;
		
	}
	
	protected final Indexable<BuildType> getBuildTypes() {
		Indexable<BuildType> buildTypes = getBuildTypes_do();
		assertTrue(buildTypes.size() > 0);
		return buildTypes;
	}
	
	protected abstract Indexable<BuildType> getBuildTypes_do();
	
	public BuildType getBuildType_NonNull(String buildTypeName) throws CommonException {
		if(buildTypeName == null) {
			return getBuildTypes().get(0);
		}
		
		for(BuildType buildType : getBuildTypes()) {
			if(areEqual(buildType.getName(), buildTypeName)) {
				return buildType;
			}
		}
		throw new CommonException(BuildManagerMessages.BuildType_NotFound(buildTypeName));
	}
	
	/* ----------------- Build Configuration ----------------- */
	
	public static class BuildConfiguration {
		
		protected final String name;
		protected final String artifactPath;
		
		public BuildConfiguration(String name, String artifactPath) {
			this.name = assertNotNull(name);
			this.artifactPath = artifactPath;
		}
		
		public String getName() {
			return name;
		}
		
		/* FIXME: remove exception */
		public String getArtifactPath() throws CommonException {
			return artifactPath;
		}
		
	}
	
	/* ----------------- Build Target name ----------------- */
	
	public BuildTargetNameParser getBuildTargetNameParser() {
		return new BuildTargetNameParser();
	}
	
	public String getDefaultBuildTypeName() {
		return getBuildTypes().get(0).getName();
	}
	
	public String getBuildTargetName2(String buildConfigName, String buildTypeName) {
		assertNotNull(buildConfigName);
		return getBuildTargetNameParser().getFullName(buildConfigName, buildTypeName);
	}
	
	public String getResolvedBuildTargetName(String buildTargetName) {
		BuildTargetNameParser nameParser = getBuildTargetNameParser();
		
		String buildConfig = nameParser.getBuildConfig(buildTargetName);
		String buildType = nameParser.getBuildType(buildTargetName);
		if(buildType == null) {
			buildType = getDefaultBuildTypeName();
		}
		
		return nameParser.getFullName(buildConfig, buildType);
	}
	
	/* -----------------  Build Target Validator  ----------------- */
	
	public ValidatedBuildTarget getValidatedBuildTarget(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		
		String targetName = buildTarget.getTargetName();
		BuildTargetNameParser nameParser = getBuildTargetNameParser();
		String buildConfigName = nameParser.getBuildConfig(targetName);
		
		BuildType buildType = getBuildType_NonNull(nameParser.getBuildType(targetName));
		
		return buildType.getValidatedBuildTarget(project, buildTarget, buildConfigName);
	}
	
	public BuildTarget getBuildTargetFor2(ProjectBuildInfo projectBuildInfo, String targetName)
			throws CommonException {
		BuildTarget buildTarget = projectBuildInfo.getDefinedBuildTarget(targetName);
		if(buildTarget != null) {
			return buildTarget;
		}
		
		return createBuildTarget(new BuildTargetData(targetName, false, null, null));
	}
	
	/* -----------------  Build Target  ----------------- */
	
	public BuildTarget createBuildTarget(BuildTargetData buildTargetData) {
		return new BuildTarget(buildTargetData);
	}
	
	public BuildTarget getValidDefinedBuildTarget(IProject project, String buildTargetName) 
			throws CommonException, StatusException {
		return getValidBuildTarget(project, buildTargetName, true, true);
	}
	
	public BuildTarget getValidBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly) 
			throws CommonException, StatusException {
		return getValidBuildTarget(project, buildTargetName, definedTargetsOnly, true);
	}
	
	public BuildTarget getValidBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly, 
			boolean requireNonNull) throws CommonException {
		ProjectBuildInfo buildInfo = getValidBuildInfo(project, false);
		
		if(buildTargetName == null || buildTargetName.isEmpty()) {
			throw new CommonException(LaunchMessages.PROCESS_LAUNCH_NoBuildTargetSpecified);
		}
		
		BuildTarget buildTarget = 
				definedTargetsOnly ? 
				buildInfo.getDefinedBuildTarget(buildTargetName) :
				buildInfo.getBuildTargetFor(buildTargetName);
		
		if(buildTarget == null && requireNonNull) {
			throw new CommonException(LaunchMessages.PROCESS_LAUNCH_NoSuchBuildTarget);
		}
		return buildTarget;
	}
	
	/* ----------------- Build operations ----------------- */
	
	protected BuildOperationCreator createBuildOperationCreator(OperationInfo opInfo, IProject project) {
		return new BuildOperationCreator(project, opInfo);
	}
	
	public IToolOperation newProjectClearMarkersOperation(OperationInfo opInfo, IProject project) {
		return createBuildOperationCreator(opInfo, project).newClearBuildMarkersOperation();
	}
	
	public final IToolOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget)
			throws CommonException {
		return newBuildTargetsOperation(project, ArrayList2.create(buildTarget));
	}
	
	public IToolOperation newBuildTargetsOperation(IProject project, Collection2<BuildTarget> targetsToBuild)
			throws CommonException {
		OperationInfo operationInfo = LangCore.getToolManager().startNewToolOperation();
		return newBuildOperation(operationInfo, project, true, targetsToBuild);
	}
	
	public final IToolOperation newProjectBuildOperation(OperationInfo opInfo, IProject project,
			boolean clearMarkers) throws CommonException {
		ArrayList2<BuildTarget> enabledTargets = getValidBuildInfo(project).getEnabledTargets();
		return newBuildOperation(opInfo, project, clearMarkers, enabledTargets);
	}
	
	public IToolOperation newBuildOperation(OperationInfo opInfo, IProject project, boolean clearMarkers,
			Collection2<BuildTarget> targetsToBuild) throws CommonException {
		return createBuildOperationCreator(opInfo, project).newProjectBuildOperation(targetsToBuild, clearMarkers);
	}
	
}