package melnorme.lang.ide.ui;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.preference.IPreferenceStore;

public interface CodeFormatterConstants extends CodeFormatterConstants_Actual {
	
	public final String[] FORMATTER_TAB_CHAR__VALUES = array(
		TAB, SPACES, MIXED 
	);
	
	// helper for tab indent mode
	public enum IndentMode {
		
		TAB(CodeFormatterConstants.TAB),
		SPACES(CodeFormatterConstants.SPACES),
		MIXED(CodeFormatterConstants.MIXED);
		
		protected final String prefValue;
		
		private IndentMode(String prefKey) {
			this.prefValue = prefKey;
		}
		
		public String getPrefValue() {
			return prefValue;
		}
		
		public static IndentMode fromPrefStore(IPreferenceStore store) {
			String indentModePrefValue = store.getString(CodeFormatterConstants.FORMATTER_INDENT_MODE);
			for (IndentMode mode : values()) {
				if(mode.getPrefValue().equals(indentModePrefValue)){
					return mode;
				}
			}
			return TAB;
		}
		
	}
	
}