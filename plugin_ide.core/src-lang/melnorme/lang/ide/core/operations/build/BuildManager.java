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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
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
	protected final LangBundleModel<? extends AbstractBundleInfo> bundleModel;
	
	public BuildManager(LangBundleModel<? extends AbstractBundleInfo> bundleModel) {
		this(new BuildModel(), bundleModel);
	}
	
	public BuildManager(BuildModel buildModel, LangBundleModel<? extends AbstractBundleInfo> bundleModel) {
		this.buildModel = buildModel;
		this.bundleModel = bundleModel;
		synchronized (init_Lock) { 
			HashMap<String, ? extends AbstractBundleInfo> projectInfos = bundleModel.connectListener(listener);
			
			for(Entry<String, ? extends AbstractBundleInfo> entry : projectInfos.entrySet()) {
				IProject project = EclipseUtils.getProject(entry.getKey());
				AbstractBundleInfo bundleInfo = entry.getValue();
				bundleProjectAdded(project, bundleInfo);
			}
		}
	}
	
	protected Object init_Lock = new Object();
	
	protected final IProjectModelListener<AbstractBundleInfo> listener = 
			new IProjectModelListener<AbstractBundleInfo>() {
		
		@Override
		public void notifyUpdateEvent(UpdateEvent<AbstractBundleInfo> updateEvent) {
			synchronized (init_Lock) {
				if(updateEvent.newProjectInfo != null) {
					bundleProjectAdded(updateEvent.project, updateEvent.newProjectInfo);
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
		return buildModel.getProjectInfo(project);
	}
	
	public ProjectBuildInfo getValidBuildInfo(IProject project) throws CommonException {
		return getValidBuildInfo(project, true);
	}
	
	public ProjectBuildInfo getValidBuildInfo(IProject project, boolean requireNonEmtpyTargets) 
			throws CommonException {
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
	
	/* -----------------  ----------------- */
	
	protected void bundleProjectAdded(IProject project, AbstractBundleInfo bundleInfo) {
		loadProjectBuildInfo(project, bundleInfo);
	}
	
	protected void bundleProjectRemoved(IProject project) {
		buildModel.removeProjectInfo(project);
	}
	
	protected void loadProjectBuildInfo(IProject project, AbstractBundleInfo bundleInfo) {
		ProjectBuildInfo currentBuildInfo = buildModel.getProjectInfo(project);
		
		if(currentBuildInfo == null) {
			String targetsPrefValue = getBuildTargetsPref(project);
			if(targetsPrefValue != null) {
				try {
					ArrayList2<BuildTarget> buildTargets = createSerializer().readProjectBuildInfo(targetsPrefValue);
					currentBuildInfo = new ProjectBuildInfo(this, project, bundleInfo, buildTargets);
				} catch(CommonException ce) {
					LangCore.logError(ce);
				}
			}
		}
		
		
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		boolean isFirstConfig = true;
		
		Indexable<BuildConfiguration> buildConfigs = bundleInfo.getBuildConfigurations();
		for(BuildConfiguration buildConfig : buildConfigs) {
			
			Indexable<BuildType> buildTypes = getBuildTypes();
			for (BuildType buildType : buildTypes) {
				
				String targetName = getBuildTargetName(buildConfig.getName(), buildType.getName()); 
				
				addBuildTargetFromConfig(buildTargets, currentBuildInfo, isFirstConfig, targetName);
				isFirstConfig = false;
			}
			
		}
		
		ProjectBuildInfo newBuildInfo = new ProjectBuildInfo(this, project, bundleInfo, buildTargets);
		setProjectBuildInfo(project, newBuildInfo);
	}
	
	protected void addBuildTargetFromConfig(ArrayList2<BuildTarget> buildTargets, ProjectBuildInfo currentBuildInfo,
			boolean isFirstConfig, String targetName) {
		BuildTarget oldBuildTarget = currentBuildInfo == null ? 
				null : 
				currentBuildInfo.getDefinedBuildTarget(targetName);
		
		boolean enabled;
		String buildOptions;
		
		if(oldBuildTarget == null) {
			enabled = isFirstConfig;
			buildOptions = null;
		} else {
			enabled = oldBuildTarget.isEnabled();
			buildOptions = oldBuildTarget.getBuildOptions();
		}
		
		buildTargets.add(createBuildTarget(targetName, enabled, buildOptions));
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
		
		public abstract String getDefaultBuildOptions(BuildTargetValidator buildTargetValidator)
				throws CommonException, CoreException;
		
		public String getArtifactPath(BuildTargetValidator buildTargetValidator) 
				throws CommonException, CoreException {
			return buildTargetValidator.getBuildConfiguration().getArtifactPath();
		}
		
		public abstract CommonBuildTargetOperation getBuildOperation(BuildTargetValidator buildTargetValidator, 
				OperationInfo opInfo, Path buildToolPath, boolean fullBuild) throws CommonException, CoreException;
		
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
			if(buildType.getName().equals(buildTypeName)) {
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
		
		public String getArtifactPath() {
			return artifactPath;
		}
		
	}
	
	protected BuildConfiguration getValidBuildConfiguration(IProject project, String buildConfigName) 
			throws CommonException {
		ProjectBuildInfo currentBuildInfo = getValidBuildInfo(project);
		return currentBuildInfo.getBuildConfiguration_nonNull(buildConfigName);
	}
	
	/* ----------------- Build Target ----------------- */
	
	public static final String BUILD_TYPE_NAME_SEPARATOR = "#";
	
	public String getBuildTargetName(String buildConfigName, String buildType) {
		return buildConfigName + StringUtil.prefixStr(BUILD_TYPE_NAME_SEPARATOR, buildType);
	}
	
	public String getBuildConfigString(String targetName) {
		return StringUtil.substringUntilMatch(targetName, BUILD_TYPE_NAME_SEPARATOR);
	}
	
	public String getBuildTypeString(String targetName) {
		return StringUtil.segmentAfterMatch(targetName, BUILD_TYPE_NAME_SEPARATOR);
	}
	
	public BuildTarget createBuildTarget(String targetName, boolean enabled, String buildOptions) {
		return new BuildTarget(targetName, enabled, buildOptions);
	}
	
	public BuildTarget getValidDefinedBuildTarget(IProject project, String buildTargetName) 
			throws CommonException, StatusException {
		return getValidBuildTarget(project, buildTargetName, false, true);
	}
	
	public BuildTarget getValidBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly) 
			throws CommonException, StatusException {
		return getValidBuildTarget(project, buildTargetName, definedTargetsOnly, true);
	}
	
	public BuildTarget getValidBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly, 
			boolean requireNonNull) throws CommonException {
		ProjectBuildInfo buildInfo = getValidBuildInfo(project);
		
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
	
	/* -----------------  ----------------- */
	
	public BuildTargetValidator createBuildTargetValidator(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		String targetName = buildTarget.getTargetName();
		String buildOptions = buildTarget.getBuildOptions();
		
		return createBuildTargetValidator(project, targetName, buildOptions);
	}
	
	public BuildTargetValidator createBuildTargetValidator(IProject project, String targetName, String buildOptions)
			throws CommonException {
		String buildConfigName = getBuildConfigString(targetName);
		String buildTypeName = getBuildTypeString(targetName);
		if(buildTypeName == null) {
			buildTypeName = getBuildTypes().get(0).getName();
		}
		return createBuildTargetValidator(project, buildConfigName, buildTypeName, buildOptions);
	}
	
	public abstract BuildTargetValidator createBuildTargetValidator(IProject project, String buildConfigName,
			String buildTypeName, String buildOptions) throws CommonException;
	
	public BuildTarget getBuildTargetFor(ProjectBuildInfo projectBuildInfo, String targetName) throws CommonException {
		return projectBuildInfo.getDefinedBuildTarget(targetName);
	}
	
	/* ----------------- Build operations ----------------- */
	
	public IToolOperation newProjectBuildOperation(OperationInfo opInfo, IProject project,
			boolean fullBuild) throws CommonException {
		return new BuildOperationCreator(project, opInfo, fullBuild).newProjectBuildOperation();
	}
	
	public IToolOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget)
			throws CommonException {
		return newBuildTargetOperation(project, ArrayList2.create(buildTarget));
	}
	
	public IToolOperation newBuildTargetOperation(IProject project, Collection2<BuildTarget> targetsToBuild)
			throws CommonException {
		OperationInfo operationInfo = LangCore.getToolManager().startNewToolOperation();
		return newBuildTargetOperation(operationInfo, project, targetsToBuild);
	}
	
	public IToolOperation newBuildTargetOperation(OperationInfo opInfo, IProject project, 
			Collection2<BuildTarget> targetsToBuild) throws CommonException {
		return new BuildOperationCreator(project, opInfo, false).newProjectBuildOperation(targetsToBuild);
	}
	
	public CommonBuildTargetOperation createBuildTargetOperation(OperationInfo opInfo,
			IProject project, Path buildToolPath, BuildTarget buildTarget, boolean fullBuild
	) throws CommonException, CoreException {
		BuildTargetValidator buildTargetValidator = createBuildTargetValidator(project, buildTarget);
		BuildType buildType = buildTargetValidator.getBuildType();
		return buildType.getBuildOperation(buildTargetValidator, opInfo, buildToolPath, fullBuild);
	}
	
}