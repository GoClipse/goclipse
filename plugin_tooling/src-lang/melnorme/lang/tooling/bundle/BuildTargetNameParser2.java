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

import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import melnorme.utilbox.misc.StringUtil;

public class BuildTargetNameParser2 extends BuildTargetNameParser {
	
	public BuildTargetNameParser2() {
		super(":");
	}
	
	@Override
	public String getFullName(String buildConfig, String buildType) {
		String name = buildType;
		if(buildConfig != null && !buildConfig.isEmpty()) {
			name += getNameSeparator() + buildConfig;
		}
		return name;
	}
	
	@Override
	public String getBuildConfigName(String targetName) {
		return nullAsEmpty(StringUtil.segmentAfterMatch(targetName, getNameSeparator()));
	}
	
	@Override
	public String getBuildTypeName(String targetName) {
		return StringUtil.substringUntilMatch(targetName, getNameSeparator());
	}
	
}