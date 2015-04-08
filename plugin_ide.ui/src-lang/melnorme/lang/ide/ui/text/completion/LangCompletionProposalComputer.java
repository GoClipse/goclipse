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


import java.util.Collections;
import java.util.List;

import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.ops.OperationSoftFailure;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

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
			return doComputeCompletionProposals(context, context.getInvocationOffset());
		} catch (CoreException ce) {
			handleExceptionInUI(ce);
		} catch (OperationSoftFailure e) {
			errorMessage = e.getMessage();
		}
		return Collections.EMPTY_LIST;
	}
	
	protected abstract List<ICompletionProposal> doComputeCompletionProposals(
		LangContentAssistInvocationContext context, int offset) throws CoreException, OperationSoftFailure;	
	
	@Override
	public List<IContextInformation> computeContextInformation(LangContentAssistInvocationContext context) {
		return Collections.emptyList();
	}
	
	protected void handleExceptionInUI(CoreException ce) {
		UIOperationExceptionHandler.handleOperationStatus(LangUIMessages.ContentAssistProcessor_opName, ce);
	}
	
}