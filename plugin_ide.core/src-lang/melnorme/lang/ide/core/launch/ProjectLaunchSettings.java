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

import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.misc.StringUtil.nullAsEmpty;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.tooling.data.StatusException;
import melnorme.utilbox.core.CommonException;

public class ProjectLaunchSettings implements ILaunchConfigSerializer {
	
	public ProjectValidator projectValidator = new ProjectValidator(LangCore.NATURE_ID);
	public String projectName = "";
	
	public ProjectLaunchSettings() {
	}
	
	public ProjectLaunchSettings(String projectName) {
		this.projectName = projectName;
	}
	
	public ProjectLaunchSettings(ILaunchConfiguration config) throws CoreException {
		projectName = config.getAttribute(LaunchConstants.ATTR_PROJECT_NAME, "");
	}
	
	public IProject getProject() {
		if(projectName == null || projectName.isEmpty()) {
			return null;
		}
		return ResourceUtils.getProject(projectName);
	}
	
	@Override
	public ProjectLaunchSettings initFrom(IResource contextualResource) {
		if(contextualResource == null) {
			return this;
		}
		IProject project = contextualResource.getProject();
		IProject validProject;
		try {
			validProject = projectValidator.getProject(project.getName());
		} catch(StatusException e) {
			return this;
		}
		try {
			initFromProject(validProject);
		} catch(CommonException e) {
			LangCore.logError("Error initializing launch settings from context resource: ", e);
		}
		
		return this;
	}
	
	public ProjectLaunchSettings initFromProject(IProject project) throws CommonException {
		projectName = project.getName();
		return this;
	}
	
	protected ILaunchManager getLaunchManager() {
		return DebugPlugin.getDefault().getLaunchManager();
	}
	
	public ILaunchConfiguration createNewConfiguration(ILaunchConfigurationType launchCfgType) 
			throws CoreException, CommonException {
		String suggestedName = getSuggestedConfigName();
		
		String launchName = getLaunchManager().generateLaunchConfigurationName(suggestedName);
		ILaunchConfigurationWorkingCopy wc = launchCfgType.newInstance(null, launchName);
		
		saveToConfig(wc);
		return wc.doSave();
	}
	
	public final String getSuggestedConfigName() {
		// Suggested name should be trimmed, because it will be used as a file name
		String suggestedName = nullAsEmpty(getSuggestedConfigName_do()).trim();
		if(suggestedName.isEmpty()) {
			return "New Launch";
		}
		return suggestedName;
	}

	protected String getSuggestedConfigName_do() {
		return nullAsEmpty(projectName);
	}
	
	@Override
	public void saveToConfig(ILaunchConfigurationWorkingCopy config, boolean rename) throws CommonException {
		config.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, projectName);
		
		config.setMappedResources(array(getProject()));
		
		if(rename) {
			String suggestedName = getSuggestedConfigName();
			String newName = getLaunchManager().generateLaunchConfigurationName(suggestedName);
			config.rename(newName);
		}
		
		saveToConfig_rest(config);
	}
	
	@SuppressWarnings("unused")
	protected void saveToConfig_rest(ILaunchConfigurationWorkingCopy config) throws CommonException {
	}
	
}