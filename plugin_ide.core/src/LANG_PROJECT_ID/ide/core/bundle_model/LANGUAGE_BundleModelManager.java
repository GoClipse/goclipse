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
package LANG_PROJECT_ID.ide.core.bundle_model;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore_Actual.LANGUAGE_BundleModel;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.lang.ide.core.project_model.BundleManifestResourceListener;
import melnorme.lang.ide.core.project_model.BundleModelManager;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class LANGUAGE_BundleModelManager extends BundleModelManager<AbstractBundleInfo, LANGUAGE_BundleModel> {
	
	public LANGUAGE_BundleModelManager() {
		super(new LANGUAGE_BundleModel());
	}
	
	@Override
	protected BundleManifestResourceListener init_createResourceListener() {
		return new ManagerResourceListener(new org.eclipse.core.runtime.Path("lang.bundle"));
	}
	
	@Override
	protected AbstractBundleInfo createNewInfo(IProject project) {
		return new AbstractBundleInfo() {
			
			@Override
			public Indexable<BuildConfiguration> getBuildConfigurations() {
				return ArrayList2.create(new BuildConfiguration("", null));
			}
			
		};
	}
	
}