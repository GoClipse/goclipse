package melnorme.lang.ide.ui;

import java.util.List;

import org.eclipse.jface.text.source.ISourceViewer;

import melnorme.lang.ide.ui.editor.ILangEditorTextHover;

import com.googlecode.goclipse.editors.TextHover;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.editor.text.GoAutoEditStrategy;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = GoUIPlugin.PLUGIN_ID;
	
	public static final String EDITOR_CONTEXT = "#GoEditorContext";
	public static final String RULER_CONTEXT = "#GoEditorRulerContext";
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "com.googlecode.goclipse.debug";
	
	protected static final Class<?> PLUGIN_IMAGES_CLASS = GoPluginImages.class;
	
	protected static void initTextHovers(List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
		textHoverSpecifications.add(TextHover.class);
	}
	
	public static GoAutoEditStrategy createAutoEditStrategy(ISourceViewer sourceViewer, String contentType) {
		return new GoAutoEditStrategy(contentType, sourceViewer);
	}
	
}