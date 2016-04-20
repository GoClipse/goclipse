/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import melnorme.lang.ide.core.CoreSettings.SettingsField;
import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.tooling.data.ValidationField;
import melnorme.util.swt.components.FieldWidget;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IField;
import melnorme.utilbox.fields.IProperty;

public class PreferencesPageContext implements IPreferencesEditor {
	
	protected final ArrayList2<IPreferencesEditor> prefAdapters = new ArrayList2<>();
	
	@Override
	public void loadDefaults() {
		for(IPreferencesEditor prefAdapter : prefAdapters) {
			prefAdapter.loadDefaults();
		}
	}
	
	@Override
	public void doSaveSettings() throws CommonException {
		for(IPreferencesEditor prefAdapter : prefAdapters) {
			prefAdapter.doSaveSettings();
		}
	}
	
	/**
	 * Add a {@link IPreferencesEditor}. Add order is preserved)
	 */
	public void addPrefElement(IPreferencesEditor prefElement) {
		prefAdapters.add(prefElement);
	}
	public void bindToPreference(FieldWidget<String> field, SettingsField<?> prefField) {
		bindToPreference(field, prefField.getGlobalPreference());
		field.addFieldValidator(true, prefField.getValidator());
	}
	public <T> void bindToPreference(IProperty<T> field, IProjectPreference<T> pref) {
		bindToPreference(field, pref.getGlobalPreference());
	}
	public <T> void bindToPreference(IProperty<T> field, IGlobalPreference<T> pref) {
		addPrefElement(getPreferencesBinder(field, pref));
	}
	
	public <T> IPreferencesEditor getPreferencesBinder(IProperty<T> field, IGlobalPreference<T> pref) {
		return new GlobalPreferenceAdapter<>(pref, field);
	}
	
	/* -----------------  ----------------- */
	
	public class GlobalPreferenceAdapter<T> implements IPreferencesEditor {
		
		protected final IGlobalPreference<T> preference;
		protected final IProperty<T> property;
		
		public GlobalPreferenceAdapter(IGlobalPreference<T> preference, IProperty<T> property) {
			this.preference = assertNotNull(preference);
			this.property = assertNotNull(property);
			
			property.set(preference.asField().get());
		}
		
		@Override
		public void doSaveSettings() throws CommonException {
			preference.setInstanceScopeValue(property.get());
		}
		
		@Override
		public void loadDefaults() {
			property.set(preference.getDefaultValue());
		}
		
	}
	
	/* ----------------- util ----------------- */
	
	public void bindToValidatedPreference(FieldWidget<String> field, DerivedValuePreference<?> derivedPref) {
		bindToPreference(field, derivedPref.getPreference());
		
		field.addFieldValidator(true, derivedPref.getValidator());
	}
	
	public void bindToValidatedPreference(IField<String> field, DerivedValuePreference<?> derivedPref, 
			ValidationField validation) {
		bindToPreference(field, derivedPref.getPreference());
		
		validation.addFieldValidator(true, field, derivedPref.getValidator());
	}
	
}