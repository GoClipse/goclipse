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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import melnorme.lang.ide.core.project_model.AbstractBundleInfo.BuildConfiguration;
import melnorme.utilbox.misc.HashcodeUtil;

public class BuildTarget {
	
	protected final String targetName;
	protected final BuildConfiguration buildConfig;
	protected final boolean enabled;
	protected final String buildOptions;
	
	public BuildTarget(String targetName, BuildConfiguration buildConfig, boolean enabled, String buildOptions) {
		this.targetName = nullAsEmpty(targetName);
		this.buildConfig = assertNotNull(buildConfig);
		this.enabled = enabled;
		this.buildOptions = nullAsEmpty(buildOptions);
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	public BuildConfiguration getBuildConfig() {
		return buildConfig;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getBuildOptions() {
		return buildOptions;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(targetName, other.targetName) &&
				areEqual(enabled, other.enabled) &&
				areEqual(buildOptions, other.buildOptions);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(targetName, enabled, buildOptions);
	}
	
	/* -----------------  ----------------- */
	
}