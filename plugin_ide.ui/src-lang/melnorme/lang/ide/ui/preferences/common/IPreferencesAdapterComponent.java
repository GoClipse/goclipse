/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences.common;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.util.swt.components.AbstractField;
import melnorme.util.swt.components.fields.ComboBoxField;

import org.eclipse.jface.preference.IPreferenceStore;


public interface IPreferencesAdapterComponent {
	
	void loadFromStore(IPreferenceStore store);
	
	void saveToStore(IPreferenceStore store);
	
	void loadStoreDefaults(IPreferenceStore store);
	
	/* ----------------- Default adapters: ----------------- */
	
	public abstract class AbstractFieldAdapter<VALUE> implements IPreferencesAdapterComponent {
		
		protected final String prefKey;
		protected final AbstractField<VALUE> field;
		
		public AbstractFieldAdapter(String prefKey, AbstractField<VALUE> field) {
			this.prefKey = assertNotNull(prefKey);
			this.field = assertNotNull(field);
		}
		
		public AbstractField<VALUE> getField() {
			return field;
		}
		
	}
	
	public class StringFieldAdapter extends AbstractFieldAdapter<String> {
		
		public StringFieldAdapter(String prefKey, AbstractField<String> field) {
			super(prefKey, field);
		}
		
		@Override
		public void loadFromStore(IPreferenceStore store) {
			field.setFieldValue(store.getString(prefKey));
		}
		
		@Override
		public void loadStoreDefaults(IPreferenceStore store) {
			field.setFieldValue(store.getDefaultString(prefKey));
		}
		
		@Override
		public void saveToStore(IPreferenceStore store) {
			store.setValue(prefKey, field.getFieldValue());
		}
		
	}
	
	public class BooleanFieldAdapter extends AbstractFieldAdapter<Boolean> {
		
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
	
	public class ComboFieldAdapter extends AbstractFieldAdapter<Integer> {
		
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
	
}