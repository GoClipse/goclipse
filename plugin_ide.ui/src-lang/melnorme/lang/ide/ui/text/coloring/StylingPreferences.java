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

import melnorme.utilbox.collections.HashMap2;
import melnorme.utilbox.fields.DomainField;

public class StylingPreferences {
	
	protected final HashMap2<String, ITextStylingPref> stylingPrefsMap = new HashMap2<>();
	
	protected final ITextStylingPref DUMMY = null;
	
	public StylingPreferences(TextStylingPreference... stylingPreferences) {
		for (TextStylingPreference stylingPreference : stylingPreferences) {
			put(stylingPreference);
		}
	}
	
	public ITextStylingPref get(TextStylingPreference pref) {
		return get(pref.key);
	}
	
	public ITextStylingPref get(String key) {
		return stylingPrefsMap.get(key);
	}
	
	public void put(ITextStylingPref pref) {
		stylingPrefsMap.put(pref.getKey(), pref);
	}
	
	public static class OverlayStylingPreferences extends StylingPreferences {
		
		protected final StylingPreferences originalStylingPreferences;
		
		public OverlayStylingPreferences(StylingPreferences stylingPreferences) {
			this.originalStylingPreferences = stylingPreferences;
			
			for(Entry<String, ITextStylingPref> entry : stylingPreferences.stylingPrefsMap) {
				put(new SimpleTextStylingPref(entry.getKey()));
			}
		}
		
		public void saveToOriginal() {
			for (Entry<String, ITextStylingPref> entry : stylingPrefsMap) {
				TextStyling fieldValue = entry.getValue().getFieldValue();
				originalStylingPreferences.get(entry.getKey()).setFieldValue(fieldValue);
			}
		}
		
	}
	
	protected static class SimpleTextStylingPref extends DomainField<TextStyling> implements ITextStylingPref {
		
		protected final String key;
		
		public SimpleTextStylingPref(String key) {
			this.key = key;
		}
		
		@Override
		public String getKey() {
			return key;
		}
		
	}
	
}