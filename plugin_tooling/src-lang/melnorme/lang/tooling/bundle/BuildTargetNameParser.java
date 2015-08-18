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

import static melnorme.utilbox.misc.StringUtil.emptyAsNull;

import melnorme.utilbox.misc.StringUtil;

public class BuildTargetNameParser {
	
	public BuildTargetNameParser() {
	}
	
	public String getBuildConfig(String buildTargetName) {
		return getBuildConfigName(buildTargetName);
	}
	
	public String getBuildType(String buildTargetName) {
		return getBuildTypeName(buildTargetName);
	}
	
	protected String getNameSeparator() {
		return " #";
	}
	
	public String getFullName(String buildConfig, String buildType) {
		return buildConfig + StringUtil.prefixStr(getNameSeparator(), emptyAsNull(buildType));
	}
	
	public String getBuildConfigName(String targetName) {
		return StringUtil.substringUntilMatch(targetName, getNameSeparator());
	}
	
	public String getBuildTypeName(String targetName) {
		return StringUtil.segmentAfterMatch(targetName, getNameSeparator());
	}
	
}