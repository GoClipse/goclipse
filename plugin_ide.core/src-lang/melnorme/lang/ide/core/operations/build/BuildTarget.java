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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.StringUtil;

public class BuildTarget {
	
	protected final String targetName;
	protected final boolean enabled;
	protected final String buildOptions;
	
	
	public BuildTarget(String targetName, boolean enabled, String buildOptions) {
		this.targetName = StringUtil.nullAsEmpty(targetName);
		this.enabled = enabled;
		this.buildOptions = buildOptions;
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
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getBuildOptions() {
		return buildOptions;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
}