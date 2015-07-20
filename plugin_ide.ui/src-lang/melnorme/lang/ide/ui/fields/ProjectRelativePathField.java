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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.data.StatusException;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.core.fntypes.ICallable;

/**
 * Field for a program path relative to Eclipse project.
 */
public abstract class ProjectRelativePathField extends EnablementButtonTextField {
	
	protected final ICallable<IProject, StatusException> projectGetter;
	
	public ProjectRelativePathField(String labelText, String enablementCheckBoxLabel, 
			ICallable<IProject, StatusException> projectGetter) {
		super(labelText, enablementCheckBoxLabel, LangUIMessages.LaunchTab_ProgramPathField__searchButton);
		this.projectGetter = assertNotNull(projectGetter);
	}
	
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