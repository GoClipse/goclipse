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
package melnorme.lang.ide.core.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ValidatedBuildTarget;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.ValidationMessages;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

/**
 * This is similar in nature to {@link BuildTargetValidator}, maybe these two could be merged?
 *
 */
public abstract class BuildTargetSettingsValidator extends AbstractValidator2 
	implements IBuildTargetSettings {
	
	public BuildTargetSettingsValidator() {
	}
	
	public ProjectValidator getProjectValidator() {
		return new ProjectValidator(LangCore.NATURE_ID);
	}
	
	public IProject getValidProject() throws CommonException {
		return getProjectValidator().getProject(getProjectName());
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected Location getProjectLocation() throws CommonException {
		return ResourceUtils.loc(getValidProject().getLocation());
	}
	
	/* -----------------  ----------------- */
	
	public BuildTarget getValidBuildTarget() throws CommonException {
		BuildTarget originalBuildTarget = getBuildManager().getValidBuildTarget(
			getValidProject(), getBuildTargetName(), false, true);
		
		BuildTargetData data = originalBuildTarget.getDataCopy();
		if(getBuildArguments() != null) {
			data.buildArguments = getBuildArguments();
		}
		if(getArtifactPath() != null) {
			data.artifactPath = getArtifactPath();
		}
		return getBuildManager().createBuildTarget(data);
	}
	
	/* -----------------  ----------------- */ 
	
	public String getDefaultArtifactPath2() throws CommonException {
		return getValidatedBuildTarget().getDefaultArtifactPath();
	}
	
	protected ValidatedBuildTarget getValidatedBuildTarget() throws CommonException {
		BuildTarget buildTarget = getValidBuildTarget();
		return getBuildManager().getValidatedBuildTarget(getValidProject(), buildTarget);
	}
	
	public Location getValidExecutableLocation() throws CoreException, CommonException {
		return getValidExecutableLocation(getValidatedBuildTarget().getEffectiveArtifactPath());
	}
	
	public Location getValidExecutableLocation(String exeFilePathString) throws CommonException, CoreException {
		if(exeFilePathString == null || exeFilePathString.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		
		Location exeFileLocation = Location.create(getProjectLocation(), exeFilePathString);
		
		if(exeFileLocation.toFile().exists() && !exeFileLocation.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(exeFileLocation));
		}
		return exeFileLocation;
	}
	
	/* -----------------  ----------------- */
	
	public String getDefaultBuildArguments() throws CommonException {
		return getValidatedBuildTarget().getDefaultBuildArguments();
	}
	
	public String getEffectiveBuildArguments() throws CommonException {
		return getValidatedBuildTarget().getEffectiveBuildArguments();
	}
	
}