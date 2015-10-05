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

import melnorme.lang.ide.ui.text.coloring.ColoringItemPreference;

//Note: the file /resources/e4-dark_sourcehighlighting.css needs to updated with changes made here, 
//such as key name changes, or the color defaults
public interface GoColorPreferences {
	
	String PREFIX = "editor.coloring.";
	
    public static final ColoringItemPreference SC__DEFAULT = 
        new ColoringItemPreference(PREFIX + "default", new RGB(0, 0, 0), false, false);
    
	public static final ColoringItemPreference SC__COMMENT = 
		new ColoringItemPreference(PREFIX + "comment", new RGB(107, 127, 147), false, false);
	
	public static final ColoringItemPreference SC__KEYWORD = 
		new ColoringItemPreference(PREFIX + "keyword", new RGB(127, 0, 85), true, false);
	public static final ColoringItemPreference SC__KW_PRIMITIVE = 
		new ColoringItemPreference(PREFIX + "primitive", new RGB(50, 130, 160), true, false);
	public static final ColoringItemPreference SC__KW_LITERAL = 
		new ColoringItemPreference(PREFIX + "value", new RGB(160, 120, 70), false, false);
	public static final ColoringItemPreference SC__BUILTIN_FUNCTION = 
		new ColoringItemPreference(PREFIX + "builtin_function", new RGB(0, 0, 0), true, false);
	public static final ColoringItemPreference SC__OPERATOR = 
		new ColoringItemPreference(PREFIX + "operator", new RGB(0, 0, 0), false, false);
	public static final ColoringItemPreference SC__STRUCTURAL_SYMBOLS = 
		new ColoringItemPreference(PREFIX + "syntax_chars", new RGB(0, 0, 0), false, false);
	
	public static final ColoringItemPreference SC__CHARACTER = 
		new ColoringItemPreference(PREFIX + "character", new RGB(0, 170, 10), false, false);
	public static final ColoringItemPreference SC__STRING = 
		new ColoringItemPreference(PREFIX + "string", new RGB(126, 164, 0), false, false);
	public static final ColoringItemPreference SC__MULTILINE_STRING = 
		new ColoringItemPreference(PREFIX + "multiline_string", new RGB(175, 175, 0), false, false);
	// 230, 125, 105
	
}