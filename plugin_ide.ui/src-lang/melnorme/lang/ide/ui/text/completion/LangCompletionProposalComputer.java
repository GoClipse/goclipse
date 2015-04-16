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


import java.util.List;

import melnorme.lang.ide.core.operations.TimeoutProgressMonitor;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.ide.ui.tools.ToolProcessRunner;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.CompletionProposal;
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
	
	protected void handleExceptionInUI(CommonException ce) {
		UIOperationExceptionHandler.handleOperationStatus(LangUIMessages.ContentAssistProcessor_opName, ce);
	}
	
	@Override
	public List<ICompletionProposal> computeCompletionProposals(SourceOperationContext context) {
		errorMessage = null;
		
		try {
			try {
				return doComputeCompletionProposals(context, context.getInvocationOffset());
			} catch (CoreException ce) {
				throw new CommonException(ce.getMessage(), ce.getCause());
			}
		} catch (CommonException ce) {
			handleExceptionInUI(ce);
		} catch (OperationSoftFailure e) {
			errorMessage = e.getMessage();
		}
		return null;
	}
	
	@Override
	public List<IContextInformation> computeContextInformation(SourceOperationContext context) {
		return null;
	}
	
	protected List<ICompletionProposal> doComputeCompletionProposals(SourceOperationContext context,
			int offset) throws CoreException, CommonException, OperationSoftFailure {
		
		final TimeoutProgressMonitor pm = new TimeoutProgressMonitor(5000);
		try {
			
			return computeProposals(context, offset, pm);
			
		} catch (OperationCancellation e) {
			if(pm.isCanceled()) {
				throw new CommonException(LangUIMessages.ContentAssist_Timeout);
			}
			// This shouldn't be possible in most concrete implementations,
			// as OperationCancellation should only occur when the timeout is reached.
			throw new OperationSoftFailure(LangUIMessages.ContentAssist_Cancelled); 
		}
		
	}
	
	protected List<ICompletionProposal> computeProposals(SourceOperationContext context, int offset,
			TimeoutProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation, OperationSoftFailure
	{
		LangCompletionResult result = doComputeProposals(context, offset, pm);
		Indexable<ToolCompletionProposal> resultProposals = result.getValidatedProposals();
		
		ArrayList2<ICompletionProposal> proposals = new ArrayList2<>();
		for (ToolCompletionProposal proposal : resultProposals) {
			proposals.add(adaptToolProposal(proposal));
		}
		
		return proposals;
	}
	
	protected ICompletionProposal adaptToolProposal(ToolCompletionProposal proposal) {
		return new CompletionProposal(
			proposal.getReplaceString(),
			proposal.getReplaceStart(),
			proposal.getReplaceLength(),
			proposal.getReplaceString().length()
		);
	}
	
	protected abstract LangCompletionResult doComputeProposals(SourceOperationContext context,
			int offset, TimeoutProgressMonitor pm) throws CoreException, CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	protected ToolProcessRunner getProcessRunner(IProgressMonitor pm) {
		return new ToolProcessRunner(pm);
	}
	
}