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

import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.DerivedValuePreference;
import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.core.utils.prefs.IProjectPreference;
import melnorme.lang.tooling.data.CompositeValidatableField;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.fields.IModelField;
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
	public void doSaveSettings() throws BackingStoreException {
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
			
			property.setValue(preference.asField().getValue());
		}
		
		@Override
		public void doSaveSettings() throws BackingStoreException {
			preference.setInstanceScopeValue(property.getValue());
		}
		
		@Override
		public void loadDefaults() {
			property.setValue(preference.getDefaultValue());
		}
		
	}
	
	/* ----------------- util ----------------- */
	
	public void bindToValidatedPreference(IModelField<String> field, DerivedValuePreference<?> pref, 
			CompositeValidatableField validation) {
		bindToPreference(field, pref);
		
		validation.addFieldValidation(true, field, pref.getValidator());
	}
	
}