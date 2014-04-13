/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui;

import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.editor.text.LangAutoEditPreferenceConstants;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class GoUIPreferencesInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = LangUIPlugin.getInstance().getPreferenceStore();
		
		// Formatter and auto-indent
		store.setDefault(CodeFormatterConstants.FORMATTER_INDENT_MODE, CodeFormatterConstants.TAB);
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, 4);
		store.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SPACES_SIZE, 4);
		
		
		store.setDefault(LangAutoEditPreferenceConstants.AE_CLOSE_STRINGS, true);
		store.setDefault(LangAutoEditPreferenceConstants.AE_CLOSE_BRACES, true);
		store.setDefault(LangAutoEditPreferenceConstants.AE_CLOSE_BRACKETS, true);
		
		store.setDefault(LangAutoEditPreferenceConstants.AE_SMART_INDENT, true);
		store.setDefault(LangAutoEditPreferenceConstants.AE_SMART_DEINDENT, true);
		store.setDefault(LangAutoEditPreferenceConstants.AE_PARENTHESES_AS_BLOCKS, true);
	}
	
}