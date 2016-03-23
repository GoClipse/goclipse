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

import static melnorme.utilbox.misc.StringUtil.emptyAsNull;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.BuildTarget.BuildTargetData;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public class BuildTargetLaunchCreator extends ProjectLaunchSettings {
	
	public BuildTargetData data = new BuildTargetData();
	
	public BuildTargetLaunchCreator() {
	}
	
	public BuildTargetLaunchCreator(String projectName, 
			String targetName, String buildArguments, String executablePath) {
		this(projectName, new BuildTargetData(targetName, true, buildArguments, null, executablePath));
	}
	
	public BuildTargetLaunchCreator(String projectName, BuildTargetData data) {
		super(projectName);
		this.data = data;
	}
	
	public BuildTargetLaunchCreator(ILaunchConfiguration config) throws CoreException {
		super(config);
		
		data.targetName = config.getAttribute(LaunchConstants.ATTR_BUILD_TARGET, "");
		data.executablePath = LaunchUtils.getOptionalAttribute(config, 
			LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, LaunchConstants.ATTR_PROGRAM_PATH);
		data.buildArguments = LaunchUtils.getOptionalAttribute(config, 
			LaunchConstants.ATTR_BUILD_ARGUMENTS_USE_DEFAULT, LaunchConstants.ATTR_BUILD_ARGUMENTS);
	}
	
	public String getTargetName() {
		return data.targetName;
	}
	
	public String getBuildArguments() {
		return data.buildArguments;
	}
	
	public String getExecutablePath() {
		return data.executablePath;
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	@Override
	public BuildTargetLaunchCreator initFromProject(IProject project) throws CommonException {
		super.initFromProject(project);
		
		ProjectBuildInfo buildInfo = getBuildManager().getBuildInfo(project);
		if(buildInfo == null) {
			return null;
		}
		
		BuildTarget defaultBuildTarget = buildInfo.getDefaultBuildTarget();
		data.targetName = defaultBuildTarget.getTargetName(); 
		data.buildArguments = null;
		data.executablePath = null;
		
		return this;
	}
	
	@Override
	protected String getSuggestedConfigName_do() {
		String launchName = nullAsEmpty(projectName) + StringUtil.prefixStr(" - ", emptyAsNull(data.targetName));
		if(data.executablePath != null) {
			launchName += "["+data.executablePath+"]";	
		}
		return launchName;
	}
	
	@Override
	protected void saveToConfig_rest(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(LaunchConstants.ATTR_BUILD_TARGET, getTargetName());
		LaunchUtils.setOptionalValue(config, 
			LaunchConstants.ATTR_BUILD_ARGUMENTS_USE_DEFAULT, 
			LaunchConstants.ATTR_BUILD_ARGUMENTS, data.buildArguments);
		LaunchUtils.setOptionalValue(config, 
			LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, 
			LaunchConstants.ATTR_PROGRAM_PATH, data.executablePath);
	}
	
}