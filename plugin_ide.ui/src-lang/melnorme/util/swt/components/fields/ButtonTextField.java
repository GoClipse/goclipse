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
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.WidgetSelectedRunner;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class ButtonTextField extends TextFieldComponent {
	
	protected String buttonLabel;
	protected Button button;
	
	public ButtonTextField(String label, int textStyle, String buttonLabel) {
		super(label, textStyle);
		this.buttonLabel = buttonLabel;
	}
	
	public ButtonTextField(String label, String buttonLabel) {
		super(label);
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
		button = SWTFactoryUtil.createPushButton(topControl, getButtonLabel(), null);
		button.addSelectionListener(new WidgetSelectedRunner(getButtonHandler()));
	}
	
	protected BasicUIOperation getButtonHandler() {
		return new SetFieldValueOperation<String>(this, this::getNewValueFromButtonSelection2);
	}
	
	protected String getButtonOperationErrorMessage() {
		return "Error:";
	}
	
	protected abstract String getNewValueFromButtonSelection2() 
			throws CoreException, CommonException, OperationCancellation;
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		super.doSetEnabled(enabled);
		SWTUtil.setEnabledIfOk(button, enabled);
	}
	
}