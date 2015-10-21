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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.CoreUtil.list;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.PreferenceHelper;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.MiscUtil;

public class TextStylingPreference extends PreferenceHelper<TextStyling> {
	
	public TextStylingPreference(String key, TextStyling styling) {
		this(LangUIPlugin.PLUGIN_ID, key, styling);
	}
	
	public TextStylingPreference(String qualifer, String key, TextStyling defaultValue) {
		super(qualifer, key, defaultValue);
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected TextStyling getPrefValue(IPreferencesAccess prefsAccess) {
		return getFromStore(prefsAccess, key);
	}
	
	@Override
	protected void setPrefValue(IEclipsePreferences preferences, TextStyling value) {
		setToStore(preferences, key, value);
	}
	
	@Override
	protected TextStyling parseString(String stringValue) {
		throw assertFail();
	}
	
	@Override
	protected String valueToString(TextStyling value) {
		throw assertFail();
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
		boolean isEnabled = getBoolean(prefsHelper, getEnabledKey(colorKey));
		boolean isBold = getBoolean(prefsHelper, getBoldKey(colorKey));
		boolean isItalic = getBoolean(prefsHelper, getItalicKey(colorKey));
		boolean isStrikethrough = getBoolean(prefsHelper, getStrikethroughKey(colorKey));
		boolean isUnderline = getBoolean(prefsHelper, getUnderlineKey(colorKey));
		
		return new TextStyling(isEnabled, rgb, isBold, isItalic, isStrikethrough, isUnderline);
	}
	
	protected static boolean getBoolean(IPreferencesAccess prefsHelper, String key) {
		return MiscUtil.parseBoolean(prefsHelper.getString(key));
	}
	
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
	
	public static RGB getRgb(IPreferencesAccess prefsHelper, String key) {
		return StringConverter.asRGB(prefsHelper.getString(key), PreferenceConverter.COLOR_DEFAULT_DEFAULT);
	}
	
	/* -----------------  ----------------- */
	
	protected static final Indexable<String> suffixes = list(
		TextColoringConstants.EDITOR_BOLD_SUFFIX,
		TextColoringConstants.EDITOR_ITALIC_SUFFIX,
		TextColoringConstants.EDITOR_STRIKETHROUGH_SUFFIX,
		TextColoringConstants.EDITOR_UNDERLINE_SUFFIX,
		TextColoringConstants.EDITOR_ENABLED_SUFFIX
	);
	
	@Override
	protected void handlePreferenceChange(PreferenceChangeEvent event) {
		String changedKey = event.getKey();
		
		String baseKey = key;
		if(changedKey.startsWith(baseKey)) {
			String suffix = changedKey.substring(baseKey.length());
			if(suffix.isEmpty() || suffixes.contains(suffix)) {
				field.setFieldValue(get());
			}
		}
	}
	
}