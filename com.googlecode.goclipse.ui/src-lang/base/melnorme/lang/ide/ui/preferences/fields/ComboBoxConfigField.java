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
package melnorme.lang.ide.ui.preferences.fields;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;


public class ComboBoxConfigField extends AbstractConfigField<Integer> {
	
	protected final String[] labels;
	protected final String[] values;
	
	protected Label labelControl;
	protected Combo combo;
	
	public ComboBoxConfigField(String label, final String prefKey,
			String[] labels, String[] values) {
		super(prefKey, label);
		this.labels = labels;
		this.values = values;
		assertTrue(labels != null && values != null && labels.length == values.length);
		assertTrue(labels.length > 0);
	}
	
	@Override
	public Integer getDefaultFieldValue() {
		return 0;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		labelControl = new Label(topControl, SWT.NONE);
		labelControl.setText(label);
		labelControl.setLayoutData(GridDataFactory.swtDefaults().create());
		
		combo = createFieldCombo(this, topControl, SWT.SINGLE | SWT.READ_ONLY);
		combo.setLayoutData(new GridData());
		combo.setFont(topControl.getFont());
		combo.setItems(labels);
		combo.setData(prefKey);
	}
	
	@Override
	public Combo getFieldControl() {
		return combo;
	}
	
	@Override
	public Control getLeftMostControl() {
		return labelControl;
	}
	
	@Override
	protected void doUpdateComponentFromValue() {
		String label = labels[getFieldValue()];
		combo.setText(label);
		assertTrue(combo.getSelectionIndex() == getFieldValue());
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		String prefValue = store.getString(prefKey);
		setFieldValue(findIndexForPrefValue(prefValue));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		String value = getPrefValueFromIndex(getFieldValue());
		store.setValue(prefKey, value);
	}
	
	protected int findIndexForPrefValue(String prefValue) {
		int index;
		for (index = 0; index < values.length; index++) {
			if(values[index].equals(prefValue)) {
				return index;
			}
		}
		return -1;
	}
	
	protected String getPrefValueFromIndex(int index) {
		String value = "";
		if(index != -1) {
			value = values[index];
		}
		return value;
	}
	
}