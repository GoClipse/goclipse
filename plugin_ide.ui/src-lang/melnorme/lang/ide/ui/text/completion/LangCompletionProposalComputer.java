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
package melnorme.lang.ide.ui.text.completion;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager.ToolManagerEngineToolRunner;
import melnorme.lang.ide.ui.LangImageProvider;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.views.AbstractLangImageProvider;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.utils.concurrency.TimeoutCancelMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangCompletionProposalComputer extends AbstractCompletionProposalComputer {
	
	@Override
	protected Indexable<ICompletionProposal> doComputeCompletionProposals(CompletionContext context) 
			throws CommonException, OperationSoftFailure {
		
		if(needsEditorSave()) {
			doEditorSave(context);
		}
		
		final TimeoutCancelMonitor cm = new TimeoutCancelMonitor(5000);
		try {
			
			return computeProposals(context, cm);
			
		} catch (OperationCancellation e) {
			if(cm.isCanceled()) {
				throw new CommonException(LangUIMessages.ContentAssist_Timeout);
			}
			// This shouldn't be possible in most concrete implementations,
			// as OperationCancellation should only occur when the timeout is reached.
			throw new OperationSoftFailure(LangUIMessages.ContentAssist_Cancelled); 
		}
	}
	
	protected boolean needsEditorSave() {
		return false;
	}
	
	protected void doEditorSave(CompletionContext context) throws CommonException {
		context.getSourceBuffer().trySaveBuffer(); 
	}
	
	protected Indexable<ICompletionProposal> computeProposals(CompletionContext context, ICancelMonitor cm)
			throws CommonException, OperationCancellation, OperationSoftFailure
	{
		
		LangCompletionResult result = doComputeProposals(context, cm);
		Indexable<ToolCompletionProposal> resultProposals = result.getValidatedProposals();
		
		ArrayList2<ICompletionProposal> proposals = new ArrayList2<>();
		for (ToolCompletionProposal proposal : resultProposals) {
			proposals.add(adaptToolProposal(context, proposal));
		}
		
		return proposals;
	}
	
	protected abstract LangCompletionResult doComputeProposals(CompletionContext context,
			ICancelMonitor cm) 
			throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	protected ICompletionProposal adaptToolProposal(CompletionContext context, ToolCompletionProposal proposal) {
		IContextInformation ctxInfo = null; // TODO: context information
		return new LangCompletionProposal(context.getSourceBuffer(), proposal, getImage(proposal), ctxInfo);
	}
	
	protected Image getImage(ToolCompletionProposal proposal) {
		ImageDescriptor imageDescriptor = createImageDescriptor(proposal);
		return LangImages.getImageDescriptorRegistry().get(imageDescriptor); 
	}
	
	public ImageDescriptor createImageDescriptor(ToolCompletionProposal proposal) {
		ImageDescriptor baseImage = getBaseImageDescriptor(proposal);
		
		StructureElementLabelProvider labelDecorator = LangUIPlugin_Actual.getStructureElementLabelProvider();
		return labelDecorator.getElementImageDescriptor(baseImage, proposal.getAttributes());
	}
	
	protected ImageDescriptor getBaseImageDescriptor(ToolCompletionProposal proposal) {
		return getImageProvider().getImageDescriptor(proposal.getKind()).getDescriptor();
	}
	
	protected AbstractLangImageProvider getImageProvider() {
		return new LangImageProvider();
	}
	
	/* -----------------  ----------------- */
	
	protected ToolManagerEngineToolRunner getEngineToolRunner() {
		return LangCore.getToolManager().new ToolManagerEngineToolRunner();
	}
	
}