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
package com.googlecode.goclipse.ui.text;

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.ui.text.coloring.TextStylingPreference;

//Note: the file /resources/e4-dark_sourcehighlighting.css needs to updated with changes made here, 
//such as key name changes, or the color defaults
public interface GoColorPreferences {
	
	String PREFIX = "editor.coloring.";
	
    public static final TextStylingPreference SC__DEFAULT = 
        new TextStylingPreference(PREFIX + "default", new RGB(0, 0, 0), false, false);
    
	public static final TextStylingPreference SC__COMMENT = 
		new TextStylingPreference(PREFIX + "comment", new RGB(107, 127, 147), false, false);
	
	public static final TextStylingPreference SC__KEYWORD = 
		new TextStylingPreference(PREFIX + "keyword", new RGB(127, 0, 85), true, false);
	public static final TextStylingPreference SC__KW_PRIMITIVE = 
		new TextStylingPreference(PREFIX + "primitive", new RGB(50, 130, 160), true, false);
	public static final TextStylingPreference SC__KW_LITERAL = 
		new TextStylingPreference(PREFIX + "value", new RGB(160, 120, 70), false, false);
	public static final TextStylingPreference SC__BUILTIN_FUNCTION = 
		new TextStylingPreference(PREFIX + "builtin_function", new RGB(0, 0, 0), true, false);
	public static final TextStylingPreference SC__OPERATOR = 
		new TextStylingPreference(PREFIX + "operator", new RGB(0, 0, 0), false, false);
	public static final TextStylingPreference SC__STRUCTURAL_SYMBOLS = 
		new TextStylingPreference(PREFIX + "syntax_chars", new RGB(0, 0, 0), false, false);
	
	public static final TextStylingPreference SC__CHARACTER = 
		new TextStylingPreference(PREFIX + "character", new RGB(0, 170, 10), false, false);
	public static final TextStylingPreference SC__STRING = 
		new TextStylingPreference(PREFIX + "string", new RGB(126, 164, 0), false, false);
	public static final TextStylingPreference SC__MULTILINE_STRING = 
		new TextStylingPreference(PREFIX + "multiline_string", new RGB(175, 175, 0), false, false);
	// 230, 125, 105
	
}