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
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;
import melnorme.lang.tooling.ops.OperationSoftFailure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;


public abstract class AbstractCompletionProposalComputer implements ILangCompletionProposalComputer {
	
	protected String errorMessage;
	
	public AbstractCompletionProposalComputer() {
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
	public Indexable<IContextInformation> computeContextInformation(SourceOperationContext context) {
		return null;
	}
	
	@Override
	public Indexable<ICompletionProposal> computeCompletionProposals(SourceOperationContext context) 
		throws CommonException 
	{
		errorMessage = null;
		
		try {
			return doComputeCompletionProposals(context, context.getInvocationOffset());
		} catch (CoreException ce) {
			throw LangCore.createCommonException(ce);
		} catch (OperationSoftFailure e) {
			errorMessage = e.getMessage();
		}
		return null;
	}
	
	protected abstract Indexable<ICompletionProposal> doComputeCompletionProposals(SourceOperationContext context,
			int offset) throws CoreException, CommonException, OperationSoftFailure;
	
}