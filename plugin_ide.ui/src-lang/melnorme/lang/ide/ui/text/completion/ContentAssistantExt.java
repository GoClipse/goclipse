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

import melnorme.lang.ide.ui.templates.LangTemplateProposal;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalSorter;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ContentAssistantExt extends ContentAssistant {
	
	protected final ContentAssistPreferenceHandler caPrefHelper;
	
	public ContentAssistantExt(ContentAssistPreferenceHandler caPrefHelper) {
		this.caPrefHelper = caPrefHelper;
		
		setSorter(new ContentAssistSorter());
	}
	
	public void configure(IPreferenceStore prefStore) {
		 caPrefHelper.configure(this, prefStore);
	}
	
	public void handlePrefChange(PropertyChangeEvent event, IPreferenceStore prefStore) {
		caPrefHelper.handlePrefChange(this, prefStore, event);
	}
	
	/* -----------------  ----------------- */
	
	public static class ContentAssistSorter implements ICompletionProposalSorter {
		@Override
		public int compare(ICompletionProposal proposalA, ICompletionProposal proposalB) {
			int relevanceA = getRelevance(proposalA);
			int relevanceB = getRelevance(proposalB);
			
			if(relevanceA != relevanceB) {
				return Integer.compare(relevanceA, relevanceB);
			}
			
			String p1SortString = getSortString(proposalA);
			String p2SortString = getSortString(proposalB);
			return p1SortString.compareTo(p2SortString);
		}

		protected int getRelevance(ICompletionProposal proposal) {
			if(proposal instanceof LangCompletionProposal) {
				LangCompletionProposal langProposal = (LangCompletionProposal) proposal;
				return langProposal.getRelevance();
			}
			
			return proposal instanceof LangTemplateProposal ? 100 : 0;
		}
		
		protected String getSortString(ICompletionProposal proposal) {
			String sortString;
			if(proposal instanceof LangCompletionProposal) {
				LangCompletionProposal langProposal = (LangCompletionProposal) proposal;
				sortString = langProposal.getSortString();
			} else {
				sortString = proposal.getDisplayString();
			}
			return sortString == null ? "\uFFFF" : sortString;
		}
		
	}
	
}