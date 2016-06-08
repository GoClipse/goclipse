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

import melnorme.lang.ide.ui.editor.actions.EditorOperationContext;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;

public class CompletionProposalsGrouping implements ILangCompletionProposalComputer {
	
	protected final String id;
	protected final String name;
	protected final ImageDescriptor image;
	protected final Indexable<ILangCompletionProposalComputer> computers;
	
	public CompletionProposalsGrouping(String id, String name, ImageDescriptor image,
			ArrayList2<ILangCompletionProposalComputer> computers) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.computers = computers;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Indexable<ILangCompletionProposalComputer> getComputers() {
		return computers;
	}
	
	/* -----------------  ----------------- */
	
	protected String lastErrorMessage = null;
	
	@Override
	public String getErrorMessage() {
		return lastErrorMessage;
	}

	protected void clearErrorMessage() {
		lastErrorMessage = null;
	}
	
	protected void updateErrorMessage(String errorMessage) {
		 if(lastErrorMessage == null) {
			 lastErrorMessage = errorMessage;
		 }
	}
	
	@Override
	public void sessionStarted() {
		clearErrorMessage();
		for (ILangCompletionProposalComputer computer : computers) {
			computer.sessionStarted();
			
			updateErrorMessage(computer.getErrorMessage());
		}
	}
	
	@Override
	public void sessionEnded() {
		clearErrorMessage();
		for (ILangCompletionProposalComputer computer : computers) {
			computer.sessionEnded();
			
			updateErrorMessage(computer.getErrorMessage());
		}
	}
	
	@Override
	public Indexable<ICompletionProposal> computeCompletionProposals(EditorOperationContext context) 
			throws CommonException {
		clearErrorMessage();
		
		ArrayList2<ICompletionProposal> proposals = new ArrayList2<>();
		
		for (ILangCompletionProposalComputer computer : computers) {
			Indexable<ICompletionProposal> computerProposals = computer.computeCompletionProposals(context);
			if(computerProposals != null) {
				proposals.addAll2(computerProposals);
			}
			
			updateErrorMessage(computer.getErrorMessage());
		}
		return proposals;
	}
	
	@Override
	public Indexable<IContextInformation> computeContextInformation(EditorOperationContext context) {
		clearErrorMessage();
		
		ArrayList2<IContextInformation> proposals = new ArrayList2<>();
		
		for (ILangCompletionProposalComputer computer : computers) {
			Indexable<IContextInformation> computerProposals = computer.computeContextInformation(context);
			if(computerProposals != null) {
				proposals.addAll2(computerProposals);
			}
			
			updateErrorMessage(computer.getErrorMessage());
		}
		return proposals;
	}
	
}