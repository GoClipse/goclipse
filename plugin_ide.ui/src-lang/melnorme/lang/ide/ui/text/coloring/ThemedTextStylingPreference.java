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

import org.osgi.service.event.Event;

import melnorme.lang.ide.ui.LangUI;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IFieldValueListener;

public class ThemedTextStylingPreference implements ITextStylingPref {
	
	protected final String key;
	
	protected final TextStylingPreference defaultPref;
	protected final TextStylingPreference darkPref;
	
	protected final DomainField<TextStyling> effectiveValue = new DomainField<>();
	
	public ThemedTextStylingPreference(String key, TextStyling styling, TextStyling defaultValueDark) {
		this(LangUIPlugin.PLUGIN_ID, key, styling, defaultValueDark);
	}
	
	public ThemedTextStylingPreference(String qualifer, String key, TextStyling defaultValue, 
			TextStyling defaultValueDark) {
		
		this.key = key;
		
		this.defaultPref = new TextStylingPreference(qualifer, key, defaultValue);
		this.darkPref = new TextStylingPreference(qualifer, key + "#dark#", defaultValueDark);
		
		LangUI.getInstance().getThemeHelper().new ThemeChangeListener() {
			@Override
			public void handleEvent(Event event) {
				effectiveValue.setFieldValue(getEffectiveValue());
			}
		};
		
		effectiveValue.setFieldValue(getEffectiveValue());
	}
	
	
	/* -----------------  ----------------- */
	
	protected DomainField<TextStyling> getGlobalField() {
		return effectiveValue;
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
	
	/* -----------------  ----------------- */
	
	protected TextStyling getEffectiveValue() {
		if(isOverridingThemeActive()) {
			return darkPref.get();
		}
		return defaultPref.get();
	}
	
	protected boolean isOverridingThemeActive() {
		return LangUI.getInstance().getThemeHelper().getIdOfActiveThemeForCurrentDisplay().contains("dark");
	}
	
	public TextStyling getDefault() {
		if(isOverridingThemeActive()) {
			return darkPref.getDefault();
		}
		return defaultPref.getDefault();
	}
	
}