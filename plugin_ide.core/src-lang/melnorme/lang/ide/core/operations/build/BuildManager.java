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
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.ICommonOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.bundle.LaunchArtifact;
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
		this(buildModel, bundleModel, true);
	}
	
	public BuildManager(BuildModel buildModel, LangBundleModel bundleModel, boolean initialize) {
		this.buildModel = buildModel;
		this.bundleModel = bundleModel;
		if(initialize) {
			initialize(bundleModel);
		}
	}
	
	public void initialize(LangBundleModel bundleModel) {
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
		
		// public access
		@Override
		public ProjectBuildInfo getProjectInfo(IProject project) {
			return super.getProjectInfo(project);
		}
		
		// public access
		@Override
		public ProjectBuildInfo setProjectInfo(IProject project, ProjectBuildInfo newProjectInfo) {
			return super.setProjectInfo(project, newProjectInfo);
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
	
	protected static final IProjectPreference<String> BUILD_TARGETS_DATA = 
			new StringPreference("build_targets", "").getProjectPreference();
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer(this);
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_DATA.getStoredValue(project));
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
		
		final ProjectBuildInfo currentBuildInfo = buildModel.getProjectInfo(project);
		
		Map<String, BuildTarget> existingBuildTargets;
		
		if(currentBuildInfo != null) {
			existingBuildTargets = currentBuildInfo.getBuildTargetsMap();
		} else {
			existingBuildTargets = getStoredTargetSettings(project);
		}
		
		// Create new build info
		ArrayList2<BuildTarget> buildTargets = createBuildTargetsForNewBundleInfo(
			project, newBundleInfo, existingBuildTargets);
		ProjectBuildInfo newBuildInfo = new ProjectBuildInfo(this, project, newBundleInfo, buildTargets);
		setProjectBuildInfo(project, newBuildInfo);
	}
	
	protected Map<String, BuildTarget> getStoredTargetSettings(IProject project) {
		Map<String, BuildTarget> existingBuildTargets = new HashMap<>();
		
		String targetsPrefValue = getBuildTargetsPref(project);
		if(targetsPrefValue != null) {
			ArrayList2<BuildTargetData> buildTargetsData;
			try {
				buildTargetsData = createSerializer().readProjectBuildInfo(targetsPrefValue);
			} catch(CommonException ce) {
				LangCore.logError("Error reading project build-info.", ce);
				return existingBuildTargets;
			}
			
			for (BuildTargetData buildTargetData : buildTargetsData) {
				BuildTarget buildTarget;
				try {
					buildTarget = createBuildTarget3(project, buildTargetData);
				} catch(CommonException ce) {
					LangCore.logWarning("Invalid build target.", ce);
					continue;
				}
				existingBuildTargets.put(buildTargetData.getTargetName(), buildTarget);
			}
		}
		return existingBuildTargets;
	}

	protected final ArrayList2<BuildTarget> createBuildTargetsForNewBundleInfo(IProject project, 
			BundleInfo newBundleInfo, Map<String, BuildTarget> currentBuildTargets) {
		
		ArrayList2<BuildTarget> buildTargets = getDefaultBuildTargets(project, newBundleInfo);
	
		for(int ix = 0; ix < buildTargets.size(); ix++) {
			BuildTarget buildTarget = buildTargets.get(ix);
			
			BuildTarget currentBuildTarget = currentBuildTargets.get(buildTarget.getTargetName());
			
			if(currentBuildTarget != null) {
				buildTargets.set(ix, currentBuildTarget);
			}
		}
		
		return buildTargets;
	}
	
	protected ArrayList2<BuildTarget> getDefaultBuildTargets(IProject project, BundleInfo newBundleInfo) {
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		boolean isFirstConfig = true;
		
		Indexable<BuildConfiguration> buildConfigs = newBundleInfo.getBuildConfigurations();
		for(BuildConfiguration buildConfig : buildConfigs) {
			
			Indexable<BuildType> buildTypes = getBuildTypes();
			for(BuildType buildType : buildTypes) {
				
				String targetName = getBuildTargetName2(buildConfig.getName(), buildType.getName());
				
				BuildTargetData newBuildTargetData = new BuildTargetData(targetName, isFirstConfig); 
				
				buildTargets.add(new BuildTarget(project, newBundleInfo, newBuildTargetData, buildType, buildConfig));
				isFirstConfig = false;
			}
			
		}
		return buildTargets;
	}
	
	public ProjectBuildInfo setProjectBuildInfo(IProject project, ProjectBuildInfo newProjectBuildInfo) {
		return buildModel.setProjectInfo(project, newProjectBuildInfo);
	}
	
	public ProjectBuildInfo setProjectBuildInfoAndSave(IProject project, ProjectBuildInfo newProjectBuildInfo) {
		buildModel.setProjectInfo(project, newProjectBuildInfo);
		
		try {
			String data = createSerializer().writeProjectBuildInfo(newProjectBuildInfo);
			BUILD_TARGETS_DATA.setValue(project, data);
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
		
		protected BuildConfiguration getValidBuildconfiguration(String buildConfigName, BundleInfo bundleInfo)
				throws CommonException {
			return bundleInfo.getBuildConfiguration_nonNull(buildConfigName);
		}
		
		public abstract String getDefaultBuildArguments(BuildTarget bt) throws CommonException;
		
		public abstract String getDefaultCheckArguments(BuildTarget bt) throws CommonException;
		
		public LaunchArtifact getMainLaunchArtifact(BuildTarget bt) throws CommonException {
			BuildConfiguration buildConfig = bt.getBuildConfiguration();
			if(buildConfig.getArtifactPath() == null) {
				return null;
			}
			return new LaunchArtifact(buildConfig.getName(), buildConfig.getArtifactPath());
		}
		
		@SuppressWarnings("unused")
		public Indexable<LaunchArtifact> getSubTargetLaunchArtifacts(BuildTarget bt) throws CommonException {
			return null;
		}
		
		public abstract CommonBuildTargetOperation getBuildOperation(BuildTarget bt,
				IOperationConsoleHandler opHandler, Path buildToolPath, String buildArguments)
				throws CommonException;
		
	}
	
	protected final Indexable<BuildType> buildTypes = initBuildTypes();
	
	protected final Indexable<BuildType> initBuildTypes() {
		Indexable<BuildType> buildTypes = getBuildTypes_do();
		assertTrue(buildTypes.size() > 0);
		assertTrue(buildTypes.contains(null) == false);
		return buildTypes;
	}
	
	protected abstract Indexable<BuildType> getBuildTypes_do();
	
	public Indexable<BuildType> getBuildTypes() {
		return buildTypes;
	}
	
	public BuildType getBuildType_NonNull(String buildTypeName) throws CommonException {
		if(buildTypeName == null) {
			return getDefaultBuildType();
		}
		
		for(BuildType buildType : buildTypes) {
			if(areEqual(buildType.getName(), buildTypeName)) {
				return buildType;
			}
		}
		throw new CommonException(BuildManagerMessages.BuildType_NotFound(buildTypeName));
	}
	
	public BuildType getDefaultBuildType() {
		return getBuildTypes().get(0);
	}
	
	/* ----------------- Build Target name ----------------- */
	
	public BuildTargetNameParser getBuildTargetNameParser() {
		return new BuildTargetNameParser();
	}
	
	public final String getDefaultBuildTypeName() {
		return getDefaultBuildType().getName();
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
	
	/* -----------------  Build Target  ----------------- */
	
	public BuildTarget createBuildTarget3(IProject project, BuildTargetDataView buildTargetData) throws CommonException {
		assertNotNull(buildTargetData.getTargetName());
		String targetName = buildTargetData.getTargetName();
		assertNotNull(targetName);
		BuildTargetNameParser nameParser = getBuildTargetNameParser();
		String buildConfigName = nameParser.getBuildConfig(targetName);
		
		BuildType buildType = getBuildType_NonNull(nameParser.getBuildType(targetName));
		
		BundleInfo bundleInfo = bundleModel.getBundleInfo(project); 
		
		return BuildTarget.create(project, bundleInfo, buildTargetData, buildType, buildConfigName);
	}
	
	public BuildTarget getDefinedBuildTarget(IProject project, String buildTargetName) 
			throws CommonException {
		return getBuildTarget(project, buildTargetName, true, true);
	}
	
	public BuildTarget getBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly) 
			throws CommonException, StatusException {
		return getBuildTarget(project, buildTargetName, definedTargetsOnly, true);
	}
	
	public BuildTarget getBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly, 
			boolean requireNonNull) throws CommonException {
		ProjectBuildInfo buildInfo = getValidBuildInfo(project, false);
		
		// validate name after validation project buildInfo
		validateBuildTargetName(buildTargetName);
		
		return getBuildTarget(buildInfo, buildTargetName, definedTargetsOnly, requireNonNull);
	}
	
	protected void validateBuildTargetName(String buildTargetName) throws CommonException {
		if(buildTargetName == null || buildTargetName.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NotSpecified);
		}
	}
	
	public BuildTarget getBuildTarget(ProjectBuildInfo buildInfo, String buildTargetName, boolean definedTargetsOnly,
			boolean requireNonNull) throws CommonException {
		assertNotNull(buildTargetName);
		
		BuildTarget buildTarget = buildInfo.getDefinedBuildTarget(buildTargetName);
		
		if(buildTarget == null) {
			if(!definedTargetsOnly) {
				buildTarget = createBuildTarget3(buildInfo.getProject(), new BuildTargetData(buildTargetName, false));
			}
			else if(requireNonNull) {
				throw new CommonException(LaunchMessages.BuildTarget_NotFound);
			}
		}
		return buildTarget;
	}
	
	/* ----------------- Build operations ----------------- */
	
	protected BuildOperationCreator createBuildOperationCreator(IOperationConsoleHandler opHandler, IProject project) {
		return new BuildOperationCreator(project, opHandler);
	}
	
	public ICommonOperation newProjectClearMarkersOperation(IOperationConsoleHandler opHandler, IProject project) {
		return createBuildOperationCreator(opHandler, project).newClearBuildMarkersOperation();
	}
	
	public final CompositeBuildOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget)
			throws CommonException {
		return newBuildTargetsOperation(project, ArrayList2.create(buildTarget));
	}
	
	public final CompositeBuildOperation newBuildTargetsOperation(IProject project, 
			Collection2<BuildTarget> targetsToBuild) throws CommonException {
		return newBuildOperation(null, project, true, targetsToBuild);
	}
	
	public final CompositeBuildOperation newProjectBuildOperation(IOperationConsoleHandler opHandler, IProject project,
			boolean clearMarkers, boolean isCheck) throws CommonException {
		ArrayList2<BuildTarget> enabledTargets = getValidBuildInfo(project).getEnabledTargets();
		return newBuildOperation(opHandler, project, clearMarkers, enabledTargets, isCheck);
	}
	
	/* ----------------- ----------------- */
	
	public CompositeBuildOperation newBuildOperation(IOperationConsoleHandler opHandler, IProject project, 
			boolean clearMarkers, Collection2<BuildTarget> targetsToBuild) throws CommonException {
		return newBuildOperation(opHandler, project, clearMarkers, targetsToBuild, false);
	}
	
	public CompositeBuildOperation newBuildOperation(IOperationConsoleHandler opHandler, IProject project,
			boolean clearMarkers, Collection2<BuildTarget> targetsToBuild, boolean isCheck) throws CommonException {
		ArrayList2<ICommonOperation> buildCommands = 
				targetsToBuild.mapx((buildTarget) -> buildTarget.getBuildOperation(opHandler, isCheck));
		
		return newTopLevelBuildOperation(opHandler, project, clearMarkers, buildCommands);
	}
	
	public CompositeBuildOperation newTopLevelBuildOperation(IOperationConsoleHandler opHandler, IProject project,
			boolean clearMarkers, Collection2<ICommonOperation> buildCommands) throws CommonException {
		BuildOperationCreator buildOpCreator = createBuildOperationCreator(opHandler, project);
		
		return buildOpCreator.newProjectBuildOperation(buildCommands, clearMarkers);
	}
	
}