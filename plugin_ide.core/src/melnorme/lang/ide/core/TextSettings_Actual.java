package melnorme.lang.ide.core;

import melnorme.lang.ide.core.text.LangDocumentPartitionerSetup;

import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.googlecode.goclipse.core.text.GoDocumentSetupParticipant;
import com.googlecode.goclipse.core.text.GoPartitionScanner;
import com.googlecode.goclipse.core.text.GoPartitions;


public class TextSettings_Actual {
	
	public static final String PARTITIONING_ID = GoPartitions.PARTITIONING_ID;
	
	public static final String[] PARTITION_TYPES = GoPartitions.PARTITION_TYPES;
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
	public static LangDocumentPartitionerSetup createDocumentSetupHelper() {
		return new GoDocumentSetupParticipant();
	}
	
}