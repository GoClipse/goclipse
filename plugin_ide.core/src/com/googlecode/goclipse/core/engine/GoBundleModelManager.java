/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.engine;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.engine.GoBundleModelManager.GoBundleModel;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.lang.ide.core.project_model.LangBundleModel;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class GoBundleModelManager extends BundleModelManager<AbstractBundleInfo, GoBundleModel> {
	
	public static class GoBundleModel extends LangBundleModel<AbstractBundleInfo> {
		
	}
	
	public GoBundleModelManager() {
		super(new GoBundleModel());
	}
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener(null);
	}
	
	
	@Override
	protected AbstractBundleInfo createNewInfo(IProject project) {
		return new AbstractBundleInfo() {
			
			@Override
			public Path getEffectiveTargetFullPath() {
				return null;
			}
			
			@Override
			public Indexable<String> getBuildConfigurations() {
				return ArrayList2.<String>create("");
			}
			
		};
	}
	
}