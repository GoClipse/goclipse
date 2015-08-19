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

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.BundleInfo;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public class ValidatedBuildTarget extends AbstractValidator2 {
	
	public final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final BuildTarget buildTarget;
	protected final BuildType buildType;
	protected final ProjectBuildInfo buildInfo;
	protected final BuildConfiguration buildConfiguration;
	
	public ValidatedBuildTarget(IProject project, BuildTarget buildTarget, BuildType buildType, 
			String buildConfigName) throws CommonException {
		this.project = project;
		this.buildTarget = buildTarget;
		this.buildType = assertNotNull(buildType);
		
		this.buildInfo = buildMgr.getValidBuildInfo(project);
		this.buildConfiguration = buildType.getValidBuildconfiguration(buildConfigName, buildInfo);
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
	
	public BuildTarget getBuildTarget() {
		return buildTarget;
	}
	
	/* -----------------  ----------------- */
	
	public ProjectBuildInfo getBuildInfo() {
		return buildInfo;
	}
	
	public BundleInfo getBundleInfo() {
		return getBuildInfo().getBundleInfo();
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
	
	public Indexable<String> getEffectiveArtifactPaths() throws CommonException {
		String artifactPath = getArtifactPath2();
		if(artifactPath != null) {
			return new ArrayList2<>(artifactPath);
		}
		return getDefaultArtifactPaths();
	}
	
	public Indexable<String> getDefaultArtifactPaths() throws CommonException {
		return getBuildType().getDefaultArtifactPaths(this);
	}
	
}