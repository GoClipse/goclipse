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
import melnorme.lang.ide.ui.text.coloring.ColoringItemPreference;

public interface LANGUAGE_ColorPreferences {
	
	String PREFIX = "editor.coloring."; 
	
	ColoringItemPreference DEFAULT = new ColoringItemPreference(PREFIX + "default",
		new RGB(0, 0, 0), false, false);
	ColoringItemPreference KEYWORDS = new ColoringItemPreference(PREFIX + "keyword",
		new RGB(0, 0, 127), true, false);
	ColoringItemPreference KEYWORDS_VALUES = new ColoringItemPreference(PREFIX + "keyword_literals",
		new RGB(0, 0, 127), false, false);
	
	ColoringItemPreference STRINGS = new ColoringItemPreference(PREFIX + LangPartitionTypes.STRING,
		new RGB(0x71, 0x8C, 0x00), false, false);
	ColoringItemPreference CHARACTER = new ColoringItemPreference(PREFIX + LangPartitionTypes.CHARACTER,
		new RGB(0x71, 0x8C, 0x00), false, false);
	
	ColoringItemPreference COMMENTS = new ColoringItemPreference(PREFIX + "comment",
		new RGB(100, 100, 100), false, false);
	ColoringItemPreference DOC_COMMENTS = new ColoringItemPreference(PREFIX + "doc_comment",
		new RGB(80, 100, 150), false, false);
	
}