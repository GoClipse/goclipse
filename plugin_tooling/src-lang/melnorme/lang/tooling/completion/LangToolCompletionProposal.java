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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.text.MessageFormat;

import melnorme.lang.tooling.CompletionProposalKind;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.misc.CollectionUtil;
import melnorme.utilbox.misc.HashcodeUtil;


public abstract class LangToolCompletionProposal {
	
	protected final int replaceOffset;
	protected final int replaceLength;
	protected final String baseReplaceString;
	protected final String label;
	protected final CompletionProposalKind kind;
	protected final ElementAttributes attributes;
	protected final String moduleName; // can be null
	protected final String description;
	
	protected final String fullReplaceString;
	protected final Indexable<SourceRange> sourceSubElements;
	
	public LangToolCompletionProposal(int replaceOffset, int replaceLength, String baseReplaceString, 
			String label, CompletionProposalKind kind, ElementAttributes attributes, 
			String moduleName, String description,
			String fullReplaceString, Indexable<SourceRange> sourceSubElements) {
		assertTrue(replaceOffset >= 0);
		assertTrue(replaceLength >= 0);
		this.replaceOffset = replaceOffset;
		this.replaceLength = replaceLength;
		this.baseReplaceString = assertNotNull(baseReplaceString);
		this.label = assertNotNull(label);
		this.kind = kind != null ? kind : CompletionProposalKind.UNKNOWN;
		this.attributes = assertNotNull(attributes);
		this.moduleName = moduleName;
		this.description = description;
		
		this.fullReplaceString = assertNotNull(fullReplaceString);
		this.sourceSubElements = CollectionUtil.nullToEmpty(sourceSubElements);
	}
	
	public int getReplaceOffset() {
		return replaceOffset;
	}
	
	public int getReplaceLength() {
		return replaceLength;
	}
	
	public String getBaseReplaceString() {
		return baseReplaceString;
	}
	
	public String getLabel() {
		return assertNotNull(label);
	}
	
	public CompletionProposalKind getKind() {
		return assertNotNull(kind);
	}
	
	public ElementAttributes getAttributes() {
		return attributes;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	
	public String getDescription() {
		return description;
	}
	
	/** @return Alternative replace string, with more completion text. 
	 * Usually used for functions complete proposals that include parameters as well.*/
	public String getFullReplaceString() {
		return fullReplaceString;
	}
	
	public Indexable<SourceRange> getSourceSubElements() {
		return sourceSubElements;
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
				areEqual(baseReplaceString, other.baseReplaceString) &&
				areEqual(label, other.label) &&
				areEqual(kind, other.kind) &&
				areEqual(moduleName, other.moduleName) &&
				areEqual(description, other.description) &&
				areEqual(fullReplaceString, other.fullReplaceString) &&
				areEqual(sourceSubElements, other.sourceSubElements) &&
				subclassEquals(other);
	}
	
	protected abstract boolean subclassEquals(LangToolCompletionProposal other);
	
	@Override
	public int hashCode() {
		return HashcodeUtil.combinedHashCode(
			replaceOffset, replaceLength, baseReplaceString, label, kind, moduleName);
	}
	
}