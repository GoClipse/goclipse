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

import melnorme.lang.ide.ui.preferences.IPreferencesComponent;
import melnorme.util.swt.components.fields.ComboBoxField;

import org.eclipse.jface.preference.IPreferenceStore;

public class ComboFieldAdapter extends AbstractFieldAdapter<Integer> implements IPreferencesComponent {
	
	protected final ComboBoxField field;
	
	public ComboFieldAdapter(String prefKey, ComboBoxField field) {
		super(prefKey, field);
		this.field = field;
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		String prefValue = store.getString(prefKey);
		field.setFieldStringValue(prefValue);
	}
	
	@Override
	public void loadStoreDefaults(IPreferenceStore store) {
		field.setFieldStringValue(store.getDefaultString(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		String value = field.getFieldStringValue();
		store.setValue(prefKey, value);
	}
	
}