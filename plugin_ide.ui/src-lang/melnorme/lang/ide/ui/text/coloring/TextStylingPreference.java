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
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.IPreferencesAccess;
import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.ui.LangUIPlugin;

public class TextStylingPreference extends PreferenceHelper<TextStyling> {
	
	public TextStylingPreference(String key, 
			RGB color, boolean bold, boolean italic) {
		this(LangUIPlugin.PLUGIN_ID, key, true, color, bold, italic, false, false);
	}
	
	public TextStylingPreference(String key, 
			boolean enabled, RGB color, boolean bold, boolean italic, boolean isStrikethrough, boolean underline) {
		this(LangUIPlugin.PLUGIN_ID, key, enabled, color, bold, italic, isStrikethrough, underline);
	}
	
	public TextStylingPreference(String qualifer, String key, 
			boolean enabled, RGB rgb, boolean isBold, boolean isItalic, boolean isStrikethrough, boolean isUnderline) {
		super(qualifer, key, new TextStyling(enabled, rgb, isBold, isItalic, isStrikethrough, isUnderline));
	}
	
	
	/* -----------------  ----------------- */
	
	public static String getColorKey(String key) {
		return key + "";
	}
	
	public static String getEnabledKey(String key) {
		return key + TextColoringConstants.EDITOR_ENABLED_SUFFIX;
	}
	
	public static String getBoldKey(String key) {
		return key + TextColoringConstants.EDITOR_BOLD_SUFFIX;
	}
	
	public static String getItalicKey(String key) {
		return key + TextColoringConstants.EDITOR_ITALIC_SUFFIX;
	}
	
	public static String getStrikethroughKey(String key) {
		return key + TextColoringConstants.EDITOR_STRIKETHROUGH_SUFFIX;
	}
	
	public static String getUnderlineKey(String key) {
		return key + TextColoringConstants.EDITOR_UNDERLINE_SUFFIX;
	}
	
	@Override
	protected void doSet(IEclipsePreferences projectPreferences, TextStyling textStyle) {
		setToStore(projectPreferences, key, textStyle);
	}
	
	@Override
	protected TextStyling doGet(IPreferencesAccess prefsAccess) {
		return getFromStore(prefsAccess, key);
	}
	
	public static void setToStore(IEclipsePreferences store, String key, TextStyling textStyle) {
		store.putBoolean(getEnabledKey(key), textStyle.isEnabled);
		store.put(getColorKey(key), StringConverter.asString(textStyle.rgb));
		store.putBoolean(getBoldKey(key), textStyle.isBold);
		store.putBoolean(getItalicKey(key), textStyle.isItalic);
		store.putBoolean(getStrikethroughKey(key), textStyle.isStrikethrough);
		store.putBoolean(getUnderlineKey(key), textStyle.isUnderline);
	}
	
	public static TextStyling getFromStore(IPreferencesAccess prefsHelper, String colorKey) {
		RGB rgb = getRgb(prefsHelper, getColorKey(colorKey));
		boolean isEnabled = prefsHelper.getBoolean(getEnabledKey(colorKey));
		boolean isBold = prefsHelper.getBoolean(getBoldKey(colorKey));
		boolean isItalic = prefsHelper.getBoolean(getItalicKey(colorKey));
		boolean isStrikethrough = prefsHelper.getBoolean(getStrikethroughKey(colorKey));
		boolean isUnderline = prefsHelper.getBoolean(getUnderlineKey(colorKey));
		
		return new TextStyling(isEnabled, rgb, isBold, isItalic, isStrikethrough, isUnderline);
	}
	
	public static RGB getRgb(IPreferencesAccess prefsHelper, String key) {
		return StringConverter.asRGB(prefsHelper.getString(key), PreferenceConverter.COLOR_DEFAULT_DEFAULT);
	}
	
}