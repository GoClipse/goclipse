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
package com.googlecode.goclipse.core.env;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import com.googlecode.goclipse.core.env.GoBundleModelManager.GoBundleModel;

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
			
			protected final ArrayList2<BuildConfiguration> DEFAULT_BUILD_CONFIGs = ArrayList2.create(
				new BuildConfiguration("#build", null),
				new BuildConfiguration("#install", null),
				new BuildConfiguration("#test", null)
			);
			
			@Override
			public Path getEffectiveTargetFullPath() {
				return null;
			}
			
			@Override
			public Indexable<BuildConfiguration> getBuildConfigurations() {
				return DEFAULT_BUILD_CONFIGs;
			}
			
		};
	}
	
}