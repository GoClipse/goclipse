package com.googlecode.goclipse.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.swt.widgets.Display;

import com.googlecode.goclipse.ui.GoUIPreferenceConstants;
import com.googlecode.goclipse.ui.text.GoScanner;

import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.text.AbstractLangScanner;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;
import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
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
	protected AbstractLangScanner createScannerFor(Display current, LangPartitionTypes partitionType,
			TokenRegistry tokenStore) {
		switch (partitionType) {
		case CODE:
			return new GoScanner(tokenStore);
					
		case LINE_COMMENT:
		case BLOCK_COMMENT:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.SC__COMMENT);
			
		case CHARACTER:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.SC__CHARACTER);
		case STRING:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.SC__STRING);
		case MULTILINE_STRING:
			return new SingleTokenScanner(tokenStore, GoUIPreferenceConstants.SC__MULTILINE_STRING);
		}
		
		throw assertFail();
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