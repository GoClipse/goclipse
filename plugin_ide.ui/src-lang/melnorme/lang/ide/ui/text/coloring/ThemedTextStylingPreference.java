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

import melnorme.lang.ide.ui.LangUI;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IFieldValueListener;

public class ThemedTextStylingPreference implements ITextStylingPref {
	
	protected final String key;
	protected final String key_Dark;
	
	protected final TextStylingPreference defaultThemePref;
	protected final TextStylingPreference darkPref;
	
	protected final DomainField<TextStyling> effectiveValue = new DomainField<>();
	
	public ThemedTextStylingPreference(String key, TextStyling defaultValue, TextStyling defaultValueDark) {
		this(LangUIPlugin.PLUGIN_ID, key, defaultValue, defaultValueDark);
	}
	
	public ThemedTextStylingPreference(String qualifer, String key, TextStyling defaultValue, 
			TextStyling defaultValueDark) {
		
		this.key = key;
		this.key_Dark = key + "#dark#";
		
		this.defaultThemePref = new TextStylingPreference(qualifer, key, defaultValue);
		this.darkPref = new TextStylingPreference(qualifer, key_Dark, defaultValueDark);
		
		LangUI.getInstance().getThemeHelper().new ThemeChangeListener() {
			@Override
			public void handleEvent(Event event) {
				updateEffectiveValue();
			}
		};
		
		defaultThemePref.getGlobalField().addListener(() -> updateEffectiveValue());
		darkPref.getGlobalField().addListener(() -> updateEffectiveValue());
		
		updateEffectiveValue();
	}
	
	public TextStylingPreference getDefaultThemePref() {
		return defaultThemePref;
	}
	
	public TextStylingPreference getDarkPref() {
		return darkPref;
	}
	
	/* -----------------  ----------------- */
	
	protected void updateEffectiveValue() {
		effectiveValue.setFieldValue(getEffectiveValue());
	}
	
	protected TextStyling getEffectiveValue() {
		if(isOverridingThemeActive()) {
			return darkPref.get();
		}
		return defaultThemePref.get();
	}
	
	protected boolean isOverridingThemeActive() {
		assertTrue(Display.getCurrent() != null);
		return LangUI.getInstance().getThemeHelper().getIdOfActiveThemeForCurrentDisplay().contains("dark");
	}
	
	public TextStyling getDefault() {
		if(isOverridingThemeActive()) {
			return darkPref.getDefault();
		}
		return defaultThemePref.getDefault();
	}
	
	public String getActiveKey() {
		if(isOverridingThemeActive()) {
			return key_Dark;
		}
		return key;
	}
	
	/* -----------------  ----------------- */
	
	protected DomainField<TextStyling> getGlobalField() {
		return effectiveValue;
	}
	
	public TextStyling getValue() {
		return getFieldValue();
	}
	
	@Override
	public TextStyling getFieldValue() {
		return getGlobalField().getFieldValue();
	}
	
	@Override
	public void setFieldValue(TextStyling value) {
		getGlobalField().setFieldValue(value);
	}
	
	@Override
	public void addValueChangedListener(IFieldValueListener listener) {
		getGlobalField().addValueChangedListener(listener);
	}
	
	@Override
	public void removeValueChangedListener(IFieldValueListener listener) {
		getGlobalField().removeValueChangedListener(listener);
	}
	
	@Override
	public String getPrefId() {
		return key; // Must return same value all the time, can't use getEffectiveBaseKey
	}
	
}