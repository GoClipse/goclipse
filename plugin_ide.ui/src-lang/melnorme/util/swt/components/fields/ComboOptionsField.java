/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.SWTLayoutUtil;
import melnorme.util.swt.SWTUtil;
import melnorme.util.swt.components.LabelledFieldComponent;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.StringUtil;

/**
 * Field for a read_only combo with a set of options.
 * The user can only choose from the list of options in the combo,
 * but the available list of options can be changed during the lifetime of the component.
 *  
 * Canonicity: The field value can only be one of the values of the Combo options, or null.
 */
public class ComboOptionsField extends LabelledFieldComponent<String> {
	
	protected String[] comboOptions = array();
	
	protected int comboStyle = SWT.DROP_DOWN;
	protected Combo combo;
	
	public ComboOptionsField(String labelText) {
		super(labelText, Option_AllowNull.YES, null);
	}
	
	public void setFieldOptions(Indexable<String> items) {
		setComboItems(items.toArray(String.class));
	}
	
	public void setFieldOptions(String... items) {
		this.comboOptions = items;
		 //This will force the value to only the possible options
		String fieldValue = getEffectiveValue(getFieldValue());
		
		setComboItems(items);
		
		if(fieldValue == null && items.length > 0) {
			fieldValue = items[0];
		}
		setFieldValue(fieldValue); 
	}
	
	@Override
	protected void doSetFieldValue(String newValue) {
		super.doSetFieldValue(getEffectiveValue(newValue));
	}
	
	public String getEffectiveValue(String newValue) {
		if(newValue != null) {
			for(String comboOption : comboOptions) {
				if(newValue.equals(comboOption)) {
					return newValue;
				}
			}
		}	
		return null;
	}
	
	/* -----------------  ----------------- */
	
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
		SWTLayoutUtil.layout2Controls(label, combo);
	}
	
	protected void createContents_Combo(Composite topControl) {
		combo = createComboControl(topControl);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setFieldValueFromControl(combo.getText());
			}
		});
		setComboItems(comboOptions);
	}
	
	protected Combo createComboControl(Composite topControl) {
		return SWTFactoryUtil.createCombo(topControl, comboStyle | SWT.READ_ONLY);
	}
	
	protected void setComboItems(String... items) {
		if(SWTUtil.isOkToUse(combo)) {
			combo.setItems(items);
		}
	}
	
	@Override
	public Combo getFieldControl() {
		return combo;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		String stringValue = StringUtil.asString(getFieldValue());
		if(stringValue != null) {
			combo.setText(stringValue);
		} else {
			if(combo.getSelectionIndex() != -1) {
				combo.deselect(combo.getSelectionIndex());
			}
		}
	}
	
}