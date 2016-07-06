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

import melnorme.utilbox.misc.MiscUtil;


/**
 * Helper to work with a boolean preference.
 */
public class BooleanPreference extends PreferenceHelper<Boolean> {
	
	public BooleanPreference(String key, boolean defaultValue) {
		super(key, defaultValue);
	}
	public BooleanPreference(String key, boolean defaultValue, IProjectPreference<Boolean> useProjectSettings) {
		super(key, defaultValue, useProjectSettings);
	}
	
	public BooleanPreference(String pluginId, String key, boolean defaultValue) {
		super(pluginId, key, defaultValue);
	}
	public BooleanPreference(String pluginId, String key, boolean defaultValue, 
			IProjectPreference<Boolean> useProjectSettings) {
		super(pluginId, key, defaultValue, useProjectSettings);
	}
	
	@Override
	protected Boolean parseString(String stringValue) {
		return MiscUtil.parseBoolean(stringValue);
	}
	
	@Override
	protected String valueToString(Boolean value) {
		return value.toString();
	}
	
}