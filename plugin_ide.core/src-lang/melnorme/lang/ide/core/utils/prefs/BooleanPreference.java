/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.prefs;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;


/**
 * Helper to work with a boolean preference.
 */
public class BooleanPreference extends PreferenceHelper<Boolean> {
	
	public BooleanPreference(String key, boolean defaultValue) {
		super(key, defaultValue);
	}
	
	public BooleanPreference(String pluginId, String key, boolean defaultValue) {
		super(pluginId, key, defaultValue);
	}
	
	@Override
	protected Boolean doGet(IPreferencesAccess prefsAccess) {
		return prefsAccess.getBoolean(key);
	}
	
	@Override
	protected void doSet(IEclipsePreferences projectPreferences, Boolean value) {
		projectPreferences.putBoolean(key, value);
	}
	
}