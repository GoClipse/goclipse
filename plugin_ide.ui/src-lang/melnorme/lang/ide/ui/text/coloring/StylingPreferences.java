/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text.coloring;

import java.util.Map.Entry;

import melnorme.lang.ide.core.utils.prefs.IPreferenceIdentifier;
import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.fields.DomainField;
import melnorme.utilbox.fields.IFieldView;

public class StylingPreferences {
	
	protected final HashMap2<String, IFieldView<TextStyling>> stylingPrefsMap = new HashMap2<>();
	
	public StylingPreferences(ThemedTextStylingPreference... stylingPreferences) {
		for (ThemedTextStylingPreference stylingPreference : stylingPreferences) {
			put(stylingPreference);
		}
	}
	
	public IFieldView<TextStyling> get(IPreferenceIdentifier pref) {
		return get(pref.getPrefId());
	}
	
	public IFieldView<TextStyling> get(String key) {
		return stylingPrefsMap.get(key);
	}
	
	protected void put(ThemedTextStylingPreference pref) {
		stylingPrefsMap.put(pref.getPrefId(), pref);
	}
	
	public static class OverlayStylingPreferences extends StylingPreferences {
		
		protected final StylingPreferences originalStylingPreferences;
		
		public OverlayStylingPreferences(StylingPreferences stylingPreferences) {
			this.originalStylingPreferences = stylingPreferences;
			
			for(Entry<String, IFieldView<TextStyling>> entry : stylingPreferences.stylingPrefsMap) {
				entry.setValue(new SimpleTextStylingPref());
			}
		}
		
		@Override
		public SimpleTextStylingPref get(String key) {
			return (SimpleTextStylingPref) super.get(key);
		}
		
		@Override
		public SimpleTextStylingPref get(IPreferenceIdentifier pref) {
			return (SimpleTextStylingPref) super.get(pref);
		}
		
	}
	
	protected static class SimpleTextStylingPref extends DomainField<TextStyling> {
		
		public SimpleTextStylingPref() {
		}
		
	}
	
}