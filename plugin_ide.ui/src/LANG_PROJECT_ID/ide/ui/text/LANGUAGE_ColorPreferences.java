/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.text;

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.text.coloring.TextStyling;
import melnorme.lang.ide.ui.text.coloring.ThemedTextStylingPreference;

public interface LANGUAGE_ColorPreferences {
	
	String PREFIX = "editor.coloring."; 
	
	ThemedTextStylingPreference DEFAULT = new ThemedTextStylingPreference(PREFIX + "default",
		new TextStyling(new RGB(  0,   0,   0), false, false),
		new TextStyling(new RGB(230, 230, 230), false, false));
	ThemedTextStylingPreference KEYWORDS = new ThemedTextStylingPreference(PREFIX + "keyword",
		new TextStyling(new RGB(  0,   0, 127), true, false),
		new TextStyling(new RGB(  0,   0, 127), true, false));
	ThemedTextStylingPreference KEYWORDS_VALUES = new ThemedTextStylingPreference(PREFIX + "keyword_literals",
		new TextStyling(new RGB(  0,  0, 127), false, false),
		new TextStyling(new RGB(127, 64,  64), false, false));
	
	ThemedTextStylingPreference STRINGS = new ThemedTextStylingPreference(PREFIX + LangPartitionTypes.STRING,
		new TextStyling(new RGB(113, 140, 0), false, false),
		new TextStyling(new RGB(113, 140, 0), false, false));
	ThemedTextStylingPreference CHARACTER = new ThemedTextStylingPreference(PREFIX + LangPartitionTypes.CHARACTER,
		new TextStyling(new RGB(113, 140, 0), false, false),
		new TextStyling(new RGB(113, 140, 0), false, false));
	
	ThemedTextStylingPreference COMMENTS = new ThemedTextStylingPreference(PREFIX + "comment",
		new TextStyling(new RGB(100, 100, 100), false, false),
		new TextStyling(new RGB(144, 144, 144), false, false));
	ThemedTextStylingPreference DOC_COMMENTS = new ThemedTextStylingPreference(PREFIX + "doc_comment",
		new TextStyling(new RGB( 80, 100, 150), false, false),
		new TextStyling(new RGB(110, 135, 205), false, false));
	
}