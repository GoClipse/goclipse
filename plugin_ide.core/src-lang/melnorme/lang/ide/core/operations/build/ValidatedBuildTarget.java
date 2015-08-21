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
import melnorme.lang.ide.core.launch.LaunchMessages;
import melnorme.lang.ide.core.launch.LaunchUtils;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.data.AbstractValidator2;
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
	
	public String[] getEffectiveEvaluatedBuildArguments() throws CommonException {
		return LaunchUtils.getEvaluatedArguments(getEffectiveBuildArguments());
	}
	
	/* -----------------  ----------------- */
	
	protected String getExecutablePath() {
		return buildTarget.getExecutablePath();
	}
	
	public String getEffectiveValidExecutablePath() throws CommonException {
		String executablePath = getExecutablePath();
		if(executablePath != null) {
			return executablePath;
		}
		
		return getDefaultExecutablePath();
	}
	
	public String getDefaultExecutablePath() throws CommonException {
		Indexable<LaunchArtifact> launchArtifacts = getLaunchArtifacts();
		if(launchArtifacts.size() > 1) {
			throw new CommonException(LaunchMessages.MSG_BuildTarget_MultipleExecutablesAvailable());
		}
		if(launchArtifacts.size() == 0) {
			throw new CommonException(LaunchMessages.MSG_BuildTarget_NoExecutablesAvailable());
		}
		return launchArtifacts.get(0).getArtifactPath();
	}
	
	public Indexable<LaunchArtifact> getLaunchArtifacts() throws CommonException {
		return getBuildType().getLaunchArtifacts(this);
	}
	
}