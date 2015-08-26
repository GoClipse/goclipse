/*******************************************************************************
 * Copyright (c) 2013, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 		DLTK team -
 * 		Bruno Medeiros - rewrote class to remove DLTK dependencies and cleanup/simplify code. 
 *******************************************************************************/
package melnorme.lang.ide.ui.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.launch.BuildTargetLaunchSettings;
import melnorme.lang.ide.core.launch.BuildTargetValidator;
import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.preferences.BuildTargetSettingsComponent;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

/**
 * A main LaunchConfigurationTa with project selection field, and program path selection field.
 */
public abstract class MainLaunchConfigurationTab extends ProjectBasedLaunchConfigurationTab {
	
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final BuildTargetSettingsComponent buildTargetSettings = init_BuildTargetSettingsComponent();
	
	public MainLaunchConfigurationTab() {
		this(true);
	}
	
	public MainLaunchConfigurationTab(boolean initialize) {
		if(initialize) {
			initBindings();
		}
	}
	
	protected BuildTargetField init_createBuildTargetField() {
		return new BuildTargetField();
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected String getProgramPathString() {
		return buildTargetSettings.programPathField.getFieldValue();
	}
	
	protected String getBuildTargetName() {
		return buildTargetField.getFieldValue();
	}
	
	protected boolean isDefaultProgramPath() {
		return buildTargetSettings.programPathField.isUseDefault();
	}
	
	protected BuildTargetSettingsComponent init_BuildTargetSettingsComponent() {
		BuildTargetSettingsComponent component = new BuildTargetSettingsComponent(
			this::getDefaultBuildTargetArguments, this::getDefaultProgramPath);
		component.programPathField.setLabelText(LangUIMessages.LaunchTab_ProgramPathField_title);
		
		component.buildArgumentsField.getUseDefaultField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		component.programPathField.getUseDefaultField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		return component;
	}
	
	/* -----------------  ----------------- */
	
	protected String getDefaultBuildTargetArguments() throws CommonException {
		return getValidator().getOriginalBuildArguments();
	}
	
	protected String getDefaultProgramPath() throws CommonException {
		return getValidator().getOriginalExecutablePath();
	}
	
	protected BuildTargetValidator getValidator() throws CommonException {
		return new BuildTargetValidator() {
			
			@Override
			public String getProjectName() throws CommonException {
				return MainLaunchConfigurationTab.this.getProjectName();
			}
			
			@Override
			public String getBuildTargetName() {
				return MainLaunchConfigurationTab.this.getBuildTargetName();
			}
			
			@Override
			public String getBuildArguments() {
				return buildTargetSettings.getEffectiveArgumentsValue();
			}
			
			@Override
			public String getExecutablePath() {
				return buildTargetSettings.getEffectiveProgramPathValue();
			}
			
		};
	}
	
	@Override
	protected void doValidate() throws CommonException, CoreException {
		getValidator().getValidBuildTarget();
		getValidator().getEffectiveBuildArguments();
		getValidator().getValidExecutableLocation();
	}
	
	/* ----------------- bindings ----------------- */
	
	protected void initBindings() {
		projectField.addValueChangedListener(this::projectFieldChanged);
		buildTargetField.addValueChangedListener(this::buildTargetFieldChanged);
		
		buildTargetSettings.buildArgumentsField.addValueChangedListener(() -> updateLaunchConfigurationDialog());
		buildTargetSettings.buildArgumentsField.addValueChangedListener(() -> 
			buildTargetSettings.programPathField.updateDefaultFieldValue());
		buildTargetSettings.programPathField.addValueChangedListener(() -> updateLaunchConfigurationDialog());
	}
	
	public void projectFieldChanged() {
		IProject project = getValidProjectOrNull();
		if(project != null) {
			ProjectBuildInfo buildInfo = getBuildManager().getBuildInfo(project);
			if(buildInfo != null) {
				
				Collection2<BuildTarget> buildTargets = buildInfo.getBuildTargets();
				buildTargetField.setFieldOptions(
					buildTargets.map((buildTarget) -> buildTarget.getTargetName()));
			}
		}
		
		updateLaunchConfigurationDialog();
	}
	
	public void buildTargetFieldChanged() {
		IProject project = getValidProjectOrNull();
		if(project != null) {
			buildTargetSettings.buildArgumentsField.updateDefaultFieldValue();
			buildTargetSettings.programPathField.updateDefaultFieldValue();
			updateLaunchConfigurationDialog();
		}
	}
	
	
	/* ----------------- Control creation ----------------- */
	
	@Override
	protected void createCustomControls(Composite parent) {
		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		buildTargetSettings.createComponent(parent, new GridData(GridData.FILL_BOTH));
	}
	
	/* ----------------- Apply/Revert/Defaults ----------------- */
	
	@Override
	protected ProjectLaunchSettings getDefaultProjectLaunchSettings() {
		return new BuildTargetLaunchSettings();
	}
	
	@Override
	public void doInitializeFrom(ILaunchConfiguration config) throws CoreException {
		BuildTargetLaunchSettings buildSettings = new BuildTargetLaunchSettings(config);
		
		super.initializeFrom(buildSettings);
		initializeBuildTargetField(buildSettings);
		initializeBuildTargetSettings(buildSettings);
	}
	
	protected void initializeBuildTargetField(BuildTargetLaunchSettings buildSettings) {
		buildTargetField.setFieldValue(buildSettings.getTargetName());
	}
	
	protected void initializeBuildTargetSettings(BuildTargetLaunchSettings buildSettings) {
		buildTargetSettings.buildArgumentsField.setEffectiveFieldValue(buildSettings.getBuildArguments());
		buildTargetSettings.programPathField.setEffectiveFieldValue(buildSettings.getExecutablePath());
	}
	
	@Override
	protected ProjectLaunchSettings getLaunchSettingsFromTab() {
		return new BuildTargetLaunchSettings(
			getProjectName(),
			getBuildTargetName(),
			buildTargetSettings.buildArgumentsField.getEffectiveFieldValue(),
			buildTargetSettings.programPathField.getEffectiveFieldValue()
		);
	}
	
}