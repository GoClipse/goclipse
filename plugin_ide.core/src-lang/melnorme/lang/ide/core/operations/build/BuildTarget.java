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
	
	public static class BuildTargetData {
		
		public String targetName;
		public boolean enabled;
		public String buildArguments;
		public String artifactPath;
		
		public BuildTargetData() {
		}
		
		public BuildTargetData(String targetName, boolean enabled, String buildOptions, String artifactPath) {
			this.targetName = targetName;
			this.enabled = enabled;
			this.buildArguments = buildOptions;
			this.artifactPath = artifactPath;
		}
		
	}
	
	protected final String targetName;
	protected final boolean enabled;
	protected final String buildArguments;
	protected final String artifactPath;
	
	public BuildTarget(BuildTargetData data) {
		this(data.targetName, data.enabled, data.buildArguments, data.artifactPath);
	}
	
	public BuildTarget(String targetName, boolean enabled, String buildArguments, String artifactPath) {
		this.targetName = StringUtil.nullAsEmpty(targetName);
		this.enabled = enabled;
		this.buildArguments = buildArguments;
		this.artifactPath = artifactPath;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(targetName, other.targetName) &&
				areEqual(enabled, other.enabled) &&
				areEqual(buildArguments, other.buildArguments) &&
				areEqual(artifactPath, other.artifactPath);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(targetName, enabled, buildArguments);
	}
	
	@Override
	public String toString() {
		return targetName + (enabled ? " [ENABLED]" : "");
	}
	
	/* -----------------  ----------------- */
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getBuildArguments() {
		return buildArguments;
	}
	
	public String getTargetName() {
		return targetName;
	}
	
	public String getArtifactPath() {
		return artifactPath;
	}
	
	public BuildTargetData getDataCopy() {
		return new BuildTargetData(
			getTargetName(),
			isEnabled(),
			getBuildArguments(),
			getArtifactPath()
		);
	}
	
}