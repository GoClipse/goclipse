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
	
	String PREFIX = "editor.coloring2.";
	
    ThemedTextStylingPreference DEFAULT = new ThemedTextStylingPreference(PREFIX + "default", 
    	new TextStyling(new RGB(  0,  0,  0), false, false),
    	new TextStyling(new RGB(230,230,230), false, false));
    
	ThemedTextStylingPreference COMMENT = new ThemedTextStylingPreference(PREFIX + "comment", 
		new TextStyling(new RGB(107, 127, 147), false, false),
    	new TextStyling(new RGB(130, 150, 175), false, false));
	
	ThemedTextStylingPreference KEYWORD = new ThemedTextStylingPreference(PREFIX + "keyword", 
		new TextStyling(new RGB(127, 0,  85), true, false),
		new TextStyling(new RGB(210, 0, 140), true, false));
	ThemedTextStylingPreference KW_NATIVE_TYPES = new ThemedTextStylingPreference(PREFIX + "primitive", 
		new TextStyling(new RGB( 65, 155, 155), false, false),
    	new TextStyling(new RGB( 75, 180, 180), false, false));
	ThemedTextStylingPreference KW_LITERAL = new ThemedTextStylingPreference(PREFIX + "value", 
		new TextStyling(new RGB(165,  90, 255), false, false),
		new TextStyling(new RGB(255, 150,  55), false, false)); // Use orange instead
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
		new TextStyling(new RGB( 0, 170, 10), false, false),
		new TextStyling(new RGB(10, 210, 60), false, false));
	ThemedTextStylingPreference CHARACTER = new ThemedTextStylingPreference(PREFIX + "character", 
		new TextStyling(new RGB(140, 175, 0), false, false),
    	new TextStyling(new RGB(150, 190, 0), false, false));
	ThemedTextStylingPreference STRING = new ThemedTextStylingPreference(PREFIX + "string", 
		new TextStyling(new RGB(175, 175, 0), false, false),
		new TextStyling(new RGB(220, 220, 0), false, false));
	ThemedTextStylingPreference MULTILINE_STRING = new ThemedTextStylingPreference(PREFIX + "multiline_string", 
		new TextStyling(new RGB(175, 175, 0), false, false),
		new TextStyling(new RGB(220, 220, 0), false, false));
	
}