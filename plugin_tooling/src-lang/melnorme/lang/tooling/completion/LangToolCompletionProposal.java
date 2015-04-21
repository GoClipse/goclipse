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
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.text.MessageFormat;

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.utilbox.misc.HashcodeUtil;


public abstract class LangToolCompletionProposal {
	
	protected final int replaceOffset;
	protected final int replaceLength;
	protected final String replaceString;
	protected final String label;
	protected final CompletionProposalKind kind;
	protected final String moduleName; // can be null
	
	public LangToolCompletionProposal(int replaceOffset, int replaceLength, String replaceString, String label,
			CompletionProposalKind kind, String moduleName) {
		super();
		this.replaceOffset = replaceOffset;
		this.replaceLength = replaceLength;
		this.replaceString = assertNotNull(replaceString);
		this.label = assertNotNull(label);
		this.kind = assertNotNull(kind);
		this.moduleName = moduleName;
	}
	
	public int getReplaceOffset() {
		return replaceOffset;
	}
	
	public int getReplaceLength() {
		return replaceLength;
	}
	
	public String getReplaceString() {
		return replaceString;
	}
	
	public String getLabel() {
		return label;
	}
	
	public CompletionProposalKind getKind() {
		return kind;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	@Override
	public String toString() {
		return MessageFormat.format("PROPOSAL: @{0}:{1} {2} [{3}]{4}",
			getReplaceOffset(), getReplaceLength(),
			getLabel(),
			getKind(),
			moduleName == null ? "" : (" - " + moduleName)
			);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(!(obj instanceof LangToolCompletionProposal)) return false;
		
		LangToolCompletionProposal other = (LangToolCompletionProposal) obj;
		
		return 
				areEqual(replaceOffset, other.replaceOffset) &&
				areEqual(replaceLength, other.replaceLength) &&
				areEqual(replaceString, other.replaceString) &&
				areEqual(label, other.label) &&
				areEqual(kind, other.kind) &&
				areEqual(moduleName, other.moduleName) &&
				subclassEquals(other);
	}
	
	protected abstract boolean subclassEquals(LangToolCompletionProposal other);
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(replaceOffset, replaceLength, replaceString, label, kind, moduleName);
	}
	
}