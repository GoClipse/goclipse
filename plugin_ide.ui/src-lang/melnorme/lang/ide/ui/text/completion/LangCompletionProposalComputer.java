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
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager.ToolManagerEngineToolRunner;
import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.ide.ui.LangImageProvider;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.EditorUtils;
import melnorme.lang.ide.ui.views.AbstractLangImageProvider;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.lang.utils.concurrency.TimeoutCancelMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangCompletionProposalComputer extends AbstractCompletionProposalComputer {
	
	protected boolean needsEditorSave() {
		return false;
	}
	
	@Override
	public Indexable<ICompletionProposal> computeCompletionProposals(ISourceBufferExt sourceBuffer, 
			ITextViewer viewer, int offset) 
			throws CommonException, OperationCancellation, OperationSoftFailure {
		
		if(needsEditorSave()) {
			sourceBuffer.trySaveBufferIfDirty(); 
		}
		
		SourceOpContext sourceOpContext = sourceBuffer.getSourceOpContext(offset, EditorUtils.getSelectedRange(viewer));
		
		final TimeoutCancelMonitor cm = new TimeoutCancelMonitor(5000);
		try {
			
			return computeProposals(sourceOpContext, cm);
			
		} catch (OperationCancellation e) {
			if(cm.isCancelled()) {
				throw new OperationSoftFailure(LangUIMessages.ContentAssist_Timeout);
			}
			// This shouldn't be possible in most concrete implementations,
			// as OperationCancellation should only occur when the timeout is reached.
			throw new OperationSoftFailure(LangUIMessages.ContentAssist_Cancelled); 
		}
	}
	
	protected Indexable<ICompletionProposal> computeProposals(SourceOpContext sourceContext, ICancelMonitor cm)
			throws CommonException, OperationCancellation, OperationSoftFailure
	{
		
		Indexable<ToolCompletionProposal> resultProposals = doComputeProposals(sourceContext, cm);
		
		ArrayList2<ICompletionProposal> proposals = new ArrayList2<>();
		for (ToolCompletionProposal proposal : resultProposals) {
			proposals.add(adaptToolProposal(sourceContext, proposal));
		}
		
		return proposals;
	}
	
	protected abstract Indexable<ToolCompletionProposal> doComputeProposals(SourceOpContext sourceContext, ICancelMonitor cm) 
			throws CommonException, OperationCancellation, OperationSoftFailure;
	
	/* -----------------  ----------------- */
	
	protected ICompletionProposal adaptToolProposal(SourceOpContext sourceContext, ToolCompletionProposal proposal) {
		IContextInformation ctxInfo = null; // TODO: context information
		return new LangCompletionProposal(sourceContext, proposal, getImage(proposal), ctxInfo);
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