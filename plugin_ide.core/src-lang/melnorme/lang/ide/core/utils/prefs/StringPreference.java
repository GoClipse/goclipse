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
	public StringPreference(String pluginId, String key, String defaultValue, 
			IProjectPreference<Boolean> useProjectPref) {
		super(pluginId, key, defaultValue, useProjectPref);
	}
	
	@Override
	protected String parseString(String stringValue) {
		return stringValue;
	}
	
	@Override
	protected String valueToString(String value) {
		return value;
	}
	
}