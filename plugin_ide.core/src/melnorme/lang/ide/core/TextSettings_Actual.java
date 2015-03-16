package melnorme.lang.ide.core;

import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import LANG_PROJECT_ID.ide.core.text.LANGUAGE_DocumentSetupParticipant;
import LANG_PROJECT_ID.ide.core.text.LANGUAGE_PartitionScanner;


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
	
	public static LangDocumentPartitionerSetup createDocumentSetupHelper() {
		return new LANGUAGE_DocumentSetupParticipant();
	}
	
}