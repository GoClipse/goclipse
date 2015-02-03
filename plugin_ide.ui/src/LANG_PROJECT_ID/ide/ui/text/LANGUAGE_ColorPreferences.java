/*******************************************************************************
 * Copyright (c) 2014, 2015 Bruno Medeiros and other Contributors.
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

import melnorme.lang.ide.ui.text.coloring.ColoringItemPreference;

public interface LANGUAGE_ColorPreferences {
	
	String PREFIX = "editor.coloring."; 
	
	ColoringItemPreference DEFAULT = new ColoringItemPreference(PREFIX + "default",
		true, new RGB(0, 0, 0), false, false, false);
	ColoringItemPreference KEYWORDS = new ColoringItemPreference(PREFIX + "keyword",
		true, new RGB(0, 0, 127), true, false, false);
	ColoringItemPreference KEYWORDS_VALUES = new ColoringItemPreference(PREFIX + "keyword_literals",
		true, new RGB(0, 0, 127), false, false, false);
	
	ColoringItemPreference STRINGS = new ColoringItemPreference(PREFIX + "string",
		true, new RGB(130, 60, 0), false, false, false);
	
	ColoringItemPreference COMMENTS = new ColoringItemPreference(PREFIX + "comment",
		true, new RGB(100, 100, 100), false, false, false);
	
}