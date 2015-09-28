package melnorme.lang.ide.ui;

import melnorme.lang.ide.core.text.format.FormatterIndentMode;

public interface CodeFormatterConstants extends CodeFormatterConstants_Actual {
	
	public static FormatterIndentMode fromPrefStore() {
		String indentModePrefValue = CodeFormatterConstants.FORMATTER_INDENT_MODE.get();
		for(FormatterIndentMode mode : FormatterIndentMode.values()) {
			if(mode.getPrefValue().equals(indentModePrefValue)){
				return mode;
			}
		}
		return FormatterIndentMode.TAB;
	}
	
}