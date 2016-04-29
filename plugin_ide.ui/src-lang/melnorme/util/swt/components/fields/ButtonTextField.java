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
package melnorme.util.swt.components.fields;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.utils.operations.BasicUIOperation;
import melnorme.util.swt.SWTFactory;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.WidgetSelectedRunner;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.Field;

public abstract class ButtonTextField extends TextFieldWidget {
	
	protected String buttonLabel;
	
	protected Button button;
	
	public ButtonTextField(String labelText, String buttonLabel) {
		super(labelText);
		this.buttonLabel = buttonLabel;
	}
	
	public ButtonTextField(Field<String> field, String labelText, String buttonLabel) {
		super(field, labelText);
		this.buttonLabel = buttonLabel;
	}
	
	protected String getButtonLabel() {
		return buttonLabel;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public int getPreferredLayoutColumns() {
		return 3;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		super.createContents_all(topControl);
		createContents_Button(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layoutControls(array(label, text, button), text, text);
	}
	
	/* -----------------  Button  ----------------- */
	
	protected void createContents_Button(Composite topControl) {
		button = SWTFactory.createPushButton(topControl, getButtonLabel(), null);
		button.addSelectionListener(new WidgetSelectedRunner(getButtonHandler()));
	}
	
	protected BasicUIOperation getButtonHandler() {
		return new SetFieldValueOperation<String>(this, this::getNewValueFromButtonSelection);
	}
	
	protected String getButtonOperationErrorMessage() {
		return "Error:";
	}
	
	protected abstract String getNewValueFromButtonSelection() 
			throws CoreException, CommonException, OperationCancellation;
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
		SWTUtil.setEnabledIfOk(button, enabled);
	}
	
}