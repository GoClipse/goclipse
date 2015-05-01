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
import melnorme.lang.ide.ui.tools.ToolManagerOperationHelper;
import melnorme.lang.ide.ui.views.AbstractLangImageProvider;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.util.swt.jface.IManagedImage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

public abstract class LangCompletionProposalComputer extends AbstractCompletionProposalComputer {
	
	@Override
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
		IContextInformation ctxInfo = null; // TODO: context information
		return new LangCompletionProposal(proposal, null, getImage(proposal), ctxInfo);
	}
	
	protected Image getImage(ToolCompletionProposal proposal) {
		IManagedImage baseImageDescriptor = getBaseImageDescriptor(proposal);
		return baseImageDescriptor.getImage();
	}
	
	protected IManagedImage getBaseImageDescriptor(ToolCompletionProposal proposal) {
		return getImageProvider().getImageDescriptor(proposal.getKind());
	}
	
	protected abstract AbstractLangImageProvider getImageProvider();
	
	protected abstract LangCompletionResult doComputeProposals(SourceOperationContext context,
			int offset, TimeoutProgressMonitor pm) throws CoreException, CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	protected ToolManagerOperationHelper getProcessRunner(IProgressMonitor pm) {
		return new ToolManagerOperationHelper(pm);
	}
	
}