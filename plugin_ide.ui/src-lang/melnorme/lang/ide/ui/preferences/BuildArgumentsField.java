/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.operations.build.VariablesResolver;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.ControlUtils;
import melnorme.util.swt.components.fields.EnablementButtonTextField;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonGetter;

/* FIXME: rename this, rework*/
public class BuildArgumentsField extends EnablementButtonTextField {
	
	protected final CommonGetter<String> getDefaultBuildTargetArguments;
	protected final VariablesResolver variablesResolver;
	
	public BuildArgumentsField(CommonGetter<String> getDefaultBuildTargetArguments, 
			VariablesResolver variablesResolver) {
		super(LangUIMessages.Fields_BuildCommand, LABEL_UseDefault, LangUIMessages.Fields_VariablesButtonLabel);
		defaultTextStyle = SWT.MULTI | SWT.BORDER;
		
		this.getDefaultBuildTargetArguments = assertNotNull(getDefaultBuildTargetArguments);
		this.variablesResolver = variablesResolver;
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
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected String getDefaultFieldValue() throws CommonException {
		return getDefaultBuildTargetArguments.get();
	}
	
	@Override
	protected String getNewValueFromButtonSelection2() throws OperationCancellation {
		StringVariableSelectionDialog variablesDialog = new StringVariableSelectionDialog(text.getShell());
		
		variablesDialog.setElements(variablesResolver.getVariables());
		
		String addedVar = ControlUtils.openStringVariableSelectionDialog(variablesDialog);
		return getFieldValue() + addedVar;
	}
	
}