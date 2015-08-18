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
package melnorme.lang.ide.core;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import com.github.rustdt.tooling.cargo.CargoManifest;

import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.project_model.AbstractBundleInfo;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class BundleInfo extends AbstractBundleInfo {
	
	protected final CargoManifest manifest;
	
	public BundleInfo(CargoManifest manifest) {
		this.manifest = assertNotNull(manifest);
	}
	
	public CargoManifest getManifest() {
		return manifest;
	}
	
	public String getCrateName() {
		return getManifest().getName();
	}
	
	@Override
	public Indexable<BuildConfiguration> getBuildConfigurations() {
		return new ArrayList2<>();
	}
	
}