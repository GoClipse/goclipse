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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CheckBoxConfigField extends AbstractConfigField<Boolean> {
	
	protected Button checkBox;
	
	public CheckBoxConfigField(String label, String prefKey) {
		super(prefKey, label);
	}
	
	protected boolean getBooleanFieldValue() {
		if(getFieldValue() == null) {
			return false;
		}
		return getFieldValue();
	}
	
	@Override
	protected void createContents(Composite topControl) {
		checkBox = new Button(topControl, SWT.CHECK);
		checkBox.setText(label);
		
		checkBox.setLayoutData(GridDataFactory.swtDefaults().span(2, 1).create());
		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFieldValue(checkBox.getSelection());
			}
		});
	}
	
	@Override
	public Control getFieldControl() {
		return checkBox;
	}
	
	@Override
	public Control getLeftMostControl() {
		return checkBox;
	}
	
	@Override
	public void doUpdateControls() {
		checkBox.setSelection(getBooleanFieldValue());
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		setFieldValue(store.getBoolean(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		store.setValue(prefKey, getBooleanFieldValue());
	}
	
}