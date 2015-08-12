/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.fields;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.components.fields.ButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;

public class ArgumentsGroupField extends ButtonTextField {
	
	public ArgumentsGroupField(String labelText) {
		super(labelText, SWT.MULTI | SWT.BORDER, LangUIMessages.Fields_VariablesButtonLabel);
	}
	
	@Override
	protected Composite doCreateTopLevelControl(Composite parent) {
		return SWTFactoryUtil.createGroup(parent, labelText, SWT.NONE);
	}
	
	@Override
	protected GridLayoutFactory createTopLevelLayout() {
		return GridLayoutFactory.swtDefaults().numColumns(getPreferredLayoutColumns());
	}
	
	@Override
	protected void createContents_Label(Composite parent) {
		// Do not create label
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents_layout() {
		text.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(40, 100).create());
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		return getFieldValue() + ControlUtils.openStringVariableSelectionDialog(text.getShell());
	}
	
}