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

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

public class LangColoringPreferencesHelper {
	
	public static String getColorKey(String key) {
		return key + "";
	}
	
	public static String getEnabledKey(String key) {
		return key + TextColoringConstants.EDITOR_SEMANTIC_HIGHLIGHTING_ENABLED_SUFFIX;
	}
	
	public static String getBoldKey(String key) {
		return key + TextColoringConstants.EDITOR_BOLD_SUFFIX;
	}
	
	public static String getItalicKey(String key) {
		return key + TextColoringConstants.EDITOR_ITALIC_SUFFIX;
	}
	
	public static String getUnderlineKey(String key) {
		return key + TextColoringConstants.EDITOR_UNDERLINE_SUFFIX;
	}
	
	protected static void setColoringStyle(IEclipsePreferences node, String key, RGB color, boolean bold, 
			boolean italic, boolean underline) {
		setStyleDefault(node, key, true, color, bold, italic, underline);
	}
	
	public static void setStyleDefault(IEclipsePreferences store, String key, boolean enabled, 
			RGB color, boolean bold, boolean italic, boolean underline) {
		setIsEnabled(store, key, enabled);
		setColor(store, key, color);
		setIsBold(store, key, bold);
		setIsItalic(store, key, italic);
		setIsUnderline(store, key, underline);
	}
	
	public static void setIsEnabled(IEclipsePreferences store, String key, boolean enabled) {
		store.putBoolean(LangColoringPreferencesHelper.getEnabledKey(key), enabled);
	}
	
	public static void setColor(IEclipsePreferences store, String key, RGB rgb) {
		store.put(LangColoringPreferencesHelper.getColorKey(key), StringConverter.asString(rgb));
	}
	
	public static void setIsBold(IEclipsePreferences store, String key, boolean bold) {
		store.putBoolean(LangColoringPreferencesHelper.getBoldKey(key), bold);
	}
	
	public static void setIsItalic(IEclipsePreferences store, String key, boolean italic) {
		store.putBoolean(LangColoringPreferencesHelper.getItalicKey(key), italic);
	}
	
	public static void setIsUnderline(IEclipsePreferences store, String key, boolean underline) {
		store.putBoolean(LangColoringPreferencesHelper.getUnderlineKey(key), underline);
	}
	
}