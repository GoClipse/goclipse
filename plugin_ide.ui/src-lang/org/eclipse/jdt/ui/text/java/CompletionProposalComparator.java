/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.ui.text.java;

import java.util.Comparator;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.templates.TemplateProposal;

/**
 * Comparator for java completion proposals. Completion proposals can be sorted by relevance or
 * alphabetically.
 * <p>
 * Note: this comparator imposes orderings that are inconsistent with equals.
 * </p>
 *
 * @since 3.1
 */
public class CompletionProposalComparator implements Comparator<ICompletionProposal> {

	protected final boolean fOrderAlphabetically;

	public CompletionProposalComparator() {
		fOrderAlphabetically= false;
	}
	
	public CompletionProposalComparator(boolean fOrderAlphabetically) {
		this.fOrderAlphabetically= fOrderAlphabetically;
	}

	@Override
	public int compare(ICompletionProposal p1, ICompletionProposal p2) {
		if (!fOrderAlphabetically) {
			int r1= getRelevance(p1);
			int r2= getRelevance(p2);
			int relevanceDif= r2 - r1;
			if (relevanceDif != 0) {
				return relevanceDif;
			}
		}
		
		return getSortKey(p1).compareToIgnoreCase(getSortKey(p2));
	}

	protected String getSortKey(ICompletionProposal p) {
//		if (p instanceof AbstractJavaCompletionProposal)
//			return ((AbstractJavaCompletionProposal) p).getSortString();
		return p.getDisplayString();
	}

	protected int getRelevance(ICompletionProposal obj) {
		if (obj instanceof IJavaCompletionProposal) {
			IJavaCompletionProposal jcp= (IJavaCompletionProposal) obj;
			return jcp.getRelevance();
		} else if (obj instanceof TemplateProposal) {
			TemplateProposal tp= (TemplateProposal) obj;
			return tp.getRelevance();
		}
		// catch all
		return 0;
	}

}