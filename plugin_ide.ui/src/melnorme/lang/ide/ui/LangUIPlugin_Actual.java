package melnorme.lang.ide.ui;

import java.util.List;

import melnorme.lang.ide.ui.editor.ILangEditorTextHover;
import melnorme.utilbox.misc.ArrayUtil;
import mmrnmhrm.ui.LANGUAGE_Images;
import mmrnmhrm.ui.text.LANGUAGE_PartitionScanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = "MMRNMHRM_ID.ide.ui";
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "MMRNMHRM_ID.ide.debug";
	
	protected static final Class<?> PLUGIN_IMAGES_CLASS = LANGUAGE_Images.class;
	
	@SuppressWarnings("unused")
	protected static void initTextHovers( List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
	}
	
	public static final String[] PARTITION_TYPES = new String[] { 
		IDocument.DEFAULT_CONTENT_TYPE, 
	};
	
	public static final String LANG_PARTITIONING = "__Mmrnmhrm_PARTITIONING_ID";
	public static final String[] LEGAL_CONTENT_TYPES = 
			ArrayUtil.remove(PARTITION_TYPES, IDocument.DEFAULT_CONTENT_TYPE);
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new LANGUAGE_PartitionScanner();
	}
	
}