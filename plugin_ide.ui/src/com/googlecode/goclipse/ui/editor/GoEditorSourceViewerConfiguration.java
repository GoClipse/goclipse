package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.information.IInformationProvider;

import _org.eclipse.cdt.ui.text.IColorManager;

import com.googlecode.goclipse.core.text.GoPartitionScanner;
import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.GoScanner;

/**
 * @author steel
 */
public class GoEditorSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	protected final GoEditor	        editor;

	public GoEditorSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager, 
			GoEditor editor) {
		super(preferenceStore, colorManager, editor);
		this.editor = editor;
	}
	
	@Override
	protected void createScanners() {
		addScanner(new GoScanner(getTokenStoreFactory()), IDocument.DEFAULT_CONTENT_TYPE);
	
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__COMMENT.key), 
			GoPartitionScanner.LINE_COMMENT);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__COMMENT.key), 
			GoPartitionScanner.BLOCK_COMMENT);
		
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__CHARACTER.key), 
			GoPartitionScanner.CHARACTER);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__STRING.key), 
			GoPartitionScanner.STRING);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SC__MULTILINE_STRING.key), 
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