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
import melnorme.lang.ide.ui.text.coloring.LangColoringPreferencesHelper;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.graphics.RGB;

public class GoUIPreferencesInitializer extends AbstractPreferenceInitializer 
	implements GoUIPreferenceConstants {

	@Override
	public void initializeDefaultPreferences() {
		final IPreferenceStore store = LangUIPlugin.getInstance().getPreferenceStore();
		
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
		
		
		new LangColoringPreferencesHelper() {
			public void initialize() {
				setColoringStyle(store, SYNTAX_COLORING__TEXT, new RGB(0, 0, 0), false, false, false);
				setColoringStyle(store, SYNTAX_COLORING__KEYWORD, new RGB(127, 0, 85), true, false, false);
				setColoringStyle(store, SYNTAX_COLORING__VALUE, new RGB(127, 0, 85), true, true, false);
				setColoringStyle(store, SYNTAX_COLORING__PRIMITIVE, new RGB(127, 0, 85), false, true, false);
				setColoringStyle(store, SYNTAX_COLORING__COMMENT, new RGB(63, 127, 95), false, false, false);
				setColoringStyle(store, SYNTAX_COLORING__BUILTIN_FUNCTION, new RGB(0, 0, 0), true, false, false);
				setColoringStyle(store, SYNTAX_COLORING__STRING, new RGB(0, 75, 200), false, false, false);
				setColoringStyle(store, SYNTAX_COLORING__MULTILINE_STRING, new RGB(230, 75, 0), false, false, false);
			}
		}.initialize();
		
		
	    store.setDefault(EDITOR_MATCHING_BRACKETS, true);
	    store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, StringConverter.asString(new RGB(128, 128, 128)));
	    
	}
	
}
