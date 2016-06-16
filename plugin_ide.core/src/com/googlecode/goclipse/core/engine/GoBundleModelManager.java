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

import com.googlecode.goclipse.core.engine.GoBundleModelManager.GoBundleModel;

import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.lang.tooling.bundle.BundleInfo;

public class GoBundleModelManager extends BundleModelManager<GoBundleModel> {
	
	public static class GoBundleModel extends LangBundleModel {
		
	}
	
	public GoBundleModelManager() {
		super(new GoBundleModel());
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