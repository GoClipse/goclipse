/*******************************************************************************
 * Copyright (c) 2013, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * 		DLTK team -
 * 		Bruno Medeiros - lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;

/**
 * A field whose main value is a project name from the Eclipse workspace.
 */
public class ProjectField extends ButtonTextField {
	
	public ProjectField() {
		super(LangUIMessages.mainTab_projectGroup, LangUIMessages.mainTab_projectButton);
	}
	
	protected IProject getProject() {
		String projectName = getFieldValue();
		if (projectName == null || projectName.isEmpty()) {
			return null;
		}
		return EclipseUtils.getWorkspaceRoot().getProject(projectName);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, getLabelText(), SWT.NONE);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return glSwtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_Label(Composite parent) {
		// Don't create
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		IProject project = chooseProject();
		return project == null ? null : project.getName();
	}
	
	protected IProject chooseProject() throws OperationCancellation {
		Shell shell = button.getShell();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, new WorkbenchLabelProvider());
		dialog.setTitle(LangUIMessages.projectField_chooseProject_title);
		dialog.setMessage(LangUIMessages.projectField_chooseProject_message);

		try {
			final IProject[] projects = getDialogChooseElements(); 
			dialog.setElements(projects);
		} catch (CoreException ce) {
			LangUIPlugin.logStatus(ce);
		}
		
		final IProject project = getProject();
		if (project != null && project.isOpen()) {
			dialog.setInitialSelections(new Object[] { project });
		}
		
		if (dialog.open() == Window.OK) {
			return (IProject) dialog.getFirstResult();
		}
		throw new OperationCancellation();
	}
	
	protected IProject[] getDialogChooseElements() throws CoreException {
		return EclipseUtils.getOpenedProjects(null);
	}
	
}