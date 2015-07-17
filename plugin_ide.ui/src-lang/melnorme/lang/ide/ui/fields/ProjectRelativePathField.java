/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.utilbox.core.fntypes.ICallable;

/**
 * Field for a program path relative to Eclipse project.
 */
public class ProjectRelativePathField extends ButtonTextField {
	
	protected final ICallable<IProject, StatusException> projectGetter;
	
	public ProjectRelativePathField(String labelText, ICallable<IProject, StatusException> projectGetter) {
		super(labelText, LangUIMessages.ProgramPathField__searchButton);
		this.projectGetter = projectGetter;
	}
	
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
	protected String getNewValueFromButtonSelection() {
		try {
			return ControlUtils.openProgramPathDialog(projectGetter.call(), button);
		} catch(StatusException se) {
			UIOperationExceptionHandler.handleStatusMessage(LangUIMessages.error_CannotBrowse, se);
			return null;
		}
	}
	
}