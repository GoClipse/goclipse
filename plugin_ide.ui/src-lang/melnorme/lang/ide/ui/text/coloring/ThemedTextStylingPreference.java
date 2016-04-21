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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;

import melnorme.lang.ide.core.utils.prefs.IPreferenceIdentifier;
import melnorme.lang.ide.ui.LangUI;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.ThemeHelper.ThemeChangeListener2;
import melnorme.util.swt.SWTUtil;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.Field;
import melnorme.utilbox.fields.FieldValueListener;
import melnorme.utilbox.fields.IField;
import melnorme.utilbox.fields.IFieldView;

public class ThemedTextStylingPreference implements IFieldView<TextStyling>, IPreferenceIdentifier {
	
	protected final String key;
	protected final String key_Dark;
	
	protected final TextStylingPreference defaultPref;
	protected final TextStylingPreference darkPref;
	
	protected final Field<TextStyling> effectiveValue = new Field<>();
	
	public ThemedTextStylingPreference(String key, TextStyling defaultValue, TextStyling defaultValueDark) {
		this(LangUIPlugin.PLUGIN_ID, key, defaultValue, defaultValueDark);
	}
	
	public ThemedTextStylingPreference(String qualifer, String key, TextStyling defaultValue, 
			TextStyling defaultValueDark) {
		
		this.key = key;
		this.key_Dark = key + "#dark#";
		
		this.defaultPref = new TextStylingPreference(qualifer, key, defaultValue);
		this.darkPref = new TextStylingPreference(qualifer, key_Dark, defaultValueDark);
		
		this.defaultPref.asField().registerListener((__) -> updateEffectiveValue());
		this.darkPref.asField().registerListener((__) -> updateEffectiveValue());
		
		LangUI.getInstance().getThemeHelper().asOwner().bind(new ThemeChangeListener2() {
			@Override
			public void handleEvent(Event event) {
				updateEffectiveValue();
			}
		});
		
		// We can't tell without going to UI if dark theme is active or no, so initialize with defaultPref
		effectiveValue.setFieldValue(defaultPref.getFromPrefStore());
		// .. then request an update of effective value
		updateEffectiveValue();
	}
	
	public TextStylingPreference getDefaultThemePref() {
		return defaultPref;
	}
	
	public TextStylingPreference getDarkPref() {
		return darkPref;
	}
	
	/* -----------------  ----------------- */
	
	protected void updateEffectiveValue() {
		SWTUtil.runInSWTThread(() -> {
			/* Unfortunately, this need to run in UI thread */
			effectiveValue.setFieldValue(getEffectivePreference().getFromPrefStore());
		});
	}
	
	protected TextStyling getEffectiveValue() {
		assertTrue(getEffectivePreference().get() == effectiveValue.get());
		return effectiveValue.get();
	}
	
	protected TextStylingPreference getEffectivePreference() {
		if(isOverridingThemeActive()) {
			return darkPref;
		}
		return defaultPref;
	}
	
	protected boolean isOverridingThemeActive() {
		assertTrue(Display.getCurrent() != null);
		return LangUI.getInstance().getThemeHelper().getIdOfActiveThemeForCurrentDisplay().contains("dark");
	}
	
	public TextStyling getDefaultValue() {
		return getEffectivePreference().getDefaultValue();
	}
	
	public String getActiveKey() {
		if(isOverridingThemeActive()) {
			return key_Dark;
		}
		return key;
	}
	
	public void setInstanceScopeValue(TextStyling value) throws CommonException {
		getEffectivePreference().setInstanceScopeValue(value);
	}
	
	/* -----------------  ----------------- */
	
	public IField<TextStyling> asField() {
		return effectiveValue;
	}
	
	@Override
	public TextStyling getFieldValue() {
		return asField().getFieldValue();
	}
	
	
	@Override
	public void addListener(FieldValueListener<TextStyling> listener) {
		asField().addListener(listener);
	}
	
	@Override
	public void removeListener(FieldValueListener<TextStyling> listener) {
		asField().removeListener(listener);
	}
	
	@Override
	public String getPrefId() {
		return key; // Must return same value all the time, can't use getEffectiveBaseKey
	}
	
}