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
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.fields.CheckBoxField;

import org.eclipse.jface.preference.IPreferenceStore;

public class BooleanFieldAdapter extends AbstractFieldAdapter<Boolean> implements IPreferencesComponent {
	
	public BooleanFieldAdapter(String label) {
		this(label, new CheckBoxField(label));
	}
	
	public BooleanFieldAdapter(String label, AbstractField<Boolean> field) {
		super(label, field);
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		field.setFieldValue(store.getBoolean(prefKey));
	}
	
	@Override
	public void loadStoreDefaults(IPreferenceStore store) {
		field.setFieldValue(store.getDefaultBoolean(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		store.setValue(prefKey, field.getFieldValue());
	}
	
}