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

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo.BuildConfiguration;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.project_model.UpdateEvent;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
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
	
	public ProjectBuildInfo getBuildInfo_NonNull(IProject project) throws CommonException {
		ProjectBuildInfo buildInfo = getBuildInfo(project);
		if(buildInfo == null) {
			throw new CommonException("No project build targets information available.");
		}
		return buildInfo;
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
					currentBuildInfo = new ProjectBuildInfo(this, project, buildTargets);
				} catch(CommonException ce) {
					LangCore.logError(ce);
				}
			}
		}
		
		Indexable<BuildConfiguration> buildConfigs = bundleInfo.getBuildConfigurations();
		
		ArrayList2<BuildTarget> buildTargets = new ArrayList2<>();
		
		boolean isFirstConfig = true;
		for(BuildConfiguration buildConfig : buildConfigs) {
			addBuildTargetFromConfig(buildTargets, buildConfig, currentBuildInfo, isFirstConfig);
			isFirstConfig = false;
		}
		
		ProjectBuildInfo newBuildInfo = new ProjectBuildInfo(this, project, buildTargets);
		setProjectBuildInfo(project, newBuildInfo);
	}
	
	protected void addBuildTargetFromConfig(ArrayList2<BuildTarget> buildTargets, BuildConfiguration buildConfig,
			ProjectBuildInfo currentBuildInfo, boolean isFirstConfig) {
		String name = buildConfig.getName();
		addBuildTargetFromConfig(buildTargets, buildConfig, currentBuildInfo, isFirstConfig, name);
	}
	
	protected void addBuildTargetFromConfig(ArrayList2<BuildTarget> buildTargets, BuildConfiguration buildConfig,
			ProjectBuildInfo currentBuildInfo, boolean isFirstConfig, String name) {
		boolean enabled = getIsEnabled(currentBuildInfo, isFirstConfig, name);
		
		buildTargets.add(createBuildTarget(name, buildConfig, enabled));
	}
	
	protected boolean getIsEnabled(ProjectBuildInfo currentBuildInfo, boolean isFirstConfig, String name) {
		BuildTarget oldBuildTarget = currentBuildInfo == null ? null : currentBuildInfo.getDefinedBuildTarget(name);
		return oldBuildTarget == null ? isFirstConfig : oldBuildTarget.isEnabled();
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
	
	public BuildTarget getBuildTargetFor(ProjectBuildInfo projectBuildInfo, String targetName) throws CommonException {
		return projectBuildInfo.getDefinedBuildTarget(targetName);
	}
	
	public BuildTarget createBuildTarget(String targetName, BuildConfiguration buildConfig, boolean enabled) {
		return new BuildTarget(targetName, buildConfig, enabled);
	}
	
	/* ----------------- Build operations ----------------- */
	
	public IBuildTargetOperation newProjectBuildOperation(IProject project, boolean fullBuild) throws CommonException {
		OperationInfo parentOpInfo = new OperationInfo(project, true, "");
		return newProjectBuildOperation(project, parentOpInfo, fullBuild);
	}
	
	public IBuildTargetOperation newProjectBuildOperation(IProject project, OperationInfo parentOpInfo,
			boolean fullBuild) throws CommonException {
		return new BuildOperationCreator(project, parentOpInfo, fullBuild).newProjectBuildOperation();
	}
	
	public IBuildTargetOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget)
			throws CommonException {
		return newBuildTargetOperation(project, ArrayList2.create(buildTarget));
	}
	
	public IBuildTargetOperation newBuildTargetOperation(IProject project, Collection2<BuildTarget> targetsToBuild)
			throws CommonException {
		OperationInfo parentOpInfo = new OperationInfo(project, true, "");
		return new BuildOperationCreator(project, parentOpInfo, false).newProjectBuildOperation(targetsToBuild);
	}
	
	public abstract CommonBuildTargetOperation createBuildTargetSubOperation(OperationInfo parentOpInfo, 
			IProject project, Path buildToolPath, BuildTarget buildTarget, boolean fullBuild);
	
	
	/* -----------------  Persistence  ----------------- */
	
	protected static final StringPreference BUILD_TARGETS_PREF = new StringPreference("build_targets", "");
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer(this);
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_PREF.get(project));
	}
	
}