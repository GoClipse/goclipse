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
package melnorme.lang.ide.ui.text.coloring;

import melnorme.lang.ide.core.utils.prefs.AbstractPreferenceHelper;
import melnorme.lang.ide.ui.LangUIPlugin;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.graphics.RGB;

public class ColoringItemPreference extends AbstractPreferenceHelper {
	
	public ColoringItemPreference(String key, 
			boolean enabled, RGB color, boolean bold, boolean italic, boolean underline) {
		super(key);
		setStyleDefault(enabled, color, bold, italic, underline);
	}
	
	public void setStyleDefault(boolean enabled, RGB color, boolean bold, boolean italic, boolean underline) {
		IEclipsePreferences defaultNode = DefaultScope.INSTANCE.getNode(LangUIPlugin.PLUGIN_ID);
		LangColoringPreferencesHelper.setStyleDefault(defaultNode, key, enabled, color, bold, italic, underline);
	}
	
}