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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.PathUtil;

public class BuildTargetValidator extends AbstractValidator2 {
	
	public final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final String buildConfigName;
	protected final String buildTypeName;
	protected final String buildOptions;
	
	public BuildTargetValidator(IProject project, String buildConfigName, String buildTypeName,
			String buildOptions) {
		this.project = project;
		this.buildConfigName = assertNotNull(buildConfigName);
		this.buildTypeName = assertNotNull(buildTypeName);
		this.buildOptions = buildOptions;
	}
	
	public BuildManager getBuildManager() {
		return buildMgr;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getBuildConfigName() {
		return buildConfigName;
	}
	
	public String getBuildTypeName() {
		return buildTypeName;
	}
	
	public String getBuildOptions() {
		return buildOptions;
	}
	
	public BuildConfiguration getBuildConfiguration() throws CommonException {
		return buildMgr.getValidBuildConfiguration(project, buildConfigName);
	}
	
	public BuildType getBuildType() throws CommonException {
		return buildMgr.getBuildType_NonNull(buildTypeName);
	}
	
	public String getEffectiveBuildOptions() throws CommonException, CoreException {
		String buildOptions = getBuildOptions();
		if(buildOptions != null) {
			return buildOptions;
		}
		return getDefaultBuildOptions();
	}
	
	public String getDefaultBuildOptions() throws CommonException, CoreException {
		return getBuildType().getDefaultBuildOptions(this);
	}
	
	/* -----------------  ----------------- */
	
	public String getArtifactPath() throws CommonException, CoreException {
		return getBuildType().getArtifactPath(this);
	}
	
	public Path getValidArtifactPath(String artifactPathStr) throws CommonException {
		if(artifactPathStr == null || artifactPathStr.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		return PathUtil.createPath(artifactPathStr);
	}
	
	public Path getValidArtifactPath3(String artifactPathOverride) throws CommonException, CoreException {
		String effectiveArtifactPath = artifactPathOverride != null ? artifactPathOverride : getArtifactPath();
		return getValidArtifactPath(effectiveArtifactPath);
	}
	
}