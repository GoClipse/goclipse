package melnorme.lang.ide.core;

import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.googlecode.goclipse.core.text.GoDocumentSetupParticipant;
import com.googlecode.goclipse.core.text.GoPartitionScanner;


public class TextSettings_Actual {
	
	public static final String PARTITIONING_ID = "___go_partioning";
	
	public static enum LangPartitionTypes {
		CODE, 
		LINE_COMMENT,
		BLOCK_COMMENT,
		CHARACTER,
		STRING,
		MULTILINE_STRING; 
		
		public String getId() {
			if(ordinal() == 0) {
				return IDocument.DEFAULT_CONTENT_TYPE;
			}
			return toString();
		}
		
	}
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
	public static LangDocumentPartitionerSetup createDocumentSetupHelper() {
		return new GoDocumentSetupParticipant();
	}
	
	/* ----------------- Common code ----------------- */
	
	public static final String[] PARTITION_TYPES = ArrayUtil.map(LangPartitionTypes.values(), 
		obj -> obj.getId(), String.class
	);
	
}