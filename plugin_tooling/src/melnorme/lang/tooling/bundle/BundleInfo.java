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
package melnorme.lang.tooling.bundle;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;

public class BundleInfo extends AbstractBundleInfo {
	
	public BundleInfo() {
	}
	
	@Override
	public Indexable<BuildConfiguration> getBuildConfigurations() {
		return ArrayList2.create(new BuildConfiguration("", null));
	}
	
}