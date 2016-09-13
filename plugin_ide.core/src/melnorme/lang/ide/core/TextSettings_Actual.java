package melnorme.lang.ide.core;

import org.eclipse.jface.text.IDocument;

import melnorme.utilbox.misc.ArrayUtil;


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
	
	/* ----------------- Common code ----------------- */
	
	public static final String[] PARTITION_TYPES = ArrayUtil.map(LangPartitionTypes.values(), 
		obj -> obj.getId(), String.class
	);
	
}