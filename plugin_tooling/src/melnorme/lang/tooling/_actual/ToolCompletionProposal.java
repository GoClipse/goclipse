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
package melnorme.lang.tooling._actual;

import melnorme.lang.tooling.completion.LangToolCompletionProposal;

public class ToolCompletionProposal extends LangToolCompletionProposal<Void> {
	
	public ToolCompletionProposal(int completionLocation, String replaceString, int replaceLength, String label,
			Void extraData) {
		super(completionLocation, replaceString, replaceLength, label, extraData);
	}
	
	@Override
	protected boolean subclassEquals(LangToolCompletionProposal<?> other) {
		return true;
	}
	
}