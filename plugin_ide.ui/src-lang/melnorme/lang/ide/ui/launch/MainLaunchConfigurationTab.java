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

import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.launch.ProjectBuildExecutableFileValidator;
import melnorme.lang.ide.core.operations.build.BuildTarget;
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
	
	protected final BuildTargetField buildTargetField = new BuildTargetField();
	protected final ProjectRelativePathField programPathField = createProgramPathField();
	
	public MainLaunchConfigurationTab() {
		initBindings();
	}
	
	protected ProjectRelativePathField createProgramPathField() {
		return new ProjectRelativePathField(
			LangUIMessages.LaunchTab_ProgramPathField_title,
			LangUIMessages.LaunchTab_ProgramPathField_useDefault,
			this::validateProject) {
			
			@Override
			protected String getDefaultFieldValue() throws CommonException {
				try {
					BuildTarget buildTarget = getValidatedBuildTarget();
					Path artifactPath = buildTarget.getBuildConfig().getArtifactPath();
					return artifactPath == null ? "" : artifactPath.toString();
				} catch(CoreException e) {
					throw new CommonException(e.getMessage(), e.getCause());
				}
			}
			
		};
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
	
	/* ---------- validation ---------- */
	
	@Override
	protected void doValidate() throws CommonException, CoreException {
		getValidatedProgramFileLocation();
	}
	
	protected BuildTarget getValidatedBuildTarget() throws CommonException, CoreException {
		return getValidator().getBuildTarget_NonNull();
	}
	
	protected Location getValidatedProgramFileLocation() throws CoreException, CommonException {
		return getValidator().getValidExecutableFileLocation();
	}
	
	protected MainLaunchTab_ExecutableFileValidator getValidator() {
		return new MainLaunchTab_ExecutableFileValidator();
	}
	
	protected class MainLaunchTab_ExecutableFileValidator extends ProjectBuildExecutableFileValidator {
		@Override
		protected String getProject_Attribute() throws CoreException {
			return getProjectName();
		}
		
		@Override
		protected ProjectValidator getProjectValidator() {
			return new ProjectValidator(LangNature.NATURE_ID);
		}
		
		@Override
		protected String getExecutablePath_Attribute() throws CoreException {
			return getProgramPathString();
		}
		
		@Override
		protected String getBuildTarget_Attribute() throws CoreException {
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
		buildTargetField.handleProjectChanged(project);
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
		if(contextualResource instanceof IFile) {
			IFile file = (IFile) contextualResource;
			String programPath = file.getProjectRelativePath().toString();
			config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, programPath);
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