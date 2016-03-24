/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
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

public class BuildTargetData implements BuildTargetDataView {
	
	public String targetName;
	public boolean enabled;
	public String buildArguments;
	public String checkArguments;
	public String executablePath;
	
	public BuildTargetData() {
	}
	
	public BuildTargetData(String targetName, boolean enabled) {
		this.targetName = targetName;
		this.enabled = enabled;
	}
	
	public BuildTargetData(String targetName, boolean enabled, String buildArguments, 
			String checkArguments, String executablePath) {
		this.targetName = targetName;
		this.enabled = enabled;
		this.buildArguments = buildArguments;
		this.checkArguments = checkArguments;
		this.executablePath = executablePath;
	}
	
	public BuildTargetData(BuildTargetDataView data) {
		this(
			data.getTargetName(),
			data.isEnabled(),
			data.getBuildArguments(),
			data.getCheckArguments(),
			data.getExecutablePath()
		);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTargetData)) return false;
		
		BuildTargetData other = (BuildTargetData) obj;
		
		return 
				areEqual(targetName, other.targetName) &&
				areEqual(enabled, other.enabled) &&
				areEqual(buildArguments, other.buildArguments) &&
				areEqual(checkArguments, other.checkArguments) &&
				areEqual(executablePath, other.executablePath);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(targetName, enabled, buildArguments, checkArguments);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public String getTargetName() {
		return targetName;
	}
	
	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public String getBuildArguments() {
		return buildArguments;
	}
	
	@Override
	public String getCheckArguments() {
		return checkArguments;
	}
	
	@Override
	public String getExecutablePath() {
		return executablePath;
	}
	
}