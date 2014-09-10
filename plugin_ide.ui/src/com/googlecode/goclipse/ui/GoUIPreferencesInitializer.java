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
package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

public class GoUIPreferencesInitializer extends AbstractPreferenceInitializer 
	implements GoUIPreferenceConstants {

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = LangUIPlugin.getInstance().getPreferenceStore();
		
	    store.setDefault(EDITOR_MATCHING_BRACKETS, true);
	    store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, StringConverter.asString(new RGB(128, 128, 128)));
	}
	
}
