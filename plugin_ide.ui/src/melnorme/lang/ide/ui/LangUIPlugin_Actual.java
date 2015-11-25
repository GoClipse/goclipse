package melnorme.lang.ide.ui;

import java.util.List;

import org.eclipse.jface.text.source.ISourceViewer;

import LANG_PROJECT_ID.ide.core.text.LANGUAGE_AutoEditStrategy;
import LANG_PROJECT_ID.ide.ui.LANGUAGE_Images;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.ui.editor.hover.ILangEditorTextHover;
import melnorme.lang.ide.ui.editor.text.LangAutoEditsPreferencesAccess;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = "LANG_PROJECT_ID.ide.ui";
	
	public static final String ROOT_PREF_PAGE_ID = PLUGIN_ID + ".PreferencePages.Root";
	
	public static final String RULER_CONTEXT = "#LANGUAGE_RulerContext";
	public static final String EDITOR_CONTEXT = "#LANGUAGE_EditorContext";
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "LANG_PROJECT_ID.ide.debug";
	
	protected static final Class<?> PLUGIN_IMAGES_CLASS = LANGUAGE_Images.class;
	
	@SuppressWarnings("unused")
	protected static void initTextHovers_afterProblemHover(
			List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
	}
	
	public static LANGUAGE_AutoEditStrategy createAutoEditStrategy(ISourceViewer sourceViewer, String contentType) {
		return new LANGUAGE_AutoEditStrategy(contentType, sourceViewer, new LangAutoEditsPreferencesAccess());
	}
	
	public static StructureElementLabelProvider getStructureElementLabelProvider() {
		return new StructureElementLabelProvider() {
		};
	}
	
	/* ----------------- UI messages:  ----------------- */
	
	public static final String TOOLS_CONSOLE_NAME = LangCore_Actual.LANGUAGE_NAME + " build";
	
	public static final String DAEMON_TOOL_Name = "lang_daemon";
	public static final String DAEMON_TOOL_ConsoleName = "lang_daemon log";
	
}