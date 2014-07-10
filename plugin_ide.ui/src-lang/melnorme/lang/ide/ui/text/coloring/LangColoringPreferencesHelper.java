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
	
	public static boolean getIsEnabled(IPreferenceStore store, String key) {
		return store.getBoolean(getEnabledKey(key));
	}
	
	public static RGB getColor(IPreferenceStore store, String key) {
		return PreferenceConverter.getColor(store, getColorKey(key));
	}
	
	public static boolean getIsBold(IPreferenceStore store, String key) {
		return store.getBoolean(getBoldKey(key));
	}
	
	public static boolean getIsItalic(IPreferenceStore store, String key) {
		return store.getBoolean(getItalicKey(key));
	}
	
	public static boolean getIsUnderline(IPreferenceStore store, String key) {
		return store.getBoolean(getUnderlineKey(key));
	}
	
}