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
		BuildTarget originalBuildTarget = getOriginalBuildTarget();
		
		BuildTargetData data = originalBuildTarget.getDataCopy();
		if(getBuildArguments() != null) {
			data.buildArguments = getBuildArguments();
		}
		if(getExecutablePath() != null) {
			data.executablePath = getExecutablePath();
		}
		return getBuildManager().createBuildTarget(data);
	}
	
	protected BuildTarget getOriginalBuildTarget() throws CommonException {
		return getBuildManager().getValidBuildTarget(
			getValidProject(), getBuildTargetName(), false, true);
	}
	
	protected ValidatedBuildTarget getValidatedBuildTarget() throws CommonException {
		return getBuildManager().getValidatedBuildTarget(getValidProject(), getValidBuildTarget());
	}
	
	protected ValidatedBuildTarget getValidatedOriginalBuildTarget() throws CommonException {
		return getBuildManager().getValidatedBuildTarget(getValidProject(), getOriginalBuildTarget());
	}
	
	/* -----------------  ----------------- */
	
	public String getOriginalBuildArguments() throws CommonException {
		return getValidatedOriginalBuildTarget().getEffectiveBuildArguments();
	}
	
	public String getOriginalExecutablePath() throws CommonException {
		return getValidatedOriginalBuildTarget().getEffectiveValidExecutablePath();
	}
	
	public String getDefaultBuildArguments2() throws CommonException {
		return getValidatedOriginalBuildTarget().getDefaultBuildArguments();
	}
	
	public String getDefaultExecutablePath2() throws CommonException {
		return getValidatedOriginalBuildTarget().getDefaultExecutablePath();
	}
	
	/* -----------------  ----------------- */ 
	
	public String getEffectiveBuildArguments() throws CommonException {
		return getValidatedBuildTarget().getEffectiveBuildArguments();
	}
	
	public Location getValidExecutableLocation() throws CommonException {
		return getValidExecutableLocation(getValidatedBuildTarget().getEffectiveValidExecutablePath());
	}
	
	/* -----------------  ----------------- */ 
	
	public Location getValidExecutableLocation(String exeFilePathString) throws CommonException {
		if(exeFilePathString == null || exeFilePathString.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		
		Location exeFileLocation = Location.create(getProjectLocation(), exeFilePathString);
		
		if(exeFileLocation.toFile().exists() && !exeFileLocation.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(exeFileLocation));
		}
		return exeFileLocation;
	}
	
}