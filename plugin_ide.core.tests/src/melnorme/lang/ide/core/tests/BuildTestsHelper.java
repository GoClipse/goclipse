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
package melnorme.lang.ide.core.tests;

import com.github.rustdt.tooling.cargo.CargoManifest;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.tooling.LANG_SPECIFIC;
import melnorme.lang.tooling.bundle.FileRef;
import melnorme.utilbox.collections.ArrayList2;

@LANG_SPECIFIC
public class BuildTestsHelper {
	
	public static BundleInfo createSampleBundleInfoA(String name, String version) {
		return new BundleInfo(new CargoManifest(name, version, null, 
			new ArrayList2<>(
				new FileRef("sampleConfig", null)
			)
		));
	}
	
}