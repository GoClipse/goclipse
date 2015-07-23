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

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.HashcodeUtil;
import melnorme.utilbox.misc.StringUtil;

public class BuildTarget {
	
	public static final String BUILD_TYPE_NAME_SEPARATOR = "#";
	
	public static String getBuildConfigString(String targetName) {
		return StringUtil.substringUntilMatch(targetName, BuildTarget.BUILD_TYPE_NAME_SEPARATOR);
	}
	
	public static String getBuildTypeString(String targetName) {
		return StringUtil.segmentAfterMatch(targetName, BuildTarget.BUILD_TYPE_NAME_SEPARATOR);
	}
	
	protected final String buildConfiguration;
	protected final String buildTypeName;
	protected final boolean enabled;
	protected final String buildOptions;
	
	public BuildTarget(String targetName, boolean enabled, String buildOptions) {
		assertNotNull(targetName);
		this.buildConfiguration = nullAsEmpty(getBuildConfigString(targetName));
		this.buildTypeName = getBuildTypeString(targetName);
		this.enabled = enabled;
		this.buildOptions = buildOptions;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof BuildTarget)) return false;
		
		BuildTarget other = (BuildTarget) obj;
		
		return 
				areEqual(buildConfiguration, other.buildConfiguration) &&
				areEqual(buildTypeName, other.buildTypeName) &&
				areEqual(enabled, other.enabled) &&
				areEqual(buildOptions, other.buildOptions);
	}
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(buildConfiguration, buildTypeName, enabled, buildOptions);
	}
	
	/* -----------------  ----------------- */
	
	public String getBuildConfiguration() {
		return buildConfiguration;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getBuildOptions() {
		return buildOptions;
	}
	
	public String getBuildTypeName() {
		return buildTypeName;
	}
	
	public String getTargetName() {
		return buildConfiguration + StringUtil.prefixStr(BUILD_TYPE_NAME_SEPARATOR, buildTypeName);
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class BuildType {
		
		protected final String name;
		
		public BuildType(String name) {
			this.name = assertNotNull(name);
		}
		
		/* -----------------  ----------------- */
		
		public String getName() {
			return name;
		}
		
		public abstract String getDefaultBuildOptions(BuildTarget buildTarget, IProject project)
				throws CommonException;
		
		public abstract Path getArtifactPath(BuildTarget buildTarget, IProject project)
				throws CommonException;
		
	}
	
	/* -----------------  ----------------- */
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected BuildType getBuildType() throws CommonException {
		return getBuildManager().getBuildType_NonNull(getBuildTypeName());
	}
	
	public String getDefaultBuildOptions(IProject project) throws CommonException {
		return getBuildType().getDefaultBuildOptions(this, project);
	}
	
	public Path getArtifactPath(IProject project) throws CommonException {
		return getBuildType().getArtifactPath(this, project);
	}
	
}