/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor;


import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;

public abstract class LangContentAssistProcessor implements IContentAssistProcessor {
	
	protected final AbstractDecoratedTextEditor editor;
	
	protected String errorMessage;
	
	public LangContentAssistProcessor(AbstractDecoratedTextEditor editor) {
		this.editor = editor;
	}
	
	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		errorMessage = null;
		try {
			if(editor == null) {
				throw LangCore.createCoreException("Error, no editor available for operation.", null);
			}
			
			String filePath = EditorUtils.getFilePathFromEditorInput(editor.getEditorInput()).toString();
			if (filePath == null) {
				throw LangCore.createCoreException("Error: Could not determine file path for editor.", null);
			}
			return doComputeCompletionProposals(offset, filePath, viewer.getDocument());
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handleOperationStatus("Content Assist", ce);
			errorMessage = ce.getMessage();
			return array();
		}
	}
	
	protected abstract ICompletionProposal[] doComputeCompletionProposals(int offset, String filePath, 
			IDocument document) throws CoreException;
	
	public static interface ILangCompletionProposalComputer {
		
		ICompletionProposal[] computeCompletionProposals() throws CoreException;
		
	}
	
}