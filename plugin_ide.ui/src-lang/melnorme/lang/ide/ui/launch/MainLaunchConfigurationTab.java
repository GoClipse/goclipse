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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.launch.ProjectBuildArtifactValidator;
import melnorme.lang.ide.core.launch.ProjectBuildArtifactValidator.ProjectBuildExecutableSettings;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTargetRunner;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.ProjectValidator;
import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.fields.ProjectRelativePathField;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

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
				MainLaunchConfigurationTab.this::validateProject);
		}
		
		@Override
		protected String getDefaultFieldValue() {
			try {
				return getValidatedBuildTargetRunner().getArtifactPath3();
			} catch(CoreException | CommonException e) {
				return null;
			}
		}
	}
	
	@Override
	protected void doValidate() throws CommonException, CoreException {
		getValidatedProgramFileLocation();
	}
	
	protected BuildTargetRunner getValidatedBuildTargetRunner() throws CommonException, CoreException {
		return getBuildManager().getBuildTargetOperation(validateProject(), getValidatedBuildTarget());
	}
	
	protected BuildTarget getValidatedBuildTarget() throws CommonException, CoreException {
		return getValidator().getBuildTarget_NonNull();
	}
	
	protected Location getValidatedProgramFileLocation() throws CoreException, CommonException {
		return getValidator().getValidExecutableFileLocation();
	}
	
	protected ProjectBuildArtifactValidator getValidator() {
		return new MainLaunchTab_ExecutableFileValidator();
	}
	
	protected class MainLaunchTab_ExecutableFileValidator extends ProjectBuildArtifactValidator {
		public MainLaunchTab_ExecutableFileValidator() {
			super(new MainLaunchTab_ProjectBuildExecutableSettings());
		}
		
		@Override
		protected ProjectValidator getProjectValidator() {
			return new ProjectValidator(LangNature.NATURE_ID);
		}
		
	}
	
	public class MainLaunchTab_ProjectBuildExecutableSettings implements ProjectBuildExecutableSettings {
		@Override
		public String getProject_Attribute() throws CoreException {
			return getProjectName();
		}
		
		@Override
		public String getExecutablePath_Attribute() throws CoreException {
			return programPathField.getEffectiveFieldValue();
		}
		
		@Override
		public String getBuildTarget_Attribute() throws CoreException {
			return getBuildTargetName();
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
	protected void setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(contextualResource, config);
		programPathField_setDefaults(contextualResource, config);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, true);
	}
	
	protected void programPathField_setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		if(contextualResource instanceof IResource) {
			IResource resource = (IResource) contextualResource;
			config.setAttribute(LaunchConstants.ATTR_PROJECT_NAME, resource.getProject().getName());
		}
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		buildTargetField.setFieldValue(getConfigAttribute(config, LaunchConstants.ATTR_BUILD_TARGET, ""));
		CheckBoxField enablementField = programPathField.getUseDefaultField();
		enablementField.setFieldValue(getConfigAttribute(config, LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, true));
		programPathField.setFieldValue(getConfigAttribute(config, LaunchConstants.ATTR_PROGRAM_PATH, ""));
	}
	
	@Override
	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		super.doPerformApply(config);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH_USE_DEFAULT, isDefaultProgramPath());
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, getProgramPathString());
		config.setAttribute(LaunchConstants.ATTR_BUILD_TARGET, getBuildTargetName());
	}
	
}