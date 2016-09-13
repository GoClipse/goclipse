package melnorme.lang.ide.ui;

import java.util.List;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core_text.GoDocumentSetupParticipant;
import com.googlecode.goclipse.core_text.GoPartitionScanner;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoStructureElementLabelProvider;
import com.googlecode.goclipse.ui.editor.GoDocTextHover;
import com.googlecode.goclipse.ui.editor.actions.GoFmtEditorOperation;
import com.googlecode.goclipse.ui.editor.text.GoAutoEditStrategy;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.text.format.ILastKeyInfoProvider;
import melnorme.lang.ide.core_text.LangDocumentPartitionerSetup;
import melnorme.lang.ide.ui.editor.hover.ILangEditorTextHover;
import melnorme.lang.ide.ui.editor.text.LangAutoEditsPreferencesAccess;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = "com.googlecode.goclipse.ui";
	
	public static final String ROOT_PREF_PAGE_ID = PLUGIN_ID + ".PreferencePages.Root";
	
	public static final String EDITOR_CONTEXT = "#GoEditorContext";
	public static final String RULER_CONTEXT = "#GoEditorRulerContext";
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "com.googlecode.goclipse.debug";
	
	protected static final Class<?> PLUGIN_IMAGES_CLASS = GoPluginImages.class;
	
	/* ----------------- text ----------------- */
	
	protected static void initTextHovers_afterProblemHover(
			List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
		textHoverSpecifications.add(GoDocTextHover.class);
	}
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
	public static GoAutoEditStrategy createAutoEditStrategy(String contentType, 
		ILastKeyInfoProvider lastKeyInfoProvider) {
		return new GoAutoEditStrategy(contentType, new LangAutoEditsPreferencesAccess(), lastKeyInfoProvider);
	}
	
	public static LangDocumentPartitionerSetup createDocumentSetupHelper() {
		return new GoDocumentSetupParticipant();
	}
	
	public static StructureElementLabelProvider getStructureElementLabelProvider() {
		return new GoStructureElementLabelProvider();
	}
	
	/* ----------------- UI messages:  ----------------- */
	
	public static final String BUILD_ConsoleName = LangCore_Actual.NAME_OF_LANGUAGE + " Build";
	public static final String ENGINE_TOOLS_ConsoleName = LangCore_Actual.NAME_OF_LANGUAGE + " Tools Log";
	
	
	/* -----------------  ----------------- */
	
	public static GoFmtEditorOperation getFormatOperation(ITextEditor editor) {
		return new GoFmtEditorOperation(editor);
	}
	
}