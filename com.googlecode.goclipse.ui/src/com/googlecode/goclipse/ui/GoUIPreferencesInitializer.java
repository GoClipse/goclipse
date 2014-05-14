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
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.swt.SWT;

import com.googlecode.goclipse.core.GoCore;
import com.googlecode.goclipse.editors.IColorDefaults;

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
		
		
		IEclipsePreferences coreDefaults = DefaultScope.INSTANCE.getNode(GoCore.PLUGIN_ID);
		
		coreDefaults.putBoolean(GoUIPreferenceConstants.FIELD_USE_HIGHLIGHTING, true);
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR, StringConverter.asString(IColorDefaults.KEYWORD));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_VALUE_COLOR, StringConverter.asString(IColorDefaults.VALUE));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR, StringConverter.asString(IColorDefaults.PRIMITIVE));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR, StringConverter.asString(IColorDefaults.COMMENT));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, StringConverter.asString(IColorDefaults.BUILTIN_FUNCTION));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_STRING_COLOR, StringConverter.asString(IColorDefaults.STRING));
		coreDefaults.put(GoUIPreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR, StringConverter.asString(IColorDefaults.MULTILINE_STRING));
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_KEYWORD_STYLE,          SWT.BOLD           );
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_VALUE_STYLE,            SWT.BOLD|SWT.ITALIC);
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_PRIMITIVE_STYLE,        SWT.ITALIC         );
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_COMMENT_STYLE,          SWT.NORMAL         );
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_STYLE, SWT.BOLD           );
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_STRING_STYLE,           SWT.NORMAL         );
		coreDefaults.putInt(GoUIPreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_STYLE, SWT.NORMAL         );
	}
	
}