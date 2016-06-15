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
package melnorme.lang.tooling.completion;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.status.StatusMessage;

/**
 * This is a tool response with either a valid result data, or an error message, but never both.
 */
public class LangCompletionResult extends ToolResponse<Indexable<ToolCompletionProposal>> {
	
	public LangCompletionResult(String errorMessage) {
		super(null, new StatusMessage(errorMessage));
	}
	
	public LangCompletionResult(Indexable<ToolCompletionProposal> proposals) {
		super(assertNotNull(proposals));
	}
	
	public boolean isErrorResult() {
		return !isValidResult();
	}
	
	public String getErrorMessage() {
		return getStatusMessageText();
	}
	
	public Indexable<ToolCompletionProposal> getProposals_maybeNull() {
		return getResultData();
	}
	
	public Indexable<ToolCompletionProposal> getValidatedProposals() throws OperationSoftFailure {
		if(isErrorResult()) {
			throw new OperationSoftFailure(getErrorMessage());
		}
		
		return getProposals_maybeNull();
	}
	
}