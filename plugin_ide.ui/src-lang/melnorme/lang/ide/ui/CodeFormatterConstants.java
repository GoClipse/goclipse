package melnorme.lang.ide.ui;


public interface CodeFormatterConstants extends CodeFormatterConstants_Actual {
	
	// helper for tab indent mode
	public enum IndentMode {
		
		TAB(CodeFormatterConstants.TAB),
		SPACES(CodeFormatterConstants.SPACES);
		
		protected final String prefValue;
		
		private IndentMode(String prefKey) {
			this.prefValue = prefKey;
		}
		
		public String getPrefValue() {
			return prefValue;
		}
		
		public static IndentMode fromPrefStore() {
			String indentModePrefValue = CodeFormatterConstants.FORMATTER_INDENT_MODE.get();
			for (IndentMode mode : values()) {
				if(mode.getPrefValue().equals(indentModePrefValue)){
					return mode;
				}
			}
			return TAB;
		}
		
	}
	
}