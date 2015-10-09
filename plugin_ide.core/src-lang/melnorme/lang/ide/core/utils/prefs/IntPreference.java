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
 * Helper to work with an int preference.
 */
public class IntPreference extends PreferenceHelper<Integer> {
	
	public IntPreference(String key, int defaultValue) {
		super(key, defaultValue);
	}
	public IntPreference(String pluginId, String key, int defaultValue) {
		super(pluginId, key, defaultValue);
	}
	public IntPreference(String pluginId, String key, int defaultValue, 
			IProjectPreference<Boolean> useProjectSettingsPref) {
		super(pluginId, key, defaultValue, useProjectSettingsPref);
	}
	
	@Override
	protected Integer doGet(IPreferencesAccess prefsAccess) {
		return prefsAccess.getInt(key);
	}
	
	@Override
	protected void doSet(IEclipsePreferences projectPreferences, Integer value) {
		projectPreferences.putInt(key, value);
	}
	
}