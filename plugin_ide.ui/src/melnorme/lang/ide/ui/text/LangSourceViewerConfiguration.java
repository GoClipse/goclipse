package melnorme.lang.ide.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;

import LANG_PROJECT_ID.ide.ui.editor.LANGUAGE_CompletionProposalComputer;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public class LangSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LangSourceViewerConfiguration(IPreferenceStore preferenceStore, 
			AbstractLangStructureEditor editor) {
		super(preferenceStore, editor);
	}
	
	@Override
	protected String getToggleCommentPrefix() {
		return "//";
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