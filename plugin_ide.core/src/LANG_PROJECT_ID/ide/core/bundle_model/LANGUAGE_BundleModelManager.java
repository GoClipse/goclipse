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
package LANG_PROJECT_ID.ide.core.bundle_model;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore_Actual.LANGUAGE_BundleModel;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class LANGUAGE_BundleModelManager extends BundleModelManager<LANGUAGE_BundleModel> {
	
	public LANGUAGE_BundleModelManager() {
		super(new LANGUAGE_BundleModel());
	}
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener(new org.eclipse.core.runtime.Path("lang.bundle"));
	}
	
	@Override
	protected void bundleProjectRemoved(IProject project) {
		model.removeProjectInfo(project);
	}
	
	@Override
	protected void bundleProjectAdded(IProject project) {
		getModel().setProjectInfo(project, new AbstractBundleInfo() {
			
			protected final ArrayList2<BuildConfiguration> DEFAULT_BUILD_CONFIGs = ArrayList2.create(
				new BuildConfiguration(null, null)
			);
			
			@Override
			public Path getEffectiveTargetFullPath() {
				return null;
			}
			
			@Override
			public Indexable<BuildConfiguration> getBuildConfigurations() {
				return DEFAULT_BUILD_CONFIGs;
			}
			
		});
	}
	
	@Override
	protected void bundleManifestFileChanged(IProject project) {
		bundleProjectAdded(project);
	}
	
}