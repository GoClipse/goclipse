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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ComboBoxConfigField extends AbstractConfigField<String> {
	
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
	}
	
	@Override
	protected void createContents(Composite topControl) {
		labelControl = new Label(topControl, SWT.NONE);
		labelControl.setText(label);
		labelControl.setLayoutData(GridDataFactory.swtDefaults().create());
		
		combo = new Combo(topControl, SWT.SINGLE | SWT.READ_ONLY);
		combo.setLayoutData(new GridData());
		combo.setFont(topControl.getFont());
		combo.setItems(labels);
		combo.setData(prefKey);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comboText = combo.getText();
				for (int i = 0; i < labels.length; i++) {
					if(labels[i].equals(comboText)) {
						updateFieldValue(values[i]);
					}
				}
			}
		});
	}
	
	@Override
	public Control getFieldControl() {
		return combo;
	}
	
	@Override
	public Control getLeftMostControl() {
		return labelControl;
	}
	
	@Override
	public void doUpdateComponentFromValue() {
		for (int i = 0; i < values.length; i++) {
			if(values[i].equals(getFieldValue())) {
				combo.setText(labels[i]);
			}
		}
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		setFieldValue(store.getString(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		store.setValue(prefKey, getFieldValue());
	}
	
}