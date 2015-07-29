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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.ValidationMessages;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

public class LaunchExecutableValidator extends AbstractValidator2 {
	
	protected final IProject validProject;
	protected final String buildTargetName; // can be null
	protected final String artifactPathOverride; // can be null
	
	public LaunchExecutableValidator(ProjectValidator projectValidator, String projectName, 
			String buildTargetName, String artifactPathOverride)
			throws CommonException {
		this(projectValidator.getProject(projectName), buildTargetName, artifactPathOverride);
	}
	
	public LaunchExecutableValidator(IProject validProject, String buildTargetName, String artifactPathOverride) {
		this.validProject = assertNotNull(validProject);
		this.buildTargetName = buildTargetName;
		this.artifactPathOverride = artifactPathOverride;
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	/* -----------------  ----------------- */
	
	public BuildTarget getBuildTarget() throws CoreException, CommonException {
		if(buildTargetName == null){
			return null;
		}
		return getValidBuildTarget();
	}
	
	public BuildTarget getValidBuildTarget() throws CoreException, CommonException {
		return getBuildManager().getValidBuildTarget(validProject, buildTargetName, false);
	}
	
	/* -----------------  ----------------- */ 
	
	public String getDefaultArtifactPath() throws CommonException, CoreException {
		BuildTarget buildTarget = getBuildTarget();
		if(buildTarget == null) {
			return null;
		}
		return getBuildManager().createBuildTargetValidator(validProject, buildTarget).getArtifactPath();
	}
	
	public Location getValidExecutableLocation() throws CoreException, CommonException {
		String effectiveArtifactPath = artifactPathOverride != null ? artifactPathOverride : getDefaultArtifactPath();
		return getValidExecutableLocation(effectiveArtifactPath);
	}
	
	public Location getValidExecutableLocation(String exeFilePathString) throws CommonException, CoreException {
		if(exeFilePathString == null || exeFilePathString.isEmpty()) {
			throw new CommonException(LaunchMessages.BuildTarget_NoArtifactPathSpecified);
		}
		Path exeFilePath = PathUtil.createPath(exeFilePathString);
		
		Location exeFileLocation = toAbsolute(exeFilePath);
		if(exeFileLocation.toFile().exists() && !exeFileLocation.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(exeFileLocation));
		}
		return exeFileLocation;
	}
	
	public Location toAbsolute(Path exePath) throws CommonException, CoreException {
		if(exePath.isAbsolute()) {
			return Location.fromAbsolutePath(exePath);
		}
		// Otherwise path is relative to project location
		return ResourceUtils.loc(validProject.getLocation()).resolve(exePath);
	}
	
}