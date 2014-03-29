package melnorme.lang.ide.ui;

import java.util.List;

import melnorme.lang.ide.ui.editor.ILangEditorTextHover;

import com.googlecode.goclipse.editors.TextHover;
import com.googlecode.goclipse.ui.GoUIPlugin;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = GoUIPlugin.PLUGIN_ID;
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "com.googlecode.goclipse.debug";
	
	protected static void initTextHovers(List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
		textHoverSpecifications.add(TextHover.class);
	}
	
}