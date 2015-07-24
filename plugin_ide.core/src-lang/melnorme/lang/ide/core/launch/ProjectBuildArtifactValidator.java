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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTargetRunner;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.ValidationMessages;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ProjectBuildArtifactValidator extends AbstractValidator2 {
	
	protected final ProjectBuildExecutableSettings settings;
	
	public ProjectBuildArtifactValidator(ProjectBuildExecutableSettings settings) {
		this.settings = settings;
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	public static interface ProjectBuildExecutableSettings {
		
		public abstract String getProject_Attribute() throws CoreException;
		public abstract String getExecutablePath_Attribute() throws CoreException;
		public abstract String getBuildTarget_Attribute() throws CoreException;
		
	}
	
	/* -----------------  ----------------- */
	
	public IProject getProject() throws StatusException, CoreException {
		return getProject(settings.getProject_Attribute());
	}
	
	public IProject getProject(String projectName) throws StatusException, CoreException {
		return getProjectValidator().getProject(projectName);
	}
	
	protected ProjectValidator getProjectValidator() {
		return new ProjectValidator();
	}
	
	/* -----------------  ----------------- */
	
	public BuildTargetValidator getBuildTargetValidator() {
		return new BuildTargetValidator(false);
	}
	
	public BuildTarget getBuildTarget() throws CoreException, CommonException {
		return getBuildTargetValidator().getBuildTarget(getProject(), settings.getBuildTarget_Attribute());
	}

	public BuildTarget getBuildTarget_NonNull() throws CoreException, CommonException {
		return getBuildTargetValidator().getBuildTarget_nonNull(getProject(), settings.getBuildTarget_Attribute());
	}
	
	/* -----------------  ----------------- */ 
	
	protected Path getValidExecutableFilePath2() throws CoreException, CommonException {
		BuildTarget buildTarget = getBuildTarget_NonNull();
		BuildTargetRunner buildTargetOperation = getBuildManager().getBuildTargetOperation(getProject(), buildTarget);
		
		String exePathString = settings.getExecutablePath_Attribute();
		if(exePathString != null) {
			return buildTargetOperation.getValidArtifactPath3(exePathString);
		}
		return buildTargetOperation.getValidArtifactPath3();
	}
	
	public Location toAbsolute(Path exePath) throws StatusException, CoreException {
		if(exePath.isAbsolute()) {
			return Location.fromAbsolutePath(exePath);
		}
		// Otherwise path is relative to project location
		return ResourceUtils.loc(getProject().getLocation()).resolve(exePath);
	}
	
	public Location getValidExecutableFileLocation() throws CoreException, CommonException {
		Location location = toAbsolute(getValidExecutableFilePath2());
		if(location.toFile().exists() && !location.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(location));
		}
		return location;
	}
	
}