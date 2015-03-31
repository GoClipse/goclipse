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
package org.eclipse.jdt.internal.ui.text.java;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

public abstract class CompletionProposalComputerDescriptor {
	
	/**
	 * Tells whether this proposal engine provides dynamic content that needs to be sorted after its
	 * proposal have been filtered. Filtering happens, e.g., when a user continues typing with an
	 * open completion window.
	 * 
	 * @since 3.8
	 */
	private boolean fNeedsSortingAfterFiltering;
	
	protected final IJavaCompletionProposalComputer proposalComputer;
	
	public CompletionProposalComputerDescriptor(IJavaCompletionProposalComputer proposalComputer) {
		this.proposalComputer = assertNotNull(proposalComputer);
		
	}
	
	public boolean isSortingAfterFilteringNeeded() {
		return fNeedsSortingAfterFiltering;
	}
	public IJavaCompletionProposalComputer getComputer() {
		return proposalComputer;
	}
	
	
	public abstract List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context, 
		IProgressMonitor monitor);
	
	public abstract List<IContextInformation> computeContextInformation(ContentAssistInvocationContext context, 
		IProgressMonitor monitor);
	
	public String getErrorMessage() {
		return getComputer().getErrorMessage();
	}
	
	public void sessionStarted() {
		IJavaCompletionProposalComputer computer= getComputer();
		computer.sessionStarted();
	}
	
	public void sessionEnded() {
		IJavaCompletionProposalComputer computer= getComputer();
		computer.sessionEnded();
	}
	
}