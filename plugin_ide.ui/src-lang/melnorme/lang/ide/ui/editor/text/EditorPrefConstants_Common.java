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
package melnorme.lang.ide.ui.editor.text;

import org.eclipse.swt.graphics.RGB;

import melnorme.lang.ide.core.utils.prefs.BooleanPreference;
import melnorme.lang.ide.ui.EditorSettings_Actual.EditorPrefConstants;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.ColorPreference;

/** 
 * This interface should not be accessed by name directly, 
 * but rather {@link EditorPrefConstants} should be used instead, 
 * so that the value of certain keys can be overrideden by Lang-specific code if so desired 
 */
public interface EditorPrefConstants_Common {
	
	BooleanPreference MATCHING_BRACKETS_ = new BooleanPreference("editor.matchingBrackets", true);
	ColorPreference MATCHING_BRACKETS_COLOR2 = new ColorPreference("editor.matchingBracketsColor", 
		new RGB(192, 192, 192));
	
	String HIGHLIGHT_BRACKET_AT_CARET_LOCATION = "editor.highlightBracketAtCaretLocation";
	String ENCLOSING_BRACKETS = "editor.enclosingBrackets";
	
	
	BooleanPreference SOURCE_HOVER_BACKGROUND_COLOR_UseSystemDefault = 
			new BooleanPreference(LangUIPlugin.PLUGIN_ID, "SourceHover.bg_color.useSystemDefault", true); 
	
	ColorPreference SOURCE_HOVER_BACKGROUND_COLOR_rgb = 
			new ColorPreference(LangUIPlugin.PLUGIN_ID, "SourceHover.bg_color.rgb", new RGB(255, 255, 255));
	
}