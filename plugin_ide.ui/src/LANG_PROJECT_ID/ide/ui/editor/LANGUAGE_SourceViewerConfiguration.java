/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package LANG_PROJECT_ID.ide.ui.editor;

import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;

import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_CodeScanner;
import LANG_PROJECT_ID.ide.ui.text.LANGUAGE_ColorPreferences;
import melnorme.lang.ide.core.TextSettings_Actual.LangPartitionTypes;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.structure.AbstractLangStructureEditor;
import melnorme.lang.ide.ui.text.AbstractLangSourceViewerConfiguration;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.util.swt.jface.text.ColorManager2;

public class LANGUAGE_SourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LANGUAGE_SourceViewerConfiguration(IPreferenceStore preferenceStore, ColorManager2 colorManager,
			AbstractLangStructureEditor editor) {
		super(preferenceStore, colorManager, editor);
	}
	
	@Override
	protected void createScanners(Display currentDisplay) {
		addScanner(new LANGUAGE_CodeScanner(getTokenStore()), 
			IDocument.DEFAULT_CONTENT_TYPE);
		
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.COMMENTS), 
			LangPartitionTypes.LINE_COMMENT.getId());
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.COMMENTS), 
			LangPartitionTypes.BLOCK_COMMENT.getId());
		
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.DOC_COMMENTS), 
			LangPartitionTypes.DOC_LINE_COMMENT.getId());
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.DOC_COMMENTS), 
			LangPartitionTypes.DOC_BLOCK_COMMENT.getId());
		
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.STRINGS), 
			LangPartitionTypes.STRING.getId());
		
		addScanner(createSingleTokenScanner(LANGUAGE_ColorPreferences.CHARACTER), 
			LangPartitionTypes.CHARACTER.getId());
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
	public IAutoEditStrategy[] getAutoEditStrategies(ISourceViewer sourceViewer, String contentType) {
		if(IDocument.DEFAULT_CONTENT_TYPE.equals(contentType)) {
			return array(LangUIPlugin_Actual.createAutoEditStrategy(sourceViewer, contentType));
		} else {
			return super.getAutoEditStrategies(sourceViewer, contentType);
		}
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