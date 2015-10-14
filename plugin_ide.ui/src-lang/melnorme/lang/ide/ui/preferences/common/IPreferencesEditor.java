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

import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.utilbox.fields.IProperty;


public interface IPreferencesEditor {
	
	boolean saveSettings();
	
	void loadDefaults();
	
	/* -----------------  ----------------- */
	
	public abstract static class AbstractPreferencesEditor implements IPreferencesEditor {
		@Override
		public boolean saveSettings() {
			try {
				doSaveSettings();
				return true;
			} catch(BackingStoreException e) {
				return false;
			}
		}
		
		protected abstract void doSaveSettings() throws BackingStoreException;
		
	}
	
	public class GlobalPreferenceAdapter<T> extends AbstractPreferencesEditor {
		
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
	
}