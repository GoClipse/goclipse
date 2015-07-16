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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.fields.ProgramPathField;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.util.swt.components.fields.ComboFieldComponent;
import melnorme.utilbox.fields.IFieldValueListener;

/**
 * A main LaunchConfigurationTa with project selection field, and program path selection field.
 */
public abstract class MainLaunchConfigurationTab extends ProjectBasedLaunchConfigurationTab {
	
	protected final BuildTargetField buildTargetField = new BuildTargetField();
	protected final ProgramPathField programPathField = createProgramPathField_2();
	
	public MainLaunchConfigurationTab() {
		super();
		projectField.addValueChangedListener(projectFieldListener);
		programPathField.addValueChangedListener(projectFieldListener);
	}
	
	protected Launch_ProgramPathField createProgramPathField_2() {
		return new Launch_ProgramPathField();
	}
	
	protected String getProgramPathName() {
		return programPathField.getFieldValue();
	}
	
	/* ---------- validation ---------- */
	
	@Override
	protected void doValidate() throws StatusException {
		getValidatedProgramFile();
	}
	
	protected IFile getValidatedProgramFile() throws StatusException {
		IProject project = validateProject();
		
		String programPathStr = getProgramPathName();
		if(programPathStr == null || programPathStr.isEmpty()) {
			throw new StatusException(StatusLevel.ERROR, LangUIMessages.error_ProgramPathNotValid);
		}
		
		IFile file = project.getFile(programPathStr);
		if(!file.exists()) {
			throw new StatusException(StatusLevel.ERROR, LangUIMessages.error_ProgramPathNotExistingFile);
		}
		
		return file;
	}
	
	/* ----------------- Control creation ----------------- */
	
	@Override
	protected void createCustomControls(Composite parent) {
//		buildTargetField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
//		new ComboFieldComponent().createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
		programPathField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
	}
	
	protected class Launch_ProgramPathField extends ProgramPathField {
		@Override
		protected void handleSearchButtonSelected() {
			try {
				openProgramPathDialog(validateProject());
			} catch(StatusException se) {
				UIOperationExceptionHandler.handleStatusMessage(LangUIMessages.error_CannotBrowse, se);
			}
		}
	}
	
	/* ----------------- bindings ----------------- */
	
	protected final IFieldValueListener projectFieldListener = new IFieldValueListener() {
		@Override
		public void fieldValueChanged() {
			IProject project = getValidProjectOrNull();
			buildTargetField.handleProjectChanged(project);
			updateLaunchConfigurationDialog();
		}
	};
	
	/* ----------------- Apply/Revert/Defaults ----------------- */
	
	@Override
	protected void setDefaults(IResource contextualResource, ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(contextualResource, config);
		programPathField_setDefaults(contextualResource, config);
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
		String programPath = getConfigAttribute(config, LaunchConstants.ATTR_PROGRAM_PATH, ""); 
		programPathField.setFieldValue(programPath);
	}
	
	@Override
	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		super.doPerformApply(config);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, getProgramPathName());
	}
	
	@Override
	protected IResource getMappedResource() throws CoreException {
		try {
			return getValidatedProgramFile();
		} catch(StatusException e) {
			return super.getMappedResource();
		}
	}
	
}