package melnorme.lang.ide.ui;

import org.eclipse.cdt.internal.ui.text.util.CColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_SimpleSourceViewerConfiguration;
import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_PartitionScanner;


public class TextSettings_Actual {
	
	public static final String PARTITIONING_ID = "LANG_PROJECT_ID.Partitioning";
	
	public static final String[] PARTITION_TYPES = new String[] { 
		LangPartitionTypes.CODE,
		LangPartitionTypes.COMMENT,
		LangPartitionTypes.STRING
	};
	
	public static interface LangPartitionTypes {
		String CODE = IDocument.DEFAULT_CONTENT_TYPE;
		String COMMENT = "comment";
		String STRING = "string";
	}
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new LANGUAGE_PartitionScanner();
	}
	
	public static LANGUAGE_SimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, CColorManager colorManager) {
		return new LANGUAGE_SimpleSourceViewerConfiguration(preferenceStore, colorManager);
	}
	
}