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
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.misc.StringUtil.emptyAsNull;

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
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
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
				IProject project = ResourceUtils.getProject(entry.getKey());
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
			for(BuildType buildType : buildTypes) {
				
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
		
		BuildTargetData data = new BuildTargetData(targetName, isFirstConfig, null, null);
		
		if(oldBuildTarget != null) {
			data.enabled = oldBuildTarget.isEnabled();
			data.buildArguments = oldBuildTarget.getBuildArguments();
			data.artifactPath = oldBuildTarget.getArtifactPath();
		}
		
		buildTargets.add(createBuildTarget(data));
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
		
		public abstract String getDefaultBuildOptions(ValidatedBuildTarget validatedBuildTarget) 
				throws CommonException;
		
		public String getArtifactPath(ValidatedBuildTarget validatedBuildTarget) throws CommonException {
			return validatedBuildTarget.getBuildConfiguration().getArtifactPath();
		}
		
		public abstract CommonBuildTargetOperation getBuildOperation(ValidatedBuildTarget validatedBuildTarget,
				OperationInfo opInfo, Path buildToolPath, boolean fullBuild)
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
		
		public String getArtifactPath() throws CommonException {
			return artifactPath;
		}
		
	}
	
	protected BuildConfiguration getValidBuildConfiguration(IProject project, String buildConfigName) 
			throws CommonException {
		ProjectBuildInfo currentBuildInfo = getValidBuildInfo(project);
		return currentBuildInfo.getBuildConfiguration_nonNull(buildConfigName);
	}
	
	/* ----------------- Build Target name ----------------- */
	
	protected String getBuildTypeNameSeparator() {
		return " #";
	}
	
	public class BuildTargetName {
		
		protected final String buildConfig;
		protected final String buildType;
		
		public BuildTargetName(String buildConfig, String buildTypeName) {
			this.buildConfig = buildConfig;
			this.buildType = buildTypeName;
		}
		
		public BuildTargetName(String buildTargetName) {
			this(getBuildConfigString(buildTargetName), getBuildTypeString(buildTargetName));
		}
		
		public String getBuildConfig() {
			return buildConfig;
		}
		
		public String getBuildType() {
			return buildType;
		}
		
		public String getEffectiveBuildType() {
			if(buildType == null) {
				return getDefaultBuildTypeName();
			}
			return buildType;
		}
		
		public String getRawName() {
			return buildConfig + StringUtil.prefixStr(getBuildTypeNameSeparator(), emptyAsNull(buildType));
		}
		
		public String getResolvedName() {
			return buildConfig + StringUtil.prefixStr(getBuildTypeNameSeparator(), getEffectiveBuildType());
		}
		
	}
	
	public String getBuildConfigString(String targetName) {
		return StringUtil.substringUntilMatch(targetName, getBuildTypeNameSeparator());
	}
	
	public String getBuildTypeString(String targetName) {
		return StringUtil.segmentAfterMatch(targetName, getBuildTypeNameSeparator());
	}
	
	public String getDefaultBuildTypeName() {
		return getBuildTypes().get(0).getName();
	}
	
	public String getBuildTargetName(String buildConfigName, String buildTypeName) {
		return new BuildTargetName(buildConfigName, buildTypeName).getRawName();
	}
	
	public String getFullBuildTargetName(String buildTargetName) {
		return new BuildTargetName(buildTargetName).getResolvedName();
	}
	
	/* -----------------  Build Target Validator  ----------------- */
	
	public ValidatedBuildTarget getValidatedBuildTarget(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		return new ValidatedBuildTarget(project, buildTarget);
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
	
	public final IToolOperation newProjectBuildOperation(OperationInfo opInfo, IProject project,
			boolean fullBuild, boolean clearMarkers) throws CommonException {
		ArrayList2<BuildTarget> enabledTargets = getValidBuildInfo(project).getEnabledTargets();
		return createBuildOperationCreator(opInfo, project, fullBuild)
				.newProjectBuildOperation(enabledTargets, clearMarkers);
	}
	
	public final IToolOperation newProjectClearMarkersOperation(OperationInfo opInfo, IProject project) {
		return createBuildOperationCreator(opInfo, project, false).newClearBuildMarkersOperation();
	}
	
	public final IToolOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget)
			throws CommonException {
		return newBuildTargetsOperation(project, ArrayList2.create(buildTarget));
	}
	
	public IToolOperation newBuildTargetsOperation(IProject project, Collection2<BuildTarget> targetsToBuild)
			throws CommonException {
		OperationInfo operationInfo = LangCore.getToolManager().startNewToolOperation();
		return createBuildOperationCreator(operationInfo, project, false)
				.newProjectBuildOperation(targetsToBuild, true);
	}
	
	protected BuildOperationCreator createBuildOperationCreator(OperationInfo opInfo, IProject project,
			boolean fullBuild) {
		return new BuildOperationCreator(project, opInfo, fullBuild);
	}
	
	public CommonBuildTargetOperation createBuildTargetOperation(OperationInfo opInfo,
			IProject project, Path buildToolPath, BuildTarget buildTarget, boolean fullBuild
	) throws CommonException, CoreException {
		ValidatedBuildTarget validatedBuildTarget = getValidatedBuildTarget(project, buildTarget);
		BuildType buildType = validatedBuildTarget.getBuildType();
		return buildType.getBuildOperation(validatedBuildTarget, opInfo, buildToolPath, fullBuild);
	}
	
}