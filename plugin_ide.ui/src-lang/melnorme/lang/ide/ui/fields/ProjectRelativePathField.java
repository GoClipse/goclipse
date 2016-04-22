/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

/**
 * Field for a program path relative to Eclipse project.
 */
public class ProjectRelativePathField extends EnablementButtonTextField {
	
	protected final CommonGetter<IProject> projectGetter;
	protected final CommonGetter<String> defaultFieldValueGetter;
	
	public ProjectRelativePathField(String labelText, String useDefaultField_Label, 
			CommonGetter<IProject> projectGetter,
			CommonGetter<String> defaultFieldValueGetter
	) {
		super(labelText, useDefaultField_Label, LangUIMessages.LaunchTab_ProgramPathField__searchButton);
		this.projectGetter = assertNotNull(projectGetter);
		this.defaultFieldValueGetter = defaultFieldValueGetter;
	}
	
	@Override
	protected String getNewValueFromButtonSelection() throws CommonException, OperationCancellation {
		return ControlUtils.openProgramPathDialog(projectGetter.get(), button);
	}
	
	@Override
	protected String getDefaultFieldValue() throws CommonException {
		return defaultFieldValueGetter.get();
	}
	
}