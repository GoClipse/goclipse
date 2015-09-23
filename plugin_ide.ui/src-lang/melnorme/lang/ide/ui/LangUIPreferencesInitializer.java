/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui;


import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;

public abstract class LangUIPreferencesInitializer extends AbstractPreferenceInitializer  
implements EditorPrefConstants, ContentAssistConstants {
	
	public LangUIPreferencesInitializer() {
		super();
	}
	
	@Override
	public final void initializeDefaultPreferences() {
		final IPreferenceStore store = LangUIPlugin.getInstance().getPreferenceStore();
		
		store.setDefault(MATCHING_BRACKETS, true);
		store.setDefault(MATCHING_BRACKETS_COLOR, StringConverter.asString(new RGB(192, 192, 192)));
		
		_PrefInitializer.initializeDefaults(store);
		initializeDefaultPreferences_other(store);
	}
	
	protected abstract void initializeDefaultPreferences_other(final IPreferenceStore langUIStore);
	
}