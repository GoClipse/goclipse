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
package melnorme.util.swt.components.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.components.AbstractFieldComponent;
import melnorme.util.swt.components.AbstractFieldExt2;

public class CheckBoxField extends AbstractFieldExt2<Boolean> {
	
	protected Button checkBox;
	
	public CheckBoxField(String labelText) {
		super(labelText, false);
	}
	
	public boolean getBooleanFieldValue() {
		return getFieldValue();
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents_do(Composite topControl) {
		checkBox = createFieldCheckbox(this, topControl, SWT.NONE);
		checkBox.setText(labelText);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout1Control(checkBox);
	}
	
	@Override
	public Button getFieldControl() {
		return checkBox;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		checkBox.setSelection(getFieldValue());
	}
	
	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);
	}
	
	/* -----------------  ----------------- */
	
	public static Button createFieldCheckbox(AbstractFieldComponent<Boolean> field, Composite parent, int style) {
		final Button checkBox = new Button(parent, SWT.CHECK | style);
		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				field.setFieldValueFromControl(checkBox.getSelection());
			}
		});
		return checkBox;
	}
	
}