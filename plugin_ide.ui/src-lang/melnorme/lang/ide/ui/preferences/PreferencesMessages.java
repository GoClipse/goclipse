package melnorme.lang.ide.ui.preferences;

import melnorme.lang.ide.ui.PreferencesMessages_Actual;

/**
 * These fields are in a class and not an interface, so that non-Lang code could change them, if necessary.
 * The messages that are expected to be IDE specific are in {@link PreferencesMessages_Actual} though.
 */
public class PreferencesMessages implements PreferencesMessages_Actual {
	
	public static final String LABEL_UseProjectSpecificSettings = "Use project specific settings";
	public static final String LABEL_ConfigureWorkspaceSettings = "Configure workspace settings";
	
	/* -----------------  ----------------- */
	
	public static String EditorPreferencePage_tabAlwaysIndent =
			"Tab always indents line";
	
	
	public static String EditorPreferencePage_title1 = 
			"Appearance color options:";
	public static String EditorPreferencePage_matchingBrackets =
			"Highlight &matching brackets";
	
	public static String EditorPreferencePage_matchingBracketsHighlightColor =
			"Matching brackets highlight";
	public static String EditorPreferencePage_backgroundForMethodParameters = 
			"Parameter hints background";
	public static String EditorPreferencePage_foregroundForMethodParameters = 
			"Parameter hints foreground";
	public static String EditorPreferencePage_backgroundForCompletionReplacement = 
			"Completion overwrite background";
	public static String EditorPreferencePage_foregroundForCompletionReplacement =
			"Completion overwrite foreground";
	public static String EditorPreferencePage_sourceHoverBackgroundColor = 
			"Source hover background";
	public static String EditorPreferencePage_color = 
			"C&olor:";
	public static String EditorPreferencePage_systemDefault = 
			"&System Default";


	public static String DLTKEditorColoringConfigurationBlock_link = "Default colors and font can be configured on "
			+ "the <a href=\"org.eclipse.ui.preferencePages.GeneralTextEditor\">Text Editors</a> and on "
			+ "the <a href=\"org.eclipse.ui.preferencePages.ColorsAndFonts\">Colors and Fonts</a> preference page.";
	
	public static String DLTKEditorPreferencePage_coloring_element = "Source coloring element:";
	public static String DLTKEditorPreferencePage_enable = "Enable";
	public static String DLTKEditorPreferencePage_color = "Color:";
	public static String DLTKEditorPreferencePage_bold = "Bold";
	public static String DLTKEditorPreferencePage_italic = "Italic";
	public static String DLTKEditorPreferencePage_strikethrough = "Strikethrough";
	public static String DLTKEditorPreferencePage_underline = "Underline";
	public static String DLTKEditorPreferencePage_preview = "Preview:";
	
	
	public static final String LangSmartTypingConfigurationBlock_autoclose_title = 
		"Automatically close:";
	public static final String LangSmartTypingConfigurationBlock_closeBrackets =
		"(Parentheses),[square] brackets";
	public static final String LangSmartTypingConfigurationBlock_closeBraces =
		"{Braces}";
	public static final String LangSmartTypingConfigurationBlock_closeStrings =
		"\"Strings\"";
	
	
	public static final String EditorPreferencePage_AutoEdits = 
		"Auto Indent";
	public static final String EditorPreferencePage_smartIndent = 
		"Smart indent on newline";
	public static final String EditorPreferencePage_smartDeIndent= 
		"Smart indent deletion (delete full indent)";
	public static final String EditorPreferencePage_considerParenthesesAsBlocks = 
		"Consider (parentheses) the same as {braces} for block smart indent";
	
	/* ----------------- Content Assist ----------------- */
	
	public static final String LangPrefs_ContentAssist_Insertion_group = 
			"Insertion:";
	public static final String LangPrefs_ContentAssist_Insertion_AutomaticSingleProposals_Label = 
			"Insert single proposals automatically";
	public static final String LangPrefs_ContentAssist_Insertion_AutomaticCommonPrefixes_Label = 
			"Insert common prefixes automatically";
	
	public static final String LangPrefs_ContentAssist_AutoActivation_group = 
			"Auto-Activation:";
	public static final String LangPrefs_ContentAssist_AutoActivation_DotTrigger_Label = 
			"Enable \".\" as trigger";
	public static final String LangPrefs_ContentAssist_AutoActivation_DoubleColonTrigger_Label = 
			"Enable \"::\" as trigger";
	public static final String LangPrefs_ContentAssist_AutoActivation_AlphanumericTrigger_Label = 
			"Enable alphabetic characters as trigger";
	public static final String LangPrefs_ContentAssist_AutoActivation_Delay_Label = 
			"Delay (ms): ";
	
}