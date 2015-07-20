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


import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.ValidationMessages;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.PathUtil;

public abstract class ProjectBuildExecutableFileValidator extends AbstractValidator2 {
	
	public ProjectBuildExecutableFileValidator() {
		super();
	}
	
	protected abstract String getProject_Attribute() throws CoreException;
	protected abstract String getExecutablePath_Attribute() throws CoreException;
	protected abstract String getBuildTarget_Attribute() throws CoreException;

	
	/* -----------------  ----------------- */
	
	public IProject getProject() throws StatusException, CoreException {
		return getProjectValidator().getProject(getProject_Attribute());
	}
	
	protected ProjectValidator getProjectValidator() {
		return new ProjectValidator();
	}
	
	/* -----------------  ----------------- */
	
	public BuildTarget getBuildTarget() throws CoreException, CommonException {
		return new BuildTargetValidator().getBuildTarget(getProject(), getBuildTarget_Attribute());
	}
	
	public BuildTarget getBuildTarget_NonNull() throws CoreException, CommonException {
		return new BuildTargetValidator().getBuildTarget_nonNull(getProject(), getBuildTarget_Attribute());
	}
	
	public Path getExecutableFilePath(BuildTarget buildTarget) throws CoreException, CommonException {
		String exePathString = getExecutablePath_Attribute();
		if(exePathString != null) {
			return PathUtil.createPath(exePathString);
		}
		
		Path artifactPath = null;
		if(buildTarget != null) {
			artifactPath = buildTarget.getBuildConfig().getArtifactPath();
		}
		if(artifactPath == null) {
			throw error(LaunchMessages.PROCESS_LAUNCH_CouldNotDetermineExeLocation);
		} else {
			return artifactPath;
		}
	}
	
	public Location getExecutableFileLocation(BuildTarget buildTarget) throws CoreException, CommonException {
		Path exePath = getExecutableFilePath(buildTarget);
		
		if(exePath.isAbsolute()) {
			return Location.fromAbsolutePath(exePath);
		}
		// Otherwise path is relative to project location
		return ResourceUtils.loc(getProject().getLocation()).resolve(exePath);
	}
	
	public Location getValidExecutableFileLocation() throws CoreException, CommonException {
		Location location = getExecutableFileLocation(getBuildTarget());
		if(!location.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(location));
		}
		return location;
	}
	
}