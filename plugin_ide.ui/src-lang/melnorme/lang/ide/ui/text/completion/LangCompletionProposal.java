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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import melnorme.lang.tooling.ToolCompletionProposal;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class LangCompletionProposal implements ICompletionProposal, ICompletionProposalExtension6 {
	
	protected final ToolCompletionProposal proposal;
	
	protected final String additionalProposalInfo;
	protected final Image image;
	protected final IContextInformation contextInformation;
	
	public LangCompletionProposal(ToolCompletionProposal proposal,
			String additionalProposalInfo, 
			Image image, 
			IContextInformation contextInformation) {
		super();
		this.proposal = assertNotNull(proposal);
		this.additionalProposalInfo = additionalProposalInfo;
		this.image = image;
		this.contextInformation = contextInformation;
	}
	
	protected int getReplaceOffset() {
		return proposal.getReplaceOffset();
	}
	
	protected int getReplaceLength() {
		return proposal.getReplaceLength();
	}
	
	protected String getReplaceString() {
		return proposal.getReplaceString();
	}
	
	@Override
	public void apply(IDocument document) {
		try {
			document.replace(getReplaceOffset(), getReplaceLength(), getReplaceString());
		} catch (BadLocationException x) {
			// ignore
		}
	}
	
	@Override
	public Point getSelection(IDocument document) {
		return new Point(getReplaceOffset() + getReplaceString().length(), 0);
	}
	
	@Override
	public String getAdditionalProposalInfo() {
		return additionalProposalInfo;
	};

	@Override
	public String getDisplayString() {
		return getStyledDisplayString().toString();
	};
	
	protected StyledString styledDisplayString;
	
	@Override
	public StyledString getStyledDisplayString() {
		if(styledDisplayString == null) {
			StyledString styledString;
			styledString = new StyledString(proposal.getLabel());
			styledString.append(new StyledString(" - " + proposal.getModuleName(), StyledString.QUALIFIER_STYLER));
			styledDisplayString = styledString;
		}
		return styledDisplayString;
	}
	
	@Override
	public Image getImage() {
		return image;
	};
	
	@Override
	public IContextInformation getContextInformation() {
		return contextInformation;
	};
	
}