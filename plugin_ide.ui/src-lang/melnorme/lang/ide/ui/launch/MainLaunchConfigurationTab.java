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

import melnorme.lang.ide.launching.LaunchConstants;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.fields.ProgramPathField;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.views.navigator.ResourceComparator;

/**
 * A main LaunchConfigurationTa with project selection field, and program path selection field.
 */
public abstract class MainLaunchConfigurationTab extends CommonMainLaunchConfigurationTab {
	
	public MainLaunchConfigurationTab() {
		super();
	}
	
	protected ProgramPathField programPathField = createProgramPathField_2();
	
	protected Launch_ProgramPathField createProgramPathField_2() {
		return new Launch_ProgramPathField();
	}
	
	protected class Launch_ProgramPathField extends ProgramPathField {
		@Override
		protected void handleSearchButtonSelected() {
			openProgramPathDialog();
		}
		
		@Override
		protected void handleFieldValueAndControlChanged() {
			updateLaunchConfigurationDialog();
		}
	}
	
	protected void openProgramPathDialog() {
		IProject project = getProject();
		if (project == null || !isValidProject(project)) {
			getShell().getDisplay().beep();
			MessageDialog.openInformation(getShell(), 
					LangUIMessages.error_CannotBrowse, LangUIMessages.error_notAValidProject);
			return;
		}
		
		openProgramPathDialog(project);
	}
	
	protected void openProgramPathDialog(IProject project) {
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				getShell(), new WorkbenchLabelProvider(),
				new WorkbenchContentProvider());
		dialog.setTitle(LangUIMessages.mainTab_ProgramPath_searchButton_title);
		dialog.setMessage(LangUIMessages.mainTab_ProgramPath_searchButton_message);
		
		dialog.setInput(project);
		dialog.setComparator(new ResourceComparator(ResourceComparator.NAME));
		if (dialog.open() == IDialogConstants.OK_ID) {
			IResource resource = (IResource) dialog.getFirstResult();
			String arg = resource.getProjectRelativePath().toPortableString();
			// check extension
			programPathField.setFieldValue(arg);
		}
	}
	
	@Override
	protected void createCustomControls(Composite parent) {
		programPathField.createComponent(parent, new GridData(GridData.FILL_HORIZONTAL));
	}
	
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
	
	protected String getProgramPathName() {
		return programPathField.getFieldValue();
	}
	
	protected IFile getProgramFile() {
		IProject project = getProject();
		if (!project.exists()) {
			return null;
		}
		String programPathStr = getProgramPathName();
		if (programPathStr == null || programPathStr.isEmpty()) {
			return null;
		} else {
			IFile programFile = project.getFile(programPathStr);
			return programFile;
		}
	}
	
	/* ---------- save/apply ---------- */
	
	@Override
	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		super.doPerformApply(config);
		config.setAttribute(LaunchConstants.ATTR_PROGRAM_PATH, getProgramPathName());
	}
	
	@Override
	protected IResource getMappedResource() throws CoreException {
		IResource programFile = getProgramFile();
		if(programFile != null && programFile.exists()) {
			return programFile;
		}
		return getProject();
	}
	
	/* ---------- validation ---------- */
	
	@Override
	protected void doValidate() {
		super.doValidate();
		validateProgramPath();
	}
	
	protected void validateProgramPath() {
		IProject project = getProject();
		if (project == null || !project.exists()) {
			return;
		}
		
		IFile programFile = getProgramFile();
		if(programFile == null) {
			setErrorMessage(LangUIMessages.error_ProgramPathNotValid);
			return;
		}
		if(!programFile.exists()) {
			setErrorMessage(LangUIMessages.error_ProgramPathNotExistingFile);
			return;
		}
		
		return;
	}
	
}