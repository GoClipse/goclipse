package melnorme.lang.ide.ui;

import static melnorme.lang.ide.core.text.format.FormatterIndentMode.TAB;

import melnorme.lang.ide.core.utils.prefs.IntPreference;
import melnorme.lang.ide.core.utils.prefs.StringPreference;


public interface CodeFormatterConstants_Actual {
	
	public static final String QUALIFIER = LangUIPlugin.PLUGIN_ID;
	
	
	StringPreference FORMATTER_INDENT_MODE = 
			new StringPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_TAB_CHAR", TAB.toString());
	IntPreference FORMATTER_TAB_SIZE = 
			new IntPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_TAB_SIZE", 4);
	IntPreference FORMATTER_INDENTATION_SPACES_SIZE = 
			new IntPreference(QUALIFIER, "CodeFormatterConstants.FORMATTER_INDENTATION_SIZE", 4);
	
}