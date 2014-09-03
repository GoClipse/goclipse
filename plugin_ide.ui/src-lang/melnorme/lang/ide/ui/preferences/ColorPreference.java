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
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.util.swt.jface.text.ColorManager;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Helper to work with a color preference.
 */
public class ColorPreference extends PreferenceHelper<RGB> {
	
	public ColorPreference(String key, RGB defaultValue) {
		super(key, defaultValue);
	}
	
	@Override
	public RGB get() {
		String stringValue = prefs().getString(key, null);
		return stringValue == null ? defaultValue : StringConverter.asRGB(stringValue);
	}
	
	public void set(RGB value) {
		assertNotNull(value);
		String stringValue = StringConverter.asString(value);
		InstanceScope.INSTANCE.getNode(getQualifier()).put(key, stringValue);
	}
	
	public Color getManagedColor() {
		return ColorManager.getDefault().getColor(get());
	}
	
	@Override
	protected void initializeDefaultValueInDefaultScope() {
		getDefaultPreferences().put(key, StringConverter.asString(defaultValue));
	}
	
}