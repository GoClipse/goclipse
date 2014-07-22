package melnorme.lang.ide.ui;

import java.util.List;

import melnorme.lang.ide.ui.editor.ILangEditorTextHover;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.text.IDocument;

import com.googlecode.goclipse.editors.TextHover;
import com.googlecode.goclipse.ui.GoPluginImages;
import com.googlecode.goclipse.ui.GoUIPlugin;
import com.googlecode.goclipse.ui.text.GoPartitions;
import com.googlecode.goclipse.ui.text.GoPartitionScanner;

/**
 * Actual/concrete IDE constants and other bindings, for Lang UI code. 
 */
public final class LangUIPlugin_Actual {
	
	public static final String PLUGIN_ID = GoUIPlugin.PLUGIN_ID;
	
	// ID to start the debug plugin automatically, if present
	protected static final String DEBUG_PLUGIN_ID = "com.googlecode.goclipse.debug";
	
	protected static final Class<?> PLUGIN_IMAGES_CLASS = GoPluginImages.class;
	
	protected static void initTextHovers(List<Class<? extends ILangEditorTextHover<?>>> textHoverSpecifications) {
		textHoverSpecifications.add(TextHover.class);
	}
	
	public static final String LANG_PARTITIONING = GoPartitions.PARTITIONING_ID; 
	public static final String[] LEGAL_CONTENT_TYPES = 
			ArrayUtil.remove(GoPartitions.PARTITION_TYPES, IDocument.DEFAULT_CONTENT_TYPE);
	
	public static GoPartitionScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
}