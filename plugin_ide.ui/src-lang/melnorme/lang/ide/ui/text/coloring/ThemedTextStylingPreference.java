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
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.utils.prefs.IPreferenceIdentifier;
import melnorme.lang.ide.ui.LangUI;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IModelField;
import melnorme.utilbox.fields.IFieldValueListener;
import melnorme.utilbox.fields.IFieldView;

public class ThemedTextStylingPreference implements IFieldView<TextStyling>, IPreferenceIdentifier {
	
	protected final String key;
	protected final String key_Dark;
	
	protected final TextStylingPreference defaultPref;
	protected final TextStylingPreference darkPref;
	
	protected final DomainField<TextStyling> effectiveValue = new DomainField<>();
	
	public ThemedTextStylingPreference(String key, TextStyling defaultValue, TextStyling defaultValueDark) {
		this(LangUIPlugin.PLUGIN_ID, key, defaultValue, defaultValueDark);
	}
	
	public ThemedTextStylingPreference(String qualifer, String key, TextStyling defaultValue, 
			TextStyling defaultValueDark) {
		
		this.key = key;
		this.key_Dark = key + "#dark#";
		
		this.defaultPref = new TextStylingPreference(qualifer, key, defaultValue);
		this.darkPref = new TextStylingPreference(qualifer, key_Dark, defaultValueDark);
		
		defaultPref.asField().registerListener(() -> updateEffectiveValue());
		darkPref.asField().registerListener(() -> updateEffectiveValue());
		
		LangUI.getInstance().getThemeHelper().new ThemeChangeListener() {
			@Override
			public void handleEvent(Event event) {
				updateEffectiveValue();
			}
		};
		
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
		effectiveValue.setFieldValue(getEffectivePreference().getFromPrefStore());
	}
	
	protected TextStyling getEffectiveValue() {
		assertTrue(getEffectivePreference().get() == effectiveValue.getValue());
		return effectiveValue.getValue();
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
	
	public void setInstanceScopeValue(TextStyling value) throws BackingStoreException {
		getEffectivePreference().setInstanceScopeValue(value);
	}
	
	/* -----------------  ----------------- */
	
	public IModelField<TextStyling> asField() {
		return effectiveValue;
	}
	
	@Override
	public TextStyling getFieldValue() {
		return asField().getFieldValue();
	}
	
	
	@Override
	public void addListener(IFieldValueListener listener) {
		asField().addListener(listener);
	}
	
	@Override
	public void removeListener(IFieldValueListener listener) {
		asField().removeListener(listener);
	}
	
	@Override
	public String getPrefId() {
		return key; // Must return same value all the time, can't use getEffectiveBaseKey
	}
	
}