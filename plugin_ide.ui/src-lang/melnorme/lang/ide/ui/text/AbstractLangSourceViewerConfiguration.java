/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text;

import static melnorme.utilbox.core.CoreUtil.array;
import static melnorme.utilbox.core.CoreUtil.tryCast;

import java.util.Map;

import melnorme.lang.ide.ui.EditorSettings_Actual;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.text.completion.CompletionProposalsGrouping;
import melnorme.lang.ide.ui.text.completion.ContentAssistPreferenceHandler;
import melnorme.lang.ide.ui.text.completion.ContentAssistantExt;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.utilbox.collections.Indexable;

import org.eclipse.cdt.ui.text.IColorManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class AbstractLangSourceViewerConfiguration extends SimpleLangSourceViewerConfiguration {
	
	protected final AbstractDecoratedTextEditor editor;
	
	public AbstractLangSourceViewerConfiguration(IPreferenceStore preferenceStore, IColorManager colorManager,
			AbstractDecoratedTextEditor editor) {
		super(preferenceStore, colorManager);
		this.editor = editor;
	}
	
	public AbstractDecoratedTextEditor getEditor() {
		return editor;
	}
	
	public AbstractLangEditor getEditor_asLang() {
		return tryCast(editor, AbstractLangEditor.class);
	}

	
	/* ----------------- Navigation operations ----------------- */
	
	@Override 
	protected Map<String, ITextEditor> getHyperlinkDetectorTargets(ISourceViewer sourceViewer) {
		Map<String, ITextEditor> targets = super.getHyperlinkDetectorTargets(sourceViewer);
		targets.put(EditorSettings_Actual.EDITOR_CODE_TARGET, editor); 
		return targets;
	}
	
	/* ----------------- Modification operations ----------------- */
	
	@Override
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		if(IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
			return array(LangUIPlugin_Actual.createAutoEditStrategy(sourceViewer, contentType));
		} else {
			return super.getAutoEditStrategies(sourceViewer, contentType);
		}
	}
	
	@Override
	public String[] getDefaultPrefixes(ISourceViewer sourceViewer, String contentType) {
		return new String[] { getToggleCommentPrefix(), "" };
	}
	
	protected abstract String getToggleCommentPrefix();
	
	/* ----------------- Content Assist ----------------- */
	
	@Override
	public ContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		AbstractLangEditor editor = getEditor_asLang();
		if(editor != null) {
			final ContentAssistPreferenceHandler caPrefHelper = createContentAssistPrefHandler();
			
			ContentAssistantExt assistant = new ContentAssistantExt(caPrefHelper);
			assistant.setDocumentPartitioning(getConfiguredDocumentPartitioning(sourceViewer));
			
			assistant.setRestoreCompletionProposalSize(LangUIPlugin.getDialogSettings("completion_proposal_size"));
			assistant.setInformationControlCreator(
				getInformationControl_ContentAsssist(caPrefHelper.getAdditionalInfoAffordanceString()));
			assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
			assistant.enableColoredLabels(true);
			
			assistant.configure(fPreferenceStore);
			configureContentAssistantProcessors(assistant);
			
			return assistant;
		}
		
		return null;
	}
	
	protected ContentAssistPreferenceHandler createContentAssistPrefHandler() {
		return new ContentAssistPreferenceHandler();
	}
	
	protected void configureContentAssistantProcessors(ContentAssistant assistant) {
		Indexable<CompletionProposalsGrouping> categories = getContentAssistCategoriesProvider().getCategories();
		IContentAssistProcessor cap = new LangContentAssistProcessor(assistant, getEditor(), categories);
		assistant.setContentAssistProcessor(cap, IDocument.DEFAULT_CONTENT_TYPE);
	}
	
	protected abstract ContentAssistCategoriesBuilder getContentAssistCategoriesProvider();
	
}