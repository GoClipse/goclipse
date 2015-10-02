package com.googlecode.goclipse.ui.editor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.core.text.GoPartitionScanner;
import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.GoScanner;

import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.util.swt.jface.text.ColorManager2;

/**
 * @author steel
 */
public class GoEditorSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	protected final GoEditor	        editor;

	public GoEditorSourceViewerConfiguration(IPreferenceStore preferenceStore, ColorManager2 colorManager, 
			GoEditor editor) {
		super(preferenceStore, colorManager, editor);
		this.editor = editor;
	}
	
	@Override
	protected void createScanners(Display currentDisplay) {
		addScanner(new GoScanner(getTokenStore()), IDocument.DEFAULT_CONTENT_TYPE);
	
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__COMMENT), 
			GoPartitionScanner.LINE_COMMENT);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__COMMENT), 
			GoPartitionScanner.BLOCK_COMMENT);
		
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__CHARACTER), 
			GoPartitionScanner.CHARACTER);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__STRING), 
			GoPartitionScanner.STRING);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__MULTILINE_STRING), 
			GoPartitionScanner.MULTILINE_STRING);
	}
	
	@Override
	protected String getToggleCommentPrefix() {
		return "//";
	}
	
	@Override
	protected IInformationProvider getInformationProvider(String contentType) {
		return null;
	}
	
	@Override
	protected ContentAssistCategoriesBuilder getContentAssistCategoriesProvider() {
		return new ContentAssistCategoriesBuilder() {
			
			@Override
			protected ILangCompletionProposalComputer createDefaultSymbolsProposalComputer() {
				return new GocodeCompletionProposalComputer();
			}
			
		};
	}
	
}