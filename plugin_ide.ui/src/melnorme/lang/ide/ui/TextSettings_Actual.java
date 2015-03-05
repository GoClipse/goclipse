package melnorme.lang.ide.ui;

import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.text.LangDocumentPartitionerSetup;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.ui.editor.GoEditorSourceViewerConfiguration;
import com.googlecode.goclipse.ui.editor.GoSimpleSourceViewerConfiguration;
import com.googlecode.goclipse.ui.text.GoDocumentSetupParticipant;
import com.googlecode.goclipse.ui.text.GoPartitionScanner;
import com.googlecode.goclipse.ui.text.GoPartitions;


public class TextSettings_Actual {
	
	public static final String PARTITIONING_ID = GoPartitions.PARTITIONING_ID;
	
	public static final String[] PARTITION_TYPES = GoPartitions.PARTITION_TYPES;
	
	public static IPartitionTokenScanner createPartitionScanner() {
		return new GoPartitionScanner();
	}
	
	public static LangDocumentPartitionerSetup createDocumentSetupHelper() {
		return new GoDocumentSetupParticipant();
	}
	
	public static GoEditorSourceViewerConfiguration createSourceViewerConfiguration(
			IPreferenceStore preferenceStore, AbstractLangEditor editor) {
		IColorManager colorManager = LangUIPlugin.getInstance().getColorManager();
		return new GoEditorSourceViewerConfiguration(preferenceStore, colorManager, (GoEditor) editor);
	}
	
	public static GoSimpleSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IPreferenceStore preferenceStore, IColorManager colorManager) {
		return new GoSimpleSourceViewerConfiguration(preferenceStore, colorManager, null);
	}
	
}