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
	
	public StylingPreferences(ThemedTextStylingPreference... stylingPreferences) {
		for (ThemedTextStylingPreference stylingPreference : stylingPreferences) {
			put(stylingPreference);
		}
	}
	
	public ITextStylingPref get(ITextStylingPref pref) {
		return get(pref.getPrefId());
	}
	
	public ITextStylingPref get(String key) {
		return stylingPrefsMap.get(key);
	}
	
	public void put(ITextStylingPref pref) {
		stylingPrefsMap.put(pref.getPrefId(), pref);
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
		
		protected final String id;
		
		public SimpleTextStylingPref(String id) {
			this.id = id;
		}
		
		@Override
		public String getPrefId() {
			return id;
		}
		
	}
	
}