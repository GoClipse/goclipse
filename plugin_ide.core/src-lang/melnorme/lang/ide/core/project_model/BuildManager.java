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
package melnorme.lang.ide.core.project_model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.BuildTarget;
import melnorme.lang.ide.core.utils.prefs.StringPreference;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.SimpleLogger;
import melnorme.utilbox.misc.StringUtil;


public abstract class BuildManager extends ProjectBasedModelManager {
	
	public static final SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	/* -----------------  ----------------- */
	
	protected final BuildModel buildModel;
	
	public BuildManager() {
		this(new BuildModel());
		
		startManager();
	}
	
	public BuildManager(BuildModel buildModel) {
		this.buildModel = buildModel;
	}
	
	public Indexable<BuildTarget> getBuildTargets(IProject project) {
		return buildModel.getProjectInfo(project);
	}
	
	public static class BuildModel 
		extends ProjectBasedModel<Indexable<BuildTarget>, IProjectModelListener<Indexable<BuildTarget>>> {
		
		public BuildModel() {
		}
		
		@Override
		protected SimpleLogger getLog() {
			return BuildManager.log;
		}
		
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected void bundleManifestFileChanged(IProject project) {
		// Do Nothing
	}
	
	@Override
	protected Object getProjectInfo(IProject project) {
		return getBuildTargets(project);
	}
	
	@Override
	protected void bundleProjectAdded(IProject project) {
		ArrayList2<BuildTarget> newProjectBuildInfo;
		
		String targetsPrefValue = getBuildTargetsPref(project);
		if(targetsPrefValue == null) {
			newProjectBuildInfo = createDefaultProjectBuildInfo();
		} else {
			try {
				newProjectBuildInfo = createSerializer().readProjectBuildInfo(targetsPrefValue);
			} catch(CommonException ce) {
				LangCore.logError(ce);
				return;
			}
		}
		buildModel.addProjectInfo(project, newProjectBuildInfo);
	}
	
	@Override
	protected void bundleProjectRemoved(IProject project) {
		buildModel.removeProjectInfo(project);
	}
	
	/* -----------------  persistence  ----------------- */
	
	protected static final StringPreference BUILD_TARGETS_PREF = new StringPreference("build_targets", "");
	
	protected BuildTargetsSerializer createSerializer() {
		return new BuildTargetsSerializer();
	}
	
	protected String getBuildTargetsPref(IProject project) {
		return StringUtil.emptyAsNull(BUILD_TARGETS_PREF.get(project));
	}
	
	protected ArrayList2<BuildTarget> createDefaultProjectBuildInfo() {
		return ArrayList2.create(
			new BuildTarget(true, null)
		);
	}
	
}