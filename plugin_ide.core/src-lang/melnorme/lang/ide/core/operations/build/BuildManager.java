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

import static melnorme.lang.ide.core.LangCore_Actual.VAR_NAME_SdkToolPath;
import static melnorme.lang.ide.core.operations.build.BuildManagerMessages.MSG_Starting_LANG_Build;
import static melnorme.lang.ide.core.utils.TextMessageUtils.headerVeryBig;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.option;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildOperationCreator.ProjectBuildOperation;
import melnorme.lang.ide.core.operations.build.BuildTargetOperation.BuildOperationParameters;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.operation.EclipseJobOperation;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.bundle.BundleInfo;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.lang.utils.EnablementCounter;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.StatusException;


public abstract class BuildManager {
	
	public static final SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	public static BuildManager getInstance() {
		return LangCore.getBuildManager();
	}
	
	/* -----------------  ----------------- */
	
	protected final BuildModel buildModel;
	protected final LangBundleModel bundleModel;
	protected final ToolManager toolManager;
	
	public BuildManager(LangBundleModel bundleModel, ToolManager toolManager) {
		this(new BuildModel(), bundleModel, toolManager);
	}
	
	public BuildManager(BuildModel buildModel, LangBundleModel bundleModel, ToolManager toolManager) {
		this(buildModel, bundleModel, toolManager, true);
	}
	
	public BuildManager(BuildModel buildModel, LangBundleModel bundleModel, ToolManager toolManager, 
			boolean initialize) {
		this.buildModel = assertNotNull(buildModel);
		this.bundleModel = assertNotNull(bundleModel);
		this.toolManager = assertNotNull(toolManager);
		if(initialize) {
			initialize(bundleModel);
		}
	}
	
	public ToolManager getToolManager() {
		return toolManager;
	}
	
	public void initialize(LangBundleModel bundleModel) {
		synchronized (init_Lock) { 
			HashMap<String, BundleInfo> projectInfos = bundleModel.connectListener(listener);
			
			for(Entry<String, BundleInfo> entry : projectInfos.entrySet()) {
				IProject project = ResourceUtils.getProject(entry.getKey());
				BundleInfo bundleInfo = entry.getValue();
				bundleProjectAddedOrModified(project, bundleInfo);
			}
		}
	}
	
	protected Object init_Lock = new Object();
	
	protected final IProjectModelListener<BundleInfo> listener = new IProjectModelListener<BundleInfo>() {
		
		@Override
		public void notifyUpdateEvent(UpdateEvent<BundleInfo> updateEvent) {
			synchronized (init_Lock) {
				if(updateEvent.newProjectInfo2 != null) {
					bundleProjectAddedOrModified(updateEvent.project, updateEvent.newProjectInfo2);
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
	
	protected static final IProjectPreference<String> BUILD_TARGETS_DATA = 
			new StringPreference("build_targets", "").getProjectPreference();
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer();
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_DATA.getStoredValue(option(project)));
	}
	
	/* ----------------- ProjectBuildInfo ----------------- */
	
	protected void bundleProjectAddedOrModified(IProject project, BundleInfo newBundleInfo) {
		loadProjectBuildInfo(project, newBundleInfo);
	}
	
	protected void bundleProjectRemoved(IProject project) {
		buildModel.removeProjectInfo(project);
	}
	
	public ProjectBuildInfo setProjectBuildInfo(IProject project, ProjectBuildInfo newProjectBuildInfo) {
		return buildModel.setProjectInfo(project, newProjectBuildInfo);
	}
	
	public void tryUpdateProjectBuildInfo(IProject project, ProjectBuildInfo oldInfo, ProjectBuildInfo newInfo) 
			throws CommonException {
		boolean success = buildModel.updateProjectInfo(project, oldInfo, newInfo);
		if(!success) {
			throw new CommonException(BuildManagerMessages.ERROR_ProjectBuildSettingsOutOfDate);
		}
	}
	
	protected void loadProjectBuildInfo(IProject project, BundleInfo newBundleInfo) {
		assertNotNull(project);
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
				buildTargetsData = createSerializer().readFromString(targetsPrefValue);
			} catch(CommonException ce) {
				LangCore.logError("Error reading project build-info.", ce);
				return existingBuildTargets;
			}
			
			for (BuildTargetData buildTargetData : buildTargetsData) {
				BuildTarget buildTarget;
				try {
					buildTarget = createBuildTarget(project, buildTargetData);
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
				buildTargets.set(ix, createBuildTargetForNewBundleInfo(
					newBundleInfo, buildTarget, currentBuildTarget.getDataCopy()));
			}
		}
		
		return buildTargets;
	}
	
	protected BuildTarget createBuildTargetForNewBundleInfo(BundleInfo newBundleInfo, BuildTarget defaultBuildTarget, 
			BuildTargetData btd) {
		return new BuildTarget(defaultBuildTarget.getProject(), newBundleInfo, btd, 
			defaultBuildTarget.getBuildType(), defaultBuildTarget.getBuildConfiguration());
	}
	
	protected ArrayList2<BuildTarget> getDefaultBuildTargets(IProject project, BundleInfo newBundleInfo) {
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		boolean isFirstConfig = true;
		
		Indexable<BuildConfiguration> buildConfigs = newBundleInfo.getBuildConfigurations();
		for(BuildConfiguration buildConfig : buildConfigs) {
			
			for(BuildType buildType : getBuildTypes()) {
				
				String targetName = getBuildTargetName2(buildConfig.getName(), buildType.getName());
				
				BuildTargetData newBuildTargetData = new BuildTargetData(targetName, isFirstConfig, false); 
				
				addDefaultBuildTarget(buildTargets, project, newBundleInfo, buildConfig, buildType, newBuildTargetData);
				isFirstConfig = false;
			}
			
		}
		return buildTargets;
	}
	
	protected void addDefaultBuildTarget(ArrayList2<BuildTarget> buildTargets, IProject project, BundleInfo bundleInfo,
			BuildConfiguration buildConfig, BuildType buildType, BuildTargetData btd) {
		buildTargets.add(new BuildTarget(project, bundleInfo, btd, buildType, buildConfig));
	}
	
	public void saveProjectInfo(IProject project) {
		ProjectBuildInfo projectInfo = buildModel.getProjectInfo(project);
		try {
			String data = createSerializer().writeProjectBuildInfo(projectInfo);
			BUILD_TARGETS_DATA.setValue(project, data);
		} catch(CommonException e) {
			LangCore.logError("Error persisting project build info: ", e);
		}
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
		
		public String getDefaultCommandLine(BuildTarget bt) throws CommonException {
			return VariablesResolver.variableRefString(VAR_NAME_SdkToolPath) + " " + getDefaultCommandArguments(bt);
		}
		
		public abstract String getDefaultCommandArguments(BuildTarget bt) throws CommonException;
		
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
		
		public abstract BuildTargetOperation getBuildOperation(BuildOperationParameters buildOpParams) 
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
		if(buildTypeName == null || buildTypeName.isEmpty()) {
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
	
	public BuildTarget createBuildTarget(IProject project, BuildTargetDataView buildTargetData) 
			throws CommonException {
		
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
	
	public final BuildTarget getBuildTarget(IProject project, String buildTargetName, boolean definedTargetsOnly, 
			boolean requireNonNull) throws CommonException {
		ProjectBuildInfo buildInfo = getValidBuildInfo(project, false);
		
		return getBuildTarget_x(buildInfo, buildTargetName, definedTargetsOnly, requireNonNull);
	}
	
	protected void validateBuildTargetName(String buildTargetName) throws CommonException {
		if(buildTargetName == null || buildTargetName.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NotSpecified);
		}
	}
	
	public BuildTarget getBuildTarget_x(ProjectBuildInfo buildInfo, String buildTargetName, boolean definedTargetsOnly,
			boolean requireNonNull) throws CommonException {
		// validate name after validation project buildInfo
		validateBuildTargetName(buildTargetName);
		
		BuildTarget buildTarget = buildInfo.getDefinedBuildTarget(buildTargetName);
		
		if(buildTarget == null) {
			if(!definedTargetsOnly) {
				buildTarget = createBuildTarget(buildInfo.getProject(), 
					new BuildTargetData(buildTargetName, false, false));
			}
			else if(requireNonNull) {
				throw new CommonException(LaunchMessages.BuildTarget_NotFound);
			}
		}
		return buildTarget;
	}
	
	public BuildTarget getFirstDefinedBuildTarget(IProject project, BuildType buildType) throws CommonException {
		ProjectBuildInfo buildInfo = getBuildInfo(project);
		
		assertNotNull(buildType);
		BuildTarget foundBT = buildInfo.getBuildTargets().findElement((bt) -> bt.getBuildType() == buildType);
		if(foundBT == null) {
			throw CommonException.fromMsgFormat(
				BuildManagerMessages.NO_BUILD_TARGET_FOUND_FOR_BUILD_TYPE_0, buildType.getName());
		}
		return foundBT;
	}
	
	/* ----------------- Build operations ----------------- */
	
	protected final EnablementCounter autoBuildsEnablement = new EnablementCounter();
	
	public EnablementCounter autoBuildsEnablement() {
		return autoBuildsEnablement;
	}
	
	/* -----------------  ----------------- */
	
	public final void executeBuildTargetOperation(
		IOperationMonitor om, IProject project, BuildTarget buildTarget
	) throws CommonException, OperationCancellation {
		executeBuildTargetsOperation(om, project, ArrayList2.create(buildTarget));
	}
	
	public final void executeBuildTargetsOperation(
		IOperationMonitor om, IProject project, Iterable<BuildTarget> targetsToBuild
	) throws CommonException, OperationCancellation {
		IToolOperationMonitor toolMonitor = getToolManager().startNewBuildOperation();
		requestBuildOperation(toolMonitor, project, true, targetsToBuild).execute(om);
	}
	
	public final EclipseJobOperation requestMultiBuild(
		IOperationMonitor parentOM, 
		Iterable<IProject> projects,
		boolean clearMarkers
	) throws CommonException, OperationCancellation {
		IToolOperationMonitor toolMonitor = getToolManager().startNewBuildOperation();
		toolMonitor.writeInfoMessage(
			headerVeryBig(MessageFormat.format(MSG_Starting_LANG_Build, LangCore_Actual.NAME_OF_LANGUAGE))
		);
		
		ArrayList2<ProjectBuildOperation> projectOps = ArrayList2.create();
		
		for (IProject project : projects) {
			// Note: this will immediately cancel previous operations
			ProjectBuildOperation newBuildOp = 
				requestProjectBuildOperation(toolMonitor, project, clearMarkers, false);
			projectOps.add(newBuildOp);
		}
		
		// Clear markers for all projects first
		// This is because building for a project/bundle can actually create markers in other projects
		for (IProject project : projects) {
			newProjectClearMarkersOperation(toolMonitor, project).execute(parentOM);
		}
		
		Operation op = (om) -> {
			for (ProjectBuildOperation projectOperation : projectOps) {
				projectOperation.execute(om);
			}
		};
		
		String opName = MessageFormat.format("Running {0} build", LangCore_Actual.NAME_OF_LANGUAGE);
		EclipseJobOperation job = new EclipseJobOperation(opName, getToolManager(), op);
		job.schedule();
		return job;
	}
	
	public Operation newProjectClearMarkersOperation(
		IToolOperationMonitor toolMonitor, IProject project
	) throws CommonException {
		return new ClearMarkersOperation(project, toolMonitor);
	}
	
	/* ----------------- ----------------- */
	
	public final ProjectBuildOperation requestProjectBuildOperation(
		IToolOperationMonitor toolMonitor,
		IProject project,
		boolean clearMarkers, 
		boolean isAuto
	) throws CommonException, OperationCancellation {
		ArrayList2<BuildTarget> enabledTargets = getValidBuildInfo(project).getEnabledTargets(!isAuto);
		return requestBuildOperation(toolMonitor, project, clearMarkers, enabledTargets);
	}
	
	public ProjectBuildOperation requestBuildOperation(
		IToolOperationMonitor toolMonitor,
		IProject project, 
		boolean clearMarkers, 
		Iterable<BuildTarget> targetsToBuild
	) throws CommonException {
		assertNotNull(toolMonitor);
		ArrayList2<Operation> buildCommands = CollectionUtil.mapx(targetsToBuild, 
			(buildTarget) -> buildTarget.getBuildOperation(toolManager, toolMonitor));
		
		BuildOperationCreator opCreator = createBuildOperationCreator(toolMonitor, project);
		ProjectBuildOperation newBuildOp = opCreator.newProjectBuildOperation2(clearMarkers, buildCommands);
		setNewBuildOperation(newBuildOp);
		return newBuildOp;
	}
	
	protected BuildOperationCreator createBuildOperationCreator(
		IToolOperationMonitor opMonitor, IProject project
	) throws CommonException {
		return new BuildOperationCreator(project, opMonitor);
	}
	
	/* -----------------  ----------------- */
	
	protected HashMap2<Location, ProjectBuildOperation> buildOps = new HashMap2<>();
	protected final Object buildOps_mutex = new Object();
	
	public ProjectBuildOperation getBuildOperation(Location location) {
		synchronized (buildOps_mutex) {
			return buildOps.get0(location);
		}
	}
	
	public ProjectBuildOperation setNewBuildOperation(ProjectBuildOperation newOperation) {
		Location location = newOperation.getLocation();
		ProjectBuildOperation oldOp;
		synchronized (buildOps_mutex) {
			oldOp = buildOps.put(location, newOperation);
		}
		if(oldOp != null) {
			oldOp.tryCancel();
		}
		return oldOp;
	}
	
	public Collection<ProjectBuildOperation> cancelAllBuilds() {
		HashMap2<Location, ProjectBuildOperation> oldBuildOps;
		synchronized (buildOps_mutex) {
			oldBuildOps = buildOps;
			buildOps = new HashMap2<>();
		}
		for (ProjectBuildOperation buildOp : oldBuildOps.values()) {
			buildOp.tryCancel();
		}
		return oldBuildOps.values();
	}
	
}