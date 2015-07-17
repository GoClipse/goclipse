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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.components.FieldComponent;
import melnorme.util.swt.components.LabelledFieldComponent;


public class ComboBoxField extends LabelledFieldComponent<Integer> {
	
	protected final String[] valueLabels;
	protected final String[] values;
	
	protected Combo combo;
	
	public ComboBoxField(String labelText, String[] labels, String[] values) {
		super(labelText, Option_AllowNull.NO, 0);
		this.valueLabels = labels;
		this.values = values;
		assertTrue(labels != null && values != null && labels.length == values.length);
		assertTrue(labels.length > 0);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 2;
	}
	
	@Override
	protected void createContents_all(Composite topControl) {
		createContents_Label(topControl);
		createContents_Combo(topControl);
	}
	
	@Override
	protected void createContents_layout() {
		SWTLayoutUtil.layout2Controls_spanLast(label, combo);
	}
	
	protected void createContents_Combo(Composite topControl) {
		combo = createFieldCombo(this, topControl, SWT.SINGLE | SWT.READ_ONLY);
		combo.setFont(topControl.getFont());
		combo.setItems(valueLabels);
	}
	
	@Override
	public Combo getFieldControl() {
		return combo;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		int indexValue = getFieldValue();
		if(indexValue == -1) {
			return;
		}
		String label = valueLabels[indexValue];
		combo.setText(label);
		assertTrue(combo.getSelectionIndex() == indexValue);
	}
	
	public String getFieldStringValue() {
		int indexValue = getFieldValue();
		return getPrefValueFromIndex(indexValue);
	}
	
	protected String getPrefValueFromIndex(int index) {
		return index == -1 ? "" : values[index];
	}
	
	public void setFieldStringValue(String stringValue) {
		int indexValue = findIndexForStringValue(stringValue);
		setFieldValue(indexValue);
	}
	
	protected int findIndexForStringValue(String stringValue) {
		int index;
		for (index = 0; index < values.length; index++) {
			if(values[index].equals(stringValue)) {
				return index;
			}
		}
		return -1;
	}
	
	/* -----------------  ----------------- */
	
	public static Combo createFieldCombo(final FieldComponent<Integer> field, Composite parent, int style) {
		final Combo combo = new Combo(parent, style);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = combo.getSelectionIndex();
				field.setFieldValueFromControl(selectionIndex);
			}
		});
		return combo;
	}
	
}