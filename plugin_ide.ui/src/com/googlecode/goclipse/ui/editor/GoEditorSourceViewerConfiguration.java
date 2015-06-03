package com.googlecode.goclipse.ui.editor;

import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.texteditor.ITextEditor;

import _org.eclipse.cdt.ui.text.IColorManager;
import _org.eclipse.jdt.internal.ui.text.CompositeReconcilingStrategy;

import com.googlecode.goclipse.core.text.GoPartitionScanner;
import com.googlecode.goclipse.editors.DoubleClickStrategy;
import com.googlecode.goclipse.editors.GoEditor;
import com.googlecode.goclipse.editors.GoEditorReconcilingStrategy;
import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.GoScanner;

/**
 * @author steel
 */
public class GoEditorSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	protected final GoEditor	        editor;
	private DoubleClickStrategy	doubleClickStrategy;

	public GoEditorSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager, 
			GoEditor editor) {
		super(preferenceStore, colorManager, editor);
		this.editor = editor;
	}
	
	@Override
	protected void createScanners() {
		addScanner(new GoScanner(getTokenStoreFactory()), IDocument.DEFAULT_CONTENT_TYPE);
	
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__COMMENT.key), 
			GoPartitionScanner.LINE_COMMENT);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__COMMENT.key), 
			GoPartitionScanner.BLOCK_COMMENT);
		
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__CHARACTER.key), 
			GoPartitionScanner.CHARACTER);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__STRING.key), 
			GoPartitionScanner.STRING);
		addScanner(createSingleTokenScanner(GoUIPreferenceConstants.SYNTAX_COLORING__MULTILINE_STRING.key), 
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
	protected void configureContentAssistantProcessors(ContentAssistant assistant) {
		super.configureContentAssistantProcessors(assistant);
		
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(100);

		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		assistant.setContextInformationPopupBackground(colorManager.getColor(new RGB(150, 150, 0)));
		assistant.setInformationControlCreator(getInformationControlCreator(editor.getSourceViewer_()));
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
	
	/* -----------------  Need to review remaining code ----------------- */
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DoubleClickStrategy();
		return doubleClickStrategy;
	}
	
	@Override
	protected CompositeReconcilingStrategy getReconciler_createCompositeStrategy(ITextEditor editor) {
		return new CompositeReconcilingStrategy(new GoEditorReconcilingStrategy(this.editor));
	}
	
}