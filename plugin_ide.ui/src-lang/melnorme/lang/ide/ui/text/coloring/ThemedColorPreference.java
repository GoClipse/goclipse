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

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.IGlobalPreference;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.fields.DomainField;

public class ThemedColorPreference extends ThemedTextStylingPreference implements IGlobalPreference<TextStyling> {
	
	public ThemedColorPreference(String key, RGB defaultValue, RGB defaultValueDark) {
		this(LangUIPlugin.PLUGIN_ID, key, new TextStyling(defaultValue), new TextStyling(defaultValueDark));
	}
	
	public ThemedColorPreference(String qualifer, String key, TextStyling defaultValue, TextStyling defaultValueDark) {
		super(qualifer, key, defaultValue, defaultValueDark);
	}
	
	@Override
	public DomainField<TextStyling> getGlobalField() {
		return super.getGlobalField();
	}
	
}