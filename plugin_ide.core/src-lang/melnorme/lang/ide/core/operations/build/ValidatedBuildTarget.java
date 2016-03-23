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
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.lang.tooling.bundle.LaunchArtifact;
import melnorme.lang.tooling.data.AbstractValidator;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ValidatedBuildTarget extends AbstractValidator {
	
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
	
	public Location getProjectLocation() throws CommonException {
		return ResourceUtils.getProjectLocation2(project);
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
	
	public boolean isDefaultBuildType() {
		return getBuildType() == buildMgr.getDefaultBuildType();
	}
	
	/* -----------------  ----------------- */
	
	public ProjectBuildInfo getBuildInfo() {
		return buildInfo;
	}
	
	public BundleInfo getBundleInfo() {
		return getBuildInfo().getBundleInfo();
	}
	
	/* -----------------  ----------------- */
	
	protected String getBuildArguments() {
		return buildTarget.getBuildArguments();
	}
	
	public String getEffectiveBuildArguments() throws CommonException {
		String buildOptions = getBuildArguments();
		if(buildOptions != null) {
			return buildOptions;
		}
		return getDefaultBuildArguments();
	}
	
	public String getDefaultBuildArguments() throws CommonException {
		return getBuildType().getDefaultBuildOptions(this);
	}
	
	public String getDefaultCheckArguments() throws CommonException {
		// FIXME: TO DO
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
		LaunchArtifact mainLaunchArtifact = getMainLaunchArtifact();
		if(mainLaunchArtifact == null) {
			throw new CommonException(LaunchMessages.MSG_BuildTarget_NoExecutableAvailable());
		}
		return mainLaunchArtifact.getArtifactPath();
	}
	
	public Indexable<LaunchArtifact> getSubLaunchArtifacts() throws CommonException {
		return getBuildType().getSubTargetLaunchArtifacts(this);
	}
	
	public LaunchArtifact getMainLaunchArtifact() throws CommonException {
		return getBuildType().getMainLaunchArtifact(this);
	}
	
}