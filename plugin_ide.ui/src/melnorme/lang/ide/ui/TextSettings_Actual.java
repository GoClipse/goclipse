package melnorme.lang.ide.ui;

import org.eclipse.cdt.internal.ui.text.util.CColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.googlecode.goclipse.ui.editor.GoSimpleSourceViewerConfiguration;
import com.googlecode.goclipse.ui.text.GoPartitionScanner;
import com.googlecode.goclipse.ui.text.GoPartitions;


public class TextSettings_Actual {
	
	public static final String PARTITIONING_ID = GoPartitions.PARTITIONING_ID;
	
	public static final String[] PARTITION_TYPES = GoPartitions.PARTITION_TYPES;
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
	public static GoSimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, CColorManager colorManager) {
		return new GoSimpleSourceViewerConfiguration(preferenceStore, colorManager, null);
	}
	
}