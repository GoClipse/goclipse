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
import melnorme.lang.ide.core.launch.BuildTargetLaunchCreator;
import melnorme.lang.ide.core.launch.BuildTargetSource;
import melnorme.lang.ide.core.launch.CompositeBuildTargetSettings;
import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.operations.build.ProjectBuildInfo;
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
			this::getDefaultBuildTargetArguments,
			this::getDefaultProgramPath
		) {
			{ createEnablementFields = false; }
		};
		component.programPathField.setLabelText(LangUIMessages.LaunchTab_ProgramPathField_title);
		
		component.buildArgumentsField.getUseDefaultField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		component.programPathField.getUseDefaultField().setLabelText(
			LangUIMessages.LaunchTab_Fields_useBuildTargetSettings);
		return component;
	}
	
	/* -----------------  ----------------- */
	
	protected final BuildTargetSource buildTargetSource = new BuildTargetSource() {
		@Override
		public String getProjectName() {
			return MainLaunchConfigurationTab.this.getProjectName();
		}
		
		@Override
		public String getBuildTargetName() {
			return MainLaunchConfigurationTab.this.getBuildTargetName();
		}
	};
	
	protected String getDefaultBuildTargetArguments() throws CommonException {
		return buildTargetSource.getBuildTarget().getEffectiveBuildArguments();
	}
	
	protected String getDefaultProgramPath() throws CommonException {
		return buildTargetSource.getBuildTarget().getEffectiveValidExecutablePath();
	}
	
	protected CompositeBuildTargetSettings getBuildTargetSettings() throws CommonException {
		return new CompositeBuildTargetSettings(buildTargetSource) {
			@Override
			public String getBuildArguments() {
				return buildTargetSettings.getEffectiveBuildArgumentsValue();
			}
			
			@Override
			public String getExecutablePath() {
				return buildTargetSettings.getEffectiveProgramPathValue();
			}
		};
	}
	
	@Override
	protected void doValidate() throws CommonException, CoreException {
		getBuildTargetSettings().getValidBuildTarget().validateForBuild();
	}
	
	/* ----------------- bindings ----------------- */
	
	protected void initBindings() {
		projectField.addListener(this::projectFieldChanged);
		buildTargetField.addListener(this::buildTargetFieldChanged);
		
		buildTargetSettings.buildArgumentsField.addListener(() -> updateLaunchConfigurationDialog());
		buildTargetSettings.buildArgumentsField.addListener(() -> 
			buildTargetSettings.programPathField.updateDefaultFieldValue());
		buildTargetSettings.programPathField.addListener(() -> updateLaunchConfigurationDialog());
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
		return new BuildTargetLaunchCreator();
	}
	
	@Override
	protected BuildTargetLaunchCreator doInitializeFrom_createSettings(ILaunchConfiguration config) 
			throws CoreException {
		return new BuildTargetLaunchCreator(config);
	}
	
	@Override
	public void doInitializeFrom(ILaunchConfiguration config) throws CoreException {
		BuildTargetLaunchCreator buildSettings = doInitializeFrom_createSettings(config);
		
		super.initializeFrom(buildSettings);
		initializeBuildTargetField(buildSettings);
		initializeBuildTargetSettings(buildSettings);
	}
	
	protected void initializeBuildTargetField(BuildTargetLaunchCreator btLaunchCreator) {
		buildTargetField.setFieldValue(btLaunchCreator.getTargetName());
	}
	
	protected void initializeBuildTargetSettings(BuildTargetLaunchCreator btLaunchCreator) {
		buildTargetSettings.buildArgumentsField.setEffectiveFieldValue(btLaunchCreator.getBuildArguments());
		buildTargetSettings.programPathField.setEffectiveFieldValue(btLaunchCreator.getExecutablePath());
	}
	
	@Override
	protected ProjectLaunchSettings getLaunchSettingsFromTab() {
		return new BuildTargetLaunchCreator(
			getProjectName(),
			getBuildTargetName(),
			buildTargetSettings.buildArgumentsField.getEffectiveFieldValue(),
			buildTargetSettings.programPathField.getEffectiveFieldValue()
		);
	}
	
}