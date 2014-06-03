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

import org.eclipse.jface.preference.IPreferenceStore;

public class StringFieldAdapter extends AbstractFieldAdapter<String> implements IPreferencesComponent {
	
	public StringFieldAdapter(String prefKey, AbstractField<String> field) {
		super(prefKey, field);
	}
	
	@Override
	public void loadFromStore(IPreferenceStore store) {
		field.setFieldValue(store.getString(prefKey));
	}
	
	@Override
	public void saveToStore(IPreferenceStore store) {
		store.setValue(prefKey, field.getFieldValue());
	}
	
}