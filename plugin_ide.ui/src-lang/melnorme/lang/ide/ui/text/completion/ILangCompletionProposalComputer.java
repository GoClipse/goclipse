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

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

import melnorme.lang.ide.ui.editor.actions.EditorOperationContext;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public interface ILangCompletionProposalComputer {
	
	void sessionStarted();
	
	Indexable<ICompletionProposal> computeCompletionProposals(EditorOperationContext context) throws CommonException;
	
	Indexable<IContextInformation> computeContextInformation(EditorOperationContext context);
	
	void sessionEnded();
	
	/**
	 * Returns the reason why this computer was unable to produce any completion proposals or
	 * context information.
	 *
	 * @return an error message or <code>null</code> if no error occurred
	 */
	String getErrorMessage();
	
}