/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.text;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.ui.editor.GocodeCompletionProposalComputer;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.ui.text.completion.ILangCompletionProposalComputer;
import melnorme.lang.ide.ui.text.completion.LangContentAssistProcessor.ContentAssistCategoriesBuilder;
import melnorme.lang.tooling.LANG_SPECIFIC;

@LANG_SPECIFIC
public class LangSourceViewerConfiguration extends AbstractLangSourceViewerConfiguration {
	
	public LangSourceViewerConfiguration(IPreferenceStore preferenceStore, ISourceBufferExt sourceBuffer, 
			ITextEditor editor) {
		super(preferenceStore, sourceBuffer, editor);
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
				return new GocodeCompletionProposalComputer();
			}
			
		};
	}
	
}