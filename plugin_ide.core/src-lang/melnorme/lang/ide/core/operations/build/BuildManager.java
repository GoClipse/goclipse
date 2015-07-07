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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.IProjectModelListener;
import melnorme.lang.ide.core.project_model.ProjectBasedModel;
import melnorme.lang.ide.core.project_model.ProjectBasedModelManager;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.misc.StringUtil;


public abstract class BuildManager extends ProjectBasedModelManager {
	
	public static final SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	public static BuildManager getInstance() {
		return LangCore.getBuildManager();
	}
	
	/* -----------------  ----------------- */
	
	protected final BuildModel buildModel;
	
	public BuildManager() {
		this(new BuildModel());
		
		startManager();
	}
	
	public BuildManager(BuildModel buildModel) {
		this.buildModel = buildModel;
	}
	
	public BuildModel getBuildModel() {
		return buildModel;
	}
	
	public ProjectBuildInfo getBuildInfo(IProject project) {
		return buildModel.getProjectInfo(project);
	}
	
	public static class BuildModel 
		extends ProjectBasedModel<ProjectBuildInfo, IProjectModelListener<ProjectBuildInfo>> {
		
		public BuildModel() {
		}
		
		@Override
		protected SimpleLogger getLog() {
			return BuildManager.log;
		}
		
	}
	
	public BuildTarget createBuildTarget(boolean enabled, String targetName) {
		return new BuildTarget(enabled, targetName);
	}
	
	public abstract CommonBuildTargetOperation createBuildTargetOperation(OperationInfo parentOpInfo, 
			IProject project, Path buildToolPath, BuildTarget buildTarget, boolean fullBuild);
	
	/* -----------------  ----------------- */
	
	@Override
	protected void bundleManifestFileChanged(IProject project) {
		// Do Nothing
	}
	
	@Override
	protected Object getProjectInfo(IProject project) {
		return getBuildInfo(project);
	}
	
	@Override
	protected void bundleProjectAdded(IProject project) {
		loadProjectBuildInfo(project);
	}
	
	protected void loadProjectBuildInfo(IProject project) {
		ProjectBuildInfo newProjectBuildInfo;
		
		String targetsPrefValue = getBuildTargetsPref(project);
		if(targetsPrefValue == null) {
			newProjectBuildInfo = createDefaultProjectBuildInfo(project);
		} else {
			try {
				ArrayList2<BuildTarget> buildTargets = createSerializer().readProjectBuildInfo(targetsPrefValue);
				newProjectBuildInfo = new ProjectBuildInfo(this, project, buildTargets);
			} catch(CommonException ce) {
				LangCore.logError(ce);
				return;
			}
		}
		setProjectBuildInfo(project, newProjectBuildInfo);
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
	
	@Override
	protected void bundleProjectRemoved(IProject project) {
		buildModel.removeProjectInfo(project);
	}
	
	/* -----------------  persistence  ----------------- */
	
	protected static final StringPreference BUILD_TARGETS_PREF = new StringPreference("build_targets", "");
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer(this);
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_PREF.get(project));
	}
	
	protected ProjectBuildInfo createDefaultProjectBuildInfo(IProject project) {
		return new ProjectBuildInfo(this, project, ArrayList2.create(
			createBuildTarget(true, null)
		));
	}
	
}