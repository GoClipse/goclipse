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
 * A string preference that allows the null value.
 */
public class OptionalStringPreference extends StringPreference {
	
	public OptionalStringPreference(String pluginId, String key,  
			IProjectPreference<Boolean> useProjectSettings) {
		super(pluginId, key, null, useProjectSettings);
	}
	
	@Override
	protected String getPrefValue(String savedValue) {
		if(savedValue != null && savedValue.startsWith(":")) {
			return savedValue.substring(1);
		}
		if(savedValue != null && savedValue.isEmpty()) {
			return null;
		}
		return savedValue;
	}
	
	@Override
	protected void setPrefValue(IEclipsePreferences preferences, String value) {
		String savedValue = value == null ? "" : ":" + value; 
		super.setPrefValue(preferences, savedValue);
	}
	
}