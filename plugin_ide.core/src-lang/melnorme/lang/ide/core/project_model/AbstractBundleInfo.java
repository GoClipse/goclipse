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
package melnorme.lang.ide.core.project_model;

import melnorme.lang.ide.core.operations.build.BuildManagerMessages;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractBundleInfo {
	
	public abstract Indexable<BuildConfiguration> getBuildConfigurations();
	
	public BuildConfiguration getBuildConfiguration_nonNull(String buildConfigName) throws CommonException {
		for(BuildConfiguration buildConfig : getBuildConfigurations()) {
			if(buildConfig.getName().equals(buildConfigName)) {
				return buildConfig;
			}
		}
		throw new CommonException(BuildManagerMessages.BuildConfig_NotFound(buildConfigName));
	}
	
}