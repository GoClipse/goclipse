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
	ColoringItemPreference OPERATORS = new ColoringItemPreference(PREFIX + "operators",
		true, new RGB(120, 0, 127), false, false, false);
	
	ColoringItemPreference STRINGS = new ColoringItemPreference(PREFIX + "string",
		true, new RGB(130, 60, 0), false, false, false);
	
	ColoringItemPreference COMMENTS = new ColoringItemPreference(PREFIX + "comment",
		true, new RGB(100, 100, 100), false, false, false);
	
}