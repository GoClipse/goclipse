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


import melnorme.lang.ide.ui.LangUIMessages;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;

public abstract class ContenAssistProcessorExt implements IContentAssistProcessor {
	
	public ContenAssistProcessorExt() {
		super();
	}
	
	/* -----------------  ----------------- */
	
	protected char[] completionAutoActivationCharacters;

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return completionAutoActivationCharacters;
	}
	
	public void setCompletionProposalAutoActivationCharacters(char[] activationSet) {
		completionAutoActivationCharacters= activationSet;
	}
	
	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	/* -----------------  ----------------- */
	
	protected String errorMessage;
	
	protected void resetComputeState() {
		errorMessage = null;
	}
	
	@Override
	public String getErrorMessage() {
		if(errorMessage != null)
			return errorMessage;
		return LangUIMessages.ContentAssistProcessor_emptyDefaultProposals;
	}
	
	@Override
	public final ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		resetComputeState();
		
		return doComputeCompletionProposals(viewer, offset);
	}
	
	protected abstract ICompletionProposal[] doComputeCompletionProposals(ITextViewer viewer, int offset);
	
	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		resetComputeState();
		
		return doComputeContextInformation(viewer, offset);
	}
	
	protected abstract IContextInformation[] doComputeContextInformation(ITextViewer viewer, int offset);
	
}