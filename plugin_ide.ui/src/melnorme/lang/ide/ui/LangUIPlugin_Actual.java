package melnorme.lang.ide.ui;

import java.util.List;

import org.eclipse.jface.text.source.ISourceViewer;

import melnorme.lang.ide.ui.editor.ILangEditorTextHover;
import LANG_PROJECT_ID.ide.ui.LANGUAGE_Images;
import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_AutoEditStrategy;

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
	protected static void initTextHovers( List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
	}
	
	public static LANGUAGE_AutoEditStrategy createAutoEditStrategy(ISourceViewer sourceViewer, String contentType) {
		return new LANGUAGE_AutoEditStrategy(contentType, sourceViewer);
	}
	
	/* ----------------- UI messages:  ----------------- */
	
	public static final String LANGUAGE_NAME = "Lang";
	public static final String DAEMON_TOOL_Name = "lang_daemon";
	public static final String DAEMON_TOOL_ConsoleName = "lang_daemon log";
	
}