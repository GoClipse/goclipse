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

import melnorme.lang.ide.ui.text.coloring.TextStyling;
import melnorme.lang.ide.ui.text.coloring.ThemedTextStylingPreference;

public interface GoColorPreferences {
	
	String PREFIX = "editor.coloring.";
	
    ThemedTextStylingPreference DEFAULT = new ThemedTextStylingPreference(PREFIX + "default", 
    	new TextStyling(new RGB(  0,  0,  0), false, false),
    	new TextStyling(new RGB(230,230,230), false, false));
    
	ThemedTextStylingPreference COMMENT = new ThemedTextStylingPreference(PREFIX + "comment", 
		new TextStyling(new RGB(107, 127, 147), false, false),
    	new TextStyling(new RGB(107, 127, 147), false, false));
	
	ThemedTextStylingPreference KEYWORD = new ThemedTextStylingPreference(PREFIX + "keyword", 
		new TextStyling(new RGB(127, 0, 85), true, false),
    	new TextStyling(new RGB(127, 0, 85), true, false));
	ThemedTextStylingPreference KW_PRIMITIVE = new ThemedTextStylingPreference(PREFIX + "primitive", 
		new TextStyling(new RGB(50, 130, 160), true, false),
    	new TextStyling(new RGB(50, 130, 160), true, false));
	ThemedTextStylingPreference KW_LITERAL = new ThemedTextStylingPreference(PREFIX + "value", 
		new TextStyling(new RGB(160, 120, 70), false, false),
    	new TextStyling(new RGB(160, 120, 70), false, false));
	ThemedTextStylingPreference BUILTIN_FUNCTION = new ThemedTextStylingPreference(PREFIX + "builtin_function", 
		new TextStyling(new RGB(  0,  0,  0), true, false),
    	new TextStyling(new RGB(230,230,230), true, false));
	ThemedTextStylingPreference OPERATOR = new ThemedTextStylingPreference(PREFIX + "operator", 
		new TextStyling(new RGB(  0,  0,  0), false, false),
    	new TextStyling(new RGB(230,230,230), false, false));
	ThemedTextStylingPreference STRUCTURAL_SYMBOLS = new ThemedTextStylingPreference(PREFIX + "syntax_chars", 
		new TextStyling(new RGB(  0,  0,  0), false, false),
    	new TextStyling(new RGB(230,230,230), false, false));
	
	ThemedTextStylingPreference NUMBER = new ThemedTextStylingPreference(PREFIX + "number",
		new TextStyling(new RGB(126, 160, 0), false, false),
		new TextStyling(new RGB(126, 160, 0), false, false));
	ThemedTextStylingPreference CHARACTER = new ThemedTextStylingPreference(PREFIX + "character", 
		new TextStyling(new RGB(0, 170, 10), false, false),
    	new TextStyling(new RGB(0, 170, 10), false, false));
	ThemedTextStylingPreference STRING = new ThemedTextStylingPreference(PREFIX + "string", 
		new TextStyling(new RGB(126, 164, 0), false, false),
    	new TextStyling(new RGB(126, 164, 0), false, false));
	ThemedTextStylingPreference MULTILINE_STRING = new ThemedTextStylingPreference(PREFIX + "multiline_string", 
		new TextStyling(new RGB(175, 175, 0), false, false),
    	new TextStyling(new RGB(175, 175, 0), false, false));
	// 230, 125, 105
	
}