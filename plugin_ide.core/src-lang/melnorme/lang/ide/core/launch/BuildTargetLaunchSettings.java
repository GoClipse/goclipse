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
	public boolean buildArguments_isDefault = true;
	public String buildArguments = null;
	public boolean programPath_isDefault = true;
	public String programPath = null;

	public BuildTargetLaunchSettings() {
	}
	
	public BuildTargetLaunchSettings(String projectName, 
			String buildTargetName, String buildArguments, String programPath) {
		super(projectName);
		this.buildTargetName = buildTargetName;
		this.buildArguments = buildArguments;
		this.buildArguments_isDefault = buildArguments == null;
		this.programPath = programPath;
		this.programPath_isDefault = programPath == null;
	}
	
	public BuildTargetLaunchSettings(ILaunchConfiguration config) throws CoreException {
		super(config);
		
		buildTargetName = config.getAttribute(LaunchConstants.ATTR_BUILD_TARGET, "");
		programPath_isDefault = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, true);
		programPath = config.getAttribute(LaunchConstants.ATTR_PROGRAM_PATH, "");
		buildArguments_isDefault = config.getAttribute(LaunchConstants.ATTR_BUILD_ARGUMENTS_USE_DEFAULT, true);
		buildArguments = config.getAttribute(LaunchConstants.ATTR_BUILD_ARGUMENTS, "");
	}
	
	public String getEffectiveBuildArguments() {
		return buildArguments_isDefault ? null : buildArguments;
	}
	
	public String getEffectiveProgramPath() {
		return programPath_isDefault ? null : programPath;
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
		programPath_isDefault = true;
		programPath = null;
		buildArguments_isDefault = true;
		buildArguments = null;
		
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
		config.setAttribute(LaunchConstants.ATTR_BUILD_ARGUMENTS_USE_DEFAULT, buildArguments_isDefault);
		config.setAttribute(LaunchConstants.ATTR_BUILD_ARGUMENTS, buildArguments);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, programPath_isDefault);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, programPath);
	}
	
}