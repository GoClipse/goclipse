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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.LabelledFieldComponent;

public class SpinnerNumberField extends LabelledFieldComponent<Integer> {
	
	protected Spinner spinner;
	
	public SpinnerNumberField(String labelText) {
		super(labelText, Option_AllowNull.NO, 0);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_Label(topControl);
		createContents_Spinner(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout2Controls_spanLast(label, spinner);
	}
	
	protected void createContents_Spinner(Composite parent) {
		spinner = createFieldSpinner(this, parent, SWT.BORDER);
	}
	
	public Spinner getSpinner() {
		return spinner;
	}
	
	@Override
	public Spinner getFieldControl() {
		return spinner;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		spinner.setSelection(getFieldValue());
	}
	
	public SpinnerNumberField setValueMinimum(int minimum) {
		spinner.setMinimum(minimum);
		return this;
	}
	
	public SpinnerNumberField setValueMaximum(int maximum) {
		spinner.setMaximum(maximum);
		return this;
	}
	
	public SpinnerNumberField setValueIncrement(int increment) {
		spinner.setIncrement(increment);
		return this;
	}
	
	@Override
	protected void doSetEnabled(boolean enabled) {
		SWTUtil.setEnabledIfOk(label, enabled);
		SWTUtil.setEnabledIfOk(spinner, enabled);
	}
	
	/* -----------------  ----------------- */
	
	public static Spinner createFieldSpinner(FieldComponent<Integer> field, Composite parent, int style) {
		final Spinner spinner = new Spinner(parent, style);
		spinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				field.setFieldValueFromControl(spinner.getSelection());
			}
		});
		return spinner;
	}
	
}