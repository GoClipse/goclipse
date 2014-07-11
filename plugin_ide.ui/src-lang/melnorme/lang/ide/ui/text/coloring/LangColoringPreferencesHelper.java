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
package melnorme.lang.ide.ui.text.coloring;

import melnorme.lang.ide.ui.PreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

public class LangColoringPreferencesHelper {
	
	public static String getColorKey(String key) {
		return key + "";
	}
	
	public static String getEnabledKey(String key) {
		return key + PreferenceConstants.EDITOR_SEMANTIC_HIGHLIGHTING_ENABLED_SUFFIX;
	}
	
	public static String getBoldKey(String key) {
		return key + PreferenceConstants.EDITOR_BOLD_SUFFIX;
	}
	
	public static String getItalicKey(String key) {
		return key + PreferenceConstants.EDITOR_ITALIC_SUFFIX;
	}
	
	public static String getUnderlineKey(String key) {
		return key + PreferenceConstants.EDITOR_UNDERLINE_SUFFIX;
	}
	
	protected static void setColoringStyle(IPreferenceStore store, String key, RGB color, boolean bold, 
			boolean italic, boolean underline) {
		setColoringStyle(store, key, true, color, bold, italic, underline);
	}
	
	public static void setColoringStyle(IPreferenceStore store, String key, boolean enabled, 
			RGB color, boolean bold, boolean italic, boolean underline) {
		setIsEnabled(store, key, enabled);
		setColor(store, key, color);
		setIsBold(store, key, bold);
		setIsItalic(store, key, italic);
		setIsUnderline(store, key, underline);
	}
	
	
	public static void setIsEnabled(IPreferenceStore store, String key, boolean enabled) {
		store.setDefault(LangColoringPreferencesHelper.getEnabledKey(key), enabled);
	}
	
	public static void setColor(IPreferenceStore store, String key, RGB rgb) {
		PreferenceConverter.setDefault(store, LangColoringPreferencesHelper.getColorKey(key), rgb);
	}
	
	public static void setIsBold(IPreferenceStore store, String key, boolean bold) {
		store.setDefault(LangColoringPreferencesHelper.getBoldKey(key), bold);
	}
	
	public static void setIsItalic(IPreferenceStore store, String key, boolean italic) {
		store.setDefault(LangColoringPreferencesHelper.getItalicKey(key), italic);
	}
	
	public static void setIsUnderline(IPreferenceStore store, String key, boolean underline) {
		store.setDefault(LangColoringPreferencesHelper.getUnderlineKey(key), underline);
	}
	
}