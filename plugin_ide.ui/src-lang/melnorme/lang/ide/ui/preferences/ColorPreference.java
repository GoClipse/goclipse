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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.HashMap;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.util.swt.jface.text.ColorManager;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

class PreferenceHelper {
	
	protected static final HashMap<String, PreferenceHelper> instances = new HashMap<>();
	
	protected final String key;
	
	public PreferenceHelper(String key) {
		this.key = key;
		
		synchronized (instances) {
			// Allow only one instance of a preference helper per key.
			assertTrue(instances.containsKey(key) == false);
			instances.put(key, this);
		}
		
	}
	
	protected UIPreferencesLookupHelper uiPrefs() {
		return LangUIPlugin.getPreferencesLookup();
	}
	
}

/**
 * Helper to work with a color preference.
 */
public class ColorPreference extends PreferenceHelper {
	
	protected final RGB defaultValue;
	
	public ColorPreference(String key, RGB defaultValue) {
		super(key);
		this.defaultValue = assertNotNull(defaultValue);
	}
	
	public RGB get() {
		return uiPrefs().getRGB(key, defaultValue);
	}
	
	public void set(RGB value) {
		uiPrefs().setRGB(key, value);
	}
	
	public Color getManagedColor() {
		return ColorManager.getDefault().getColor(get());
	}
	
}