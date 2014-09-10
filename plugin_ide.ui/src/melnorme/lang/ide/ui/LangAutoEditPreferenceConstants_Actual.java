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
package melnorme.lang.ide.ui;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;



public interface LangAutoEditPreferenceConstants_Actual {
	
	public static final String QUALIFIER = LangUIPlugin.PLUGIN_ID;
	
	BooleanPreference AE_CLOSE_STRINGS = 
			new BooleanPreference(QUALIFIER, "EDITOR_CLOSE_STRINGS", true);
	BooleanPreference AE_CLOSE_BRACKETS = 
			new BooleanPreference(QUALIFIER, "EDITOR_CLOSE_BRACKETS", true);
	BooleanPreference AE_CLOSE_BRACES = 
			new BooleanPreference(QUALIFIER, "EDITOR_CLOSE_BRACES", true);
	
	BooleanPreference AE_SMART_INDENT = 
			new BooleanPreference(QUALIFIER, "autoedit.editorSmartIndent", true);
	BooleanPreference AE_SMART_DEINDENT = 
			new BooleanPreference(QUALIFIER, "autoedit.smart_deindent", true);
	BooleanPreference AE_PARENTHESES_AS_BLOCKS = 
			new BooleanPreference(QUALIFIER, "autoedit.parentheses_as_blocks", true);
	
	// Not used currently?
	BooleanPreference AE_SMART_PASTE = 
			new BooleanPreference(QUALIFIER, "autoedit.smartPaste", true);
	
}