package LANG_PROJECT_ID.ide.ui.editor;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.swt.widgets.Display;

import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_CodeScanner;
import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_ColorPreferences;
import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.text.AbstractLangScanner;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.coloring.SingleTokenScanner;
import melnorme.lang.ide.ui.text.coloring.TokenRegistry;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.util.swt.jface.text.ColorManager2;

public class LANGUAGE_SourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LANGUAGE_SourceViewerConfiguration(IPreferenceStore preferenceStore, ColorManager2 colorManager,
			AbstractLangStructureEditor editor) {
		super(preferenceStore, colorManager, editor);
	}
	
	@Override
	protected AbstractLangScanner createScannerFor(Display current, LangPartitionTypes partitionType, 
			TokenRegistry tokenStore) {
		switch (partitionType) {
		case CODE: 
			return new LANGUAGE_CodeScanner(tokenStore);
		
		case LINE_COMMENT: 
		case BLOCK_COMMENT: 
			return new SingleTokenScanner(tokenStore, LANGUAGE_ColorPreferences.COMMENTS);
		
		case DOC_LINE_COMMENT:
		case DOC_BLOCK_COMMENT:
			return new SingleTokenScanner(tokenStore, LANGUAGE_ColorPreferences.DOC_COMMENTS);
		
		case STRING:
			return new SingleTokenScanner(tokenStore, LANGUAGE_ColorPreferences.STRINGS);
		
		case CHARACTER:
			return new SingleTokenScanner(tokenStore, LANGUAGE_ColorPreferences.CHARACTER);
		}
		throw assertUnreachable();
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
				return new LANGUAGE_CompletionProposalComputer();
			}
		};
	}
	
}