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
import melnorme.lang.ide.core.operations.build.BuildTargetValidator3;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.lang.tooling.data.ValidationMessages;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ProjectBuildArtifactValidator extends AbstractValidator2 {
	
	public static interface ProjectBuildExecutableSettings {
		
		public abstract String getProject_Attribute() throws CoreException;
		public abstract String getExecutablePath_Attribute() throws CoreException;
		public abstract String getBuildTarget_Attribute() throws CoreException;
		
	}
	
	/* -----------------  ----------------- */
	
	protected final ProjectBuildExecutableSettings settings;
	
	public ProjectBuildArtifactValidator(ProjectBuildExecutableSettings settings) {
		this.settings = settings;
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected String getProjectName() throws CoreException {
		return settings.getProject_Attribute();
	}
	
	/** @return BuildTargetName. Can be null */
	protected String getBuildTargetName() throws CoreException {
		return settings.getBuildTarget_Attribute();
	}
	
	/* -----------------  ----------------- */
	
	public IProject getValidProject() throws CommonException, CoreException {
		return getProjectValidator().getProject(getProjectName());
	}
	
	protected ProjectValidator getProjectValidator() {
		return new ProjectValidator();
	}
	
	/* -----------------  ----------------- */
	
	public BuildTarget getBuildTarget() throws CoreException, CommonException {
		if(getBuildTargetName() == null){
			return null;
		}
		return getValidBuildTarget();
	}
	
	public BuildTarget getValidBuildTarget() throws CoreException, CommonException {
		return getBuildManager().getValidBuildTarget(getValidProject(), getBuildTargetName(), false);
	}
	
	/* -----------------  ----------------- */ 
	
	protected Path getValidExecutableFilePath2() throws CoreException, CommonException {
		BuildTargetValidator3 buildTargetValidator = getBuildManager()
				.createBuildTargetValidator(getValidProject(), getValidBuildTarget());
		
		return buildTargetValidator.getValidArtifactPath3(settings.getExecutablePath_Attribute());
	}
	
	public Location toAbsolute(Path exePath) throws CommonException, CoreException {
		if(exePath.isAbsolute()) {
			return Location.fromAbsolutePath(exePath);
		}
		// Otherwise path is relative to project location
		return ResourceUtils.loc(getValidProject().getLocation()).resolve(exePath);
	}
	
	public Location getValidExecutableFileLocation() throws CoreException, CommonException {
		Location location = toAbsolute(getValidExecutableFilePath2());
		if(location.toFile().exists() && !location.toFile().isFile()) {
			error(ValidationMessages.Location_NotAFile(location));
		}
		return location;
	}
	
}