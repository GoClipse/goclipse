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
 * Helper to work with a string preference.
 */
public class StringPreference extends PreferenceHelper<String> {
	
	public StringPreference(String key, String defaultValue) {
		super(key, defaultValue);
	}
	public StringPreference(String pluginId, String key, String defaultValue) {
		super(pluginId, key, defaultValue);
	}
	
	
	@Override
	protected String doGet(PreferencesLookupHelper combinedPrefs) {
		return combinedPrefs.getString(key);
	}
	
	@Override
	protected void doSet(IEclipsePreferences projectPreferences, String value) {
		projectPreferences.put(key, value);
	}
	
}