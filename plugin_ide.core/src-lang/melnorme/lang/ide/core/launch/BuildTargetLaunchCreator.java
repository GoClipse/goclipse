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
import melnorme.lang.ide.core.operations.build.BuildTargetData;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.tooling.bundle.BuildTargetNameParser;
import melnorme.lang.tooling.commands.CommandInvocation;
import melnorme.lang.tooling.commands.CommandInvocationSerializer;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;

public class BuildTargetLaunchCreator extends ProjectLaunchSettings {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	protected final CommandInvocationSerializer commandInvocationSerializer = new CommandInvocationSerializer();
	
	public BuildTargetData data = new BuildTargetData();
	public String launchNameSuffixSuggestion;
	
	public BuildTargetLaunchCreator() {
	}
	
	public BuildTargetLaunchCreator(String projectName, BuildTargetData data) {
		super(projectName);
		this.data = data;
	}
	
	public BuildTargetLaunchCreator(String projectName, BuildTargetData data, String launchNameSuffixSuggestion) {
		this(projectName, data);
		this.launchNameSuffixSuggestion = launchNameSuffixSuggestion;
	}
	
	public BuildTargetLaunchCreator(ILaunchConfiguration config) throws CoreException, CommonException {
		super(config);
		
		data.targetName = config.getAttribute(LaunchConstants.ATTR_BUILD_TARGET, "");
		data.executablePath = LaunchUtils.getOptionalAttribute(config, 
			LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, LaunchConstants.ATTR_PROGRAM_PATH);
		data.buildCommand = commandInvocationSerializer.readFromString(
			LaunchUtils.getOptionalAttribute(config, 
				LaunchConstants.ATTR_BUILD_COMMAND_USE_DEFAULT, LaunchConstants.ATTR_BUILD_COMMAND));
	}
	
	public String getTargetName() {
		return data.targetName;
	}
	
	public CommandInvocation getBuildCommand() {
		return data.buildCommand;
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
		data.buildCommand = null;
		data.executablePath = null;
		
		return this;
	}
	
	@Override
	protected String getSuggestedConfigName_do() {
		if(launchNameSuffixSuggestion != null) {
			return nullAsEmpty(projectName) + StringUtil.prefixStr(" - ", emptyAsNull(launchNameSuffixSuggestion));
		}
		
		String launchName = nullAsEmpty(projectName) + getSuggestedConfigName_targetSuffix();
		if(data.executablePath != null) {
			launchName += "["+data.executablePath+"]";	
		}
		return launchName;
	}
	
	protected String getSuggestedConfigName_targetSuffix() {
		if(emptyAsNull(data.targetName) == null) {
			return "";
		}
		String suggestedLabel = getSuggestedLabelForBuildTarget(data.targetName);
		if(emptyAsNull(suggestedLabel) == null) {
			return "";
		}
		return " - " + suggestedLabel;
	}
	
	/** @return the preferred label for given build target name. Can be null */
	protected String getSuggestedLabelForBuildTarget(String buildTargetName) {
		BuildTargetNameParser nameParser = buildManager.getBuildTargetNameParser();
		// Let the label be only the build config part, and ignore build type:  
		// for most languages that's what will make most sense.
		
		return nameParser.getBuildConfig(buildTargetName);
	}
	
	@Override
	protected void saveToConfig_rest(ILaunchConfigurationWorkingCopy config) throws CommonException {
		String serializedBuildCommand = commandInvocationSerializer.writeToString(data.buildCommand);
		
		config.setAttribute(LaunchConstants.ATTR_BUILD_TARGET, getTargetName());
		LaunchUtils.setOptionalValue(config, 
			LaunchConstants.ATTR_BUILD_COMMAND_USE_DEFAULT, 
			LaunchConstants.ATTR_BUILD_COMMAND, serializedBuildCommand);
		LaunchUtils.setOptionalValue(config, 
			LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, 
			LaunchConstants.ATTR_PROGRAM_PATH, data.executablePath);
	}
	
}