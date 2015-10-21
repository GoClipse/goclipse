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
package melnorme.lang.ide.ui.preferences;

import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.jface.text.ColorManager;

/**
 * Helper to work with a color preference.
 */
public class ColorPreference extends PreferenceHelper<RGB> {
	
	public ColorPreference(String key, RGB defaultValue) {
		super(LangUIPlugin.PLUGIN_ID, key, defaultValue);
	}
	public ColorPreference(String pluginId, String key, RGB defaultValue) {
		super(pluginId, key, defaultValue);
	}
	
	@Override
	protected RGB parseString(String stringValue) {
		return StringConverter.asRGB(stringValue);
	}
	
	@Override
	protected String valueToString(RGB value) {
		return StringConverter.asString(value);
	}
	
	public Color getManagedColor() {
		return ColorManager.getDefault().getColor(get());
	}
	
}