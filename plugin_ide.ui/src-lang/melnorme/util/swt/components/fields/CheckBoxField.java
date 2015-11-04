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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.LabelledFieldComponent;

public class CheckBoxField extends LabelledFieldComponent<Boolean> {
	
	protected Button checkBox;
	
	public CheckBoxField(String labelText) {
		super(labelText, Option_AllowNull.NO, false);
	}
	
	public boolean getBooleanFieldValue() {
		return getFieldValue();
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_CheckButton(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout1Control(checkBox);
	}
	
	@Override
	public Button getFieldControl() {
		return checkBox;
	}
	
	protected void createContents_CheckButton(Composite topControl) {
		checkBox = SWTFactoryUtil.createButton(topControl, SWT.CHECK | SWT.NONE, labelText);
		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setFieldValueFromControl(checkBox.getSelection());
			}
		});
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		checkBox.setSelection(getFieldValue());
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		SWTUtil.setEnabledIfOk(checkBox, enabled);
	}
	
}