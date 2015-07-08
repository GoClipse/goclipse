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

import melnorme.lang.ide.core.project_model.AbstractBundleInfo.BuildConfiguration;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.StringUtil;

public class BuildTarget {
	
	protected final String targetName;
	protected final BuildConfiguration buildConfig;
	protected final boolean enabled;
	
	public BuildTarget(String targetName, BuildConfiguration buildConfig, boolean enabled) {
		this.targetName = StringUtil.nullAsEmpty(targetName);
		this.buildConfig = assertNotNull(buildConfig);
		this.enabled = enabled;
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
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(enabled, other.enabled) &&
				areEqual(targetName, other.targetName);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(enabled, targetName);
	}
	
	/* -----------------  ----------------- */
	
}