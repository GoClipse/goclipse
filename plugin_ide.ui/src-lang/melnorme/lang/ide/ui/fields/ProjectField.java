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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.TextFieldComponent;

/**
 * A field whose main value is a project name from the Eclipse workspace.
 */
public class ProjectField extends TextFieldComponent {
	
	protected Button projectSelectionButton;
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, LangUIMessages.mainTab_projectGroup, SWT.NONE);
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
	protected Text doCreateContents(Composite topControl) {
		Text text = SWTFactoryUtil.createText(topControl, SWT.SINGLE | SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		projectSelectionButton = SWTFactoryUtil.createPushButton(topControl, 
				LangUIMessages.mainTab_projectButton, null);
		projectSelectionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleProjectButtonSelected();
			}
		});
		return text;
	}
	
	protected IProject getProject() {
		String projectName = getFieldValue();
		if (projectName == null || projectName.isEmpty()) {
			return null;
		}
		return EclipseUtils.getWorkspaceRoot().getProject(projectName);
	}
	
	/**
	 * Show a dialog that lets the user select a project. This in turn provides
	 * context for the main type, allowing the user to key a main type name, or
	 * constraining the search for main types to the specified project.
	 */
	protected void handleProjectButtonSelected() {
		IProject project = chooseProject();
		if (project == null) {
			return;
		}
		
		setFieldValue(project.getName());
	}
	
	protected IProject chooseProject() {
		Shell shell = projectSelectionButton.getShell();
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
		
		return null;
	}
	
	protected IProject[] getDialogChooseElements() throws CoreException {
		return EclipseUtils.getOpenedProjects(null);
	}
	
}