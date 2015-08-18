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

import org.eclipse.core.resources.IProject;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.utilbox.core.CommonException;

public class ValidatedBuildTarget extends AbstractValidator2 {
	
	public final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final BuildTarget buildTarget;
	protected final BuildConfiguration buildConfiguration;
	protected final BuildType buildType;
	
	public ValidatedBuildTarget(IProject project, BuildTarget buildTarget) throws CommonException {
		this.project = project;
		this.buildTarget = buildTarget;
		
		String targetName = buildTarget.getTargetName();
		BuildTargetNameParser nameParser = buildMgr.getBuildTargetNameParser();
		this.buildConfiguration = buildMgr.getValidBuildConfiguration(project, nameParser.getBuildConfig(targetName));
		this.buildType = buildMgr.getBuildType_NonNull(nameParser.getBuildType(targetName));
	}
	
	public BuildManager getBuildManager() {
		return buildMgr;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getBuildConfigName() {
		return buildConfiguration.getName();
	}
	
	public String getBuildTypeName() {
		return buildType.getName();
	}
	
	public BuildConfiguration getBuildConfiguration() {
		return buildConfiguration;
	}
	
	public BuildType getBuildType() {
		return buildType;
	}
	
	/* -----------------  ----------------- */
	
	protected String getBuildArguments2() {
		return buildTarget.getBuildArguments();
	}
	
	public String getEffectiveBuildArguments() throws CommonException {
		String buildOptions = getBuildArguments2();
		if(buildOptions != null) {
			return buildOptions;
		}
		return getDefaultBuildArguments();
	}
	
	public String getDefaultBuildArguments() throws CommonException {
		return getBuildType().getDefaultBuildOptions(this);
	}
	
	/* -----------------  ----------------- */
	
	protected String getArtifactPath2() {
		return buildTarget.getArtifactPath();
	}
	
	public String getEffectiveArtifactPath() throws CommonException {
		String artifactPath = getArtifactPath2();
		if(artifactPath != null) {
			return artifactPath;
		}
		return getDefaultArtifactPath();
	}
	
	public String getDefaultArtifactPath() throws CommonException {
		return getBuildType().getArtifactPath(this);
	}
	
}