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
import melnorme.lang.ide.core.launch.ProjectLaunchSettings;
import melnorme.lang.ide.core.launch.LaunchExecutableValidator;
import melnorme.lang.ide.core.launch.BuildTargetLaunchSettings;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.fields.ProjectRelativePathField;
import melnorme.utilbox.core.CommonException;

/**
 * A main LaunchConfigurationTa with project selection field, and program path selection field.
 */
public abstract class MainLaunchConfigurationTab extends ProjectBasedLaunchConfigurationTab {
	
	protected final BuildTargetField buildTargetField = init_createBuildTargetField();
	protected final ProjectRelativePathField programPathField = init_createProgramPathField();
	
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
	protected ProjectRelativePathField init_createProgramPathField() {
		return new MainLaunchTab_ProgramPathField();
	}
	
	protected BuildManager getBuildManager() {
		return LangCore.getBuildManager();
	}
	
	protected String getProgramPathString() {
		return programPathField.getFieldValue();
	}
	
	protected String getBuildTargetName() {
		return buildTargetField.getFieldValue();
	}
	
	protected boolean isDefaultProgramPath() {
		return programPathField.isUseDefault();
	}
	
	protected class MainLaunchTab_ProgramPathField extends ProjectRelativePathField {
		public MainLaunchTab_ProgramPathField() {
			super(
				LangUIMessages.LaunchTab_ProgramPathField_title, 
				LangUIMessages.LaunchTab_ProgramPathField_useDefault, 
				MainLaunchConfigurationTab.this::validateProject
			);
		}
		
		@Override
		protected String getDefaultFieldValue() {
			try {
				return getValidator().getDefaultArtifactPath();
			} catch(CoreException | CommonException e) {
				return null;
			}
		}
	}
	
	@Override
	protected void doValidate() throws CommonException, CoreException {
		getValidator().getBuildTarget();
		getValidator().getValidExecutableLocation();
	}
	
	protected LaunchExecutableValidator getValidator() throws CommonException {
		return new MainLaunchTab_LaunchExecutableValidator();
	}
	
	protected class MainLaunchTab_LaunchExecutableValidator extends LaunchExecutableValidator {
		public MainLaunchTab_LaunchExecutableValidator() throws CommonException {
			super(
				MainLaunchConfigurationTab.this.getValidProject(),
				MainLaunchConfigurationTab.this.getBuildTargetName(),
				MainLaunchConfigurationTab.this.programPathField.getEffectiveFieldValue()
			);
		}
	}
	
	/* ----------------- Control creation ----------------- */
	
	@Override
	protected void createCustomControls(Composite parent) {
		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		programPathField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	/* ----------------- bindings ----------------- */
	
	protected void initBindings() {
		projectField.addValueChangedListener(this::projectFieldChanged);
		buildTargetField.addValueChangedListener(this::buildTargetFieldChanged);
		programPathField.addValueChangedListener(this::updateLaunchConfigurationDialog);
	}
	
	public void projectFieldChanged() {
		IProject project = getValidProjectOrNull();
		if(project != null) {
			ProjectBuildInfo buildInfo = getBuildManager().getBuildInfo(project);
			if(buildInfo != null) {
				
				buildTargetField.setFieldOptions(
					buildInfo.getBuildTargets().map((buildTarget) -> buildTarget.getTargetName()));
			}
		}
		
		updateLaunchConfigurationDialog();
	}
	
	public void buildTargetFieldChanged() {
		programPathField.updateDefaultFieldValue();
		updateLaunchConfigurationDialog();
	}
	
	/* ----------------- Apply/Revert/Defaults ----------------- */
	
	@Override
	protected ProjectLaunchSettings getDefaultProjectLaunchSettings() {
		return new BuildTargetLaunchSettings();
	}
	
	@Override
	public void doInitializeFrom(ILaunchConfiguration config) throws CoreException {
		BuildTargetLaunchSettings projectLaunchSettings = new BuildTargetLaunchSettings(config);
		
		super.initializeFrom(projectLaunchSettings);
		buildTargetField.setFieldValue(projectLaunchSettings.buildTargetName);
		programPathField.setEffectiveFieldValue(projectLaunchSettings.getEffectiveProgramPath());
	}
	
	@Override
	protected ProjectLaunchSettings getLaunchSettingsFromTab() {
		return new BuildTargetLaunchSettings(
			getProjectName(),
			getBuildTargetName(),
			isDefaultProgramPath(),
			getProgramPathString()
		);
	}
	
}