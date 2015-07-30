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
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public class BuildTargetLaunchSettings extends ProjectLaunchSettings {
	
	public String buildTargetName = null;
	public boolean isDefaultProgramPath = true;
	public String programPath = "";

	public BuildTargetLaunchSettings() {
	}
	
	public BuildTargetLaunchSettings(String projectName, String buildTargetName, 
			boolean isDefaultProgramPath, String programPath) {
		super(projectName);
		this.buildTargetName = buildTargetName;
		this.isDefaultProgramPath = isDefaultProgramPath;
		this.programPath = programPath;
	}
	
	public BuildTargetLaunchSettings(ILaunchConfiguration config) throws CoreException {
		super(config);
		
		buildTargetName = config.getAttribute(LaunchConstants.ATTR_BUILD_TARGET, "");
		isDefaultProgramPath = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, true);
		programPath = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, "");
	}
	
	public String getEffectiveProgramPath() {
		return isDefaultProgramPath ? null : programPath;
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	@Override
	public BuildTargetLaunchSettings initFromProject(IProject project) throws CommonException {
		super.initFromProject(project);
		
		ProjectBuildInfo buildInfo = getBuildManager().getBuildInfo(project);
		if(buildInfo == null) {
			return null;
		}
		
		BuildTarget defaultBuildTarget = buildInfo.getDefaultBuildTarget();
		buildTargetName = defaultBuildTarget.getTargetName(); 
		isDefaultProgramPath = true;
		programPath  = null;
		
		return this;
	}
	
	@Override
	protected String getSuggestedConfigName_do() {
		return StringUtil.nullAsEmpty(projectName) + 
				StringUtil.prefixStr(" - ", StringUtil.emptyAsNull(buildTargetName));
	}
	
	@Override
	protected void saveToConfig_rest(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(LaunchConstants.ATTR_BUILD_TARGET, buildTargetName);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, isDefaultProgramPath);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, programPath);
	}
	
}