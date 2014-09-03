/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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
 * Helper to work with an int preference.
 */
public class IntPreference extends PreferenceHelper<Integer> {
	
	public IntPreference(String key, int defaultValue) {
		super(key, defaultValue);
	}
	
	@Override
	public Integer get() {
		return prefs().getInt(key, defaultValue);
	}
	
	public void set(int value) {
		prefs().setInt(key, value);
	}
	
	@Override
	protected void initializeDefaultValueInDefaultScope() {
		getDefaultPreferences().putInt(key, defaultValue);
	}
	
}