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

import melnorme.lang.ide.ui.editor.actions.SourceOperationContext;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

public interface ILangCompletionProposalComputer {
	
	void sessionStarted();
	
	List<ICompletionProposal> computeCompletionProposals(SourceOperationContext context);
	
	List<IContextInformation> computeContextInformation(SourceOperationContext context);
	
	void sessionEnded();
	
	/**
	 * Returns the reason why this computer was unable to produce any completion proposals or
	 * context information.
	 *
	 * @return an error message or <code>null</code> if no error occurred
	 */
	String getErrorMessage();
	
}