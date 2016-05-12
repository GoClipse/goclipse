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


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.ToolManager.ToolManagerEngineToolRunner;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.LangImageProvider;
import melnorme.lang.ide.ui.LangImages;
import melnorme.lang.ide.ui.LangUIMessages;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.editor.AbstractLangEditor;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.ide.ui.views.AbstractLangImageProvider;
import melnorme.lang.ide.ui.views.StructureElementLabelProvider;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.completion.LangCompletionResult;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.lang.utils.concurrency.TimeoutCancelMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.ownership.Disposable;

public abstract class LangCompletionProposalComputer extends AbstractCompletionProposalComputer {
	
	@Override
	protected Indexable<ICompletionProposal> doComputeCompletionProposals(SourceOperationContext context,
			int offset) throws CoreException, CommonException, OperationSoftFailure {
		
		final TimeoutCancelMonitor cm = new TimeoutCancelMonitor(5000);
		try {
			if(needsEditorSave()) {
				doEditorSave(context, EclipseUtils.pm(cm));
			}
			
			return computeProposals(context, offset, cm);
			
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
	
	protected void doEditorSave(SourceOperationContext context, IProgressMonitor pm) throws CommonException {
		IEditorPart editor = context.getEditor_nonNull();
		if(editor instanceof AbstractLangEditor) {
			AbstractLangEditor langEditor = (AbstractLangEditor) editor;
			
			try(Disposable disposable = langEditor.saveActionsEnablement().enterDisable()) {
				editor.doSave(pm);
			}
		} else {
			editor.doSave(pm);
		}
	}
	
	protected Indexable<ICompletionProposal> computeProposals(SourceOperationContext context, int offset,
			ICancelMonitor cm)
			throws CoreException, CommonException, OperationCancellation, OperationSoftFailure
	{
		
		LangCompletionResult result = doComputeProposals(context, offset, cm);
		Indexable<ToolCompletionProposal> resultProposals = result.getValidatedProposals();
		
		ArrayList2<ICompletionProposal> proposals = new ArrayList2<>();
		for (ToolCompletionProposal proposal : resultProposals) {
			proposals.add(adaptToolProposal(proposal));
		}
		
		return proposals;
	}
	
	protected abstract LangCompletionResult doComputeProposals(SourceOperationContext context,
			int offset, ICancelMonitor cm) 
			throws CoreException, CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	protected ICompletionProposal adaptToolProposal(ToolCompletionProposal proposal) {
		IContextInformation ctxInfo = null; // TODO: context information
		return new LangCompletionProposal(proposal, getImage(proposal), ctxInfo);
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