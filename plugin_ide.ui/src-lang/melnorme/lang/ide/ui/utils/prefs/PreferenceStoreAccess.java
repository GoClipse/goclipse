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
package melnorme.lang.ide.ui.utils.prefs;

import org.eclipse.jface.preference.IPreferenceStore;

import melnorme.lang.ide.core.utils.prefs.PreferenceHelper.IPreferencesAccess;

/**
 * An adapter which provides only read access to a PST
 */
public class PreferenceStoreAccess implements IPreferencesAccess {
	
	protected final IPreferenceStore prefStore;
	
	public PreferenceStoreAccess(IPreferenceStore prefStore) {
		this.prefStore = prefStore;
	}
	
	@Override
	public String getString(String key) {
		return prefStore.getString(key);
	}
	
}