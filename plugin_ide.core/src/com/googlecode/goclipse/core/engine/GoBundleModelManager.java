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
package com.googlecode.goclipse.core.engine;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.engine.GoBundleModelManager.GoBundleModel;

import melnorme.lang.ide.core.CoreSettings;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.SettingsChangeListener;
import melnorme.lang.ide.core.operations.ToolchainPreferences;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.tooling.LocationHandle;
import melnorme.lang.tooling.bundle.BundleInfo;

public class GoBundleModelManager extends BundleModelManager<GoBundleModel> {
	
	protected final CoreSettings settings = LangCore.settings();
	
	public static class GoBundleModel extends LangBundleModel {
		
	}
	
	public GoBundleModelManager() {
		super(new GoBundleModel());
		
		settings.addSettingsListener(new SettingsChangeListener() {
			
			@Override
			public void preferenceChanged(IProjectPreference<?> setting, LocationHandle location, Object newValue) {
				if(
					setting == ToolchainPreferences.SDK_PATH ||
					setting == ToolchainPreferences.USE_PROJECT_SETTINGS ||
					setting == GoEnvironmentPrefs.GO_PATH || 
					setting == GoEnvironmentPrefs.APPEND_PROJECT_LOC_TO_GOPATH
				) {
					IProject project = ResourceUtils.getProjectAt(location);
					// Trigger a model change notification
					model.setBundleInfo(project, model.getBundleInfo(project));
				}
			}
		});
	}
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener();
	}
	
	@Override
	protected BundleInfo createNewInfo(IProject project) {
		return new BundleInfo("default");
	}
	
}