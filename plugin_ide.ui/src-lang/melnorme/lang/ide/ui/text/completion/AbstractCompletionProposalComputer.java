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

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

import melnorme.lang.ide.core.text.ISourceBufferExt;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;


public abstract class AbstractCompletionProposalComputer implements ILangCompletionProposalComputer {
	
	public AbstractCompletionProposalComputer() {
	}
	
	@Override
	public abstract Indexable<ICompletionProposal> computeCompletionProposals(ISourceBufferExt sourceBuffer, 
			ITextViewer viewer, int offset) 
		throws CommonException, OperationSoftFailure;
	
}