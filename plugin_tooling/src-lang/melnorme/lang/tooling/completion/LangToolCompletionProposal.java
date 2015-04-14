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

import static melnorme.utilbox.core.CoreUtil.areEqual;
import melnorme.utilbox.misc.HashcodeUtil;


public abstract class LangToolCompletionProposal<EXTRA> {
	
	protected final int completionOffset;
	protected final String replaceString;
	protected final int replaceLength;
	protected final String label;
	protected final EXTRA extraData;
	
	public LangToolCompletionProposal(int completionLocation, String replaceString, int replaceLength, String label,
			EXTRA extraData) {
		this.completionOffset = completionLocation;
		this.replaceString = replaceString;
		this.replaceLength = replaceLength;
		this.label = label;
		this.extraData = extraData;
	}
	
	public String getReplaceString() {
		return replaceString;
	}
	
	public int getReplaceStart() {
		return completionOffset;
	}
	
	public int getReplaceLength() {
		return replaceLength;
	}
	
	public EXTRA getExtraData() {
		return extraData;
	}
	
	@Override
	public String toString() {
		return "PROPOSAL: " + "@" + getReplaceStart() + ":" + getReplaceLength() + " " + getReplaceString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LangToolCompletionProposal)) return false;
		
		LangToolCompletionProposal<?> other = (LangToolCompletionProposal<?>) obj;
		
		return 
				completionOffset == other.completionOffset &&
				areEqual(replaceString, other.replaceString) &&
				replaceLength == other.replaceLength &&
				areEqual(label, other.label) &&
				subclassEquals(other);
	}
	
	protected abstract boolean subclassEquals(LangToolCompletionProposal<?> other);
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(completionOffset, replaceString, replaceLength);
	}
	
}