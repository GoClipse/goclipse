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
package melnorme.lang.tooling.completion;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.utilbox.collections.Indexable;

public class LangCompletionResult {
	
	protected final String errorMessage;
	protected final Indexable<LangCompletionProposal> proposals;
	
	public LangCompletionResult(String errorMessage) {
		super();
		this.errorMessage = assertNotNull(errorMessage);
		this.proposals = null;
	}
	
	public LangCompletionResult(Indexable<LangCompletionProposal> proposals) {
		this.errorMessage = null;
		this.proposals = proposals;
	}
	
	public boolean isErrorResult() {
		return errorMessage != null;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public Indexable<LangCompletionProposal> getProposals() {
		return proposals;
	}
	
}