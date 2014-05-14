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
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;

import com.googlecode.goclipse.editors.IColorDefaults;

public class GoUIPreferencesInitializer extends AbstractPreferenceInitializer 
	implements GoUIPreferenceConstants {

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
		
		
		store.setDefault(FIELD_USE_HIGHLIGHTING, true);
		store.setDefault(FIELD_SYNTAX_KEYWORD_COLOR, StringConverter.asString(IColorDefaults.KEYWORD));
		store.setDefault(FIELD_SYNTAX_VALUE_COLOR, StringConverter.asString(IColorDefaults.VALUE));
		store.setDefault(FIELD_SYNTAX_PRIMITIVE_COLOR, StringConverter.asString(IColorDefaults.PRIMITIVE));
		store.setDefault(FIELD_SYNTAX_COMMENT_COLOR, StringConverter.asString(IColorDefaults.COMMENT));
		store.setDefault(FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, StringConverter.asString(IColorDefaults.BUILTIN_FUNCTION));
		store.setDefault(FIELD_SYNTAX_STRING_COLOR, StringConverter.asString(IColorDefaults.STRING));
		store.setDefault(FIELD_SYNTAX_MULTILINE_STRING_COLOR, StringConverter.asString(IColorDefaults.MULTILINE_STRING));
		store.setDefault(FIELD_SYNTAX_KEYWORD_STYLE,          SWT.BOLD           );
		store.setDefault(FIELD_SYNTAX_VALUE_STYLE,            SWT.BOLD|SWT.ITALIC);
		store.setDefault(FIELD_SYNTAX_PRIMITIVE_STYLE,        SWT.ITALIC         );
		store.setDefault(FIELD_SYNTAX_COMMENT_STYLE,          SWT.NORMAL         );
		store.setDefault(FIELD_SYNTAX_BUILTIN_FUNCTION_STYLE, SWT.BOLD           );
		store.setDefault(FIELD_SYNTAX_STRING_STYLE,           SWT.NORMAL         );
		store.setDefault(FIELD_SYNTAX_MULTILINE_STRING_STYLE, SWT.NORMAL         );
		
	    store.setDefault(EDITOR_MATCHING_BRACKETS, true);
	    store.setDefault(EDITOR_MATCHING_BRACKETS_COLOR, StringConverter.asString(new RGB(128, 128, 128)));
	    
	}
	
}