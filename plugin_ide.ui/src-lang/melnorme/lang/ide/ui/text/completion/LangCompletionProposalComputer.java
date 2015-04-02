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
package melnorme.lang.ide.ui.text.completion;


import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.misc.Location;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.ui.IEditorPart;

public abstract class LangCompletionProposalComputer implements ILangCompletionProposalComputer {
	
	protected String errorMessage;
	
	public LangCompletionProposalComputer() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return errorMessage;
	}
	
	@Override
	public void sessionStarted() {
	}
	
	@Override
	public void sessionEnded() {
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public List<ICompletionProposal> computeCompletionProposals(LangContentAssistInvocationContext context) {
		errorMessage = null;
		
		try {
			IEditorPart editor = context.getEditor_nonNull();
			
			IDocument document = context.getViewer().getDocument();
			
			Location fileLocation = EditorUtils.getLocationFromEditorInput(editor.getEditorInput());
			if(fileLocation == null) {
				throw LangCore.createCoreException("Error, invalid location for editor input.", null);
			}
			
			return doComputeCompletionProposals(context.getInvocationOffset(), fileLocation.path, document);
		} catch (CoreException ce) {
			handleExceptionInUI(ce);
			errorMessage = ce.getMessage();
			return Collections.EMPTY_LIST;
		}
	}
	
	protected abstract List<ICompletionProposal> doComputeCompletionProposals(int offset, Path filePath, 
			IDocument document) throws CoreException;	
	
	@Override
	public List<IContextInformation> computeContextInformation(LangContentAssistInvocationContext context) {
		return Collections.emptyList();
	}
	
	protected void handleExceptionInUI(CoreException ce) {
		UIOperationExceptionHandler.handleOperationStatus(LangUIMessages.ContentAssistProcessor_opName, ce);
	}
	
}