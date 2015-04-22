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
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.text.TextUtils;
import melnorme.lang.tooling.ToolCompletionProposal;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.utilbox.collections.Indexable;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension5;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension6;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.link.ILinkedModeListener;
import org.eclipse.jface.text.link.LinkedModeModel;
import org.eclipse.jface.text.link.LinkedModeUI;
import org.eclipse.jface.text.link.LinkedPosition;
import org.eclipse.jface.text.link.LinkedPositionGroup;
import org.eclipse.jface.text.link.LinkedModeUI.ExitFlags;
import org.eclipse.jface.text.link.LinkedModeUI.IExitPolicy;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.link.EditorLinkedModeUI;

public class LangCompletionProposal implements 
	ICompletionProposal, 
	ICompletionProposalExtension,
	ICompletionProposalExtension2,
	ICompletionProposalExtension3,
	ICompletionProposalExtension5,
	ICompletionProposalExtension6 
{
	
	protected final ToolCompletionProposal proposal;
	
	protected final String additionalProposalInfo;
	protected final Image image;
	protected final IContextInformation contextInformation;
	
	protected int relevance = 0;
	protected int replaceLength;
	protected int cursorPosition;
	protected StyledString styledDisplayString;
	
	public LangCompletionProposal(ToolCompletionProposal proposal,
			String additionalProposalInfo, 
			Image image, 
			IContextInformation contextInformation) {
		super();
		this.proposal = assertNotNull(proposal);
		this.additionalProposalInfo = additionalProposalInfo;
		this.image = image;
		this.contextInformation = contextInformation;
		
		this.relevance = getDefaultRelevance();
		this.replaceLength = proposal.getReplaceLength();
		this.cursorPosition = getReplacementStringEndOffset();
	}
	
	protected int getReplaceOffset() {
		return proposal.getReplaceOffset();
	}
	
	protected int getReplaceLength() {
		return replaceLength;
	}
	
	public String getBaseReplaceString() {
		return proposal.getReplaceString();
	}
	
	public String getEffectiveReplaceString() {
		return proposal.getFullReplaceString();
	}
	
	public int getRelevance() {
		return relevance;
	}
	
	protected int getDefaultRelevance() {
		String underlyingElementName = getUnderlyingElementName();
		if(underlyingElementName != null && underlyingElementName.startsWith("_")) {
			// In C-style languages, identifiers that start with "_" are typically reserved values,
			// so make it less relevant
			return 10;
		}
		return 0;
	}
	
	protected String getUnderlyingElementName() {
		return proposal.getLabel();
	}
	
	public String getSortString() {
		return proposal.getLabel();
	}
	
	@Override
	public String getDisplayString() {
		return proposal.getLabel();
	}
	
	@Override
	public StyledString getStyledDisplayString() {
		if(styledDisplayString == null) {
			StyledString styledString = new StyledString(proposal.getLabel());
			
			String labelModuleSuffix = getLabelModuleSuffix();
			if(labelModuleSuffix != null) {
				styledString.append(new StyledString(labelModuleSuffix, StyledString.QUALIFIER_STYLER));
			}
			styledDisplayString = styledString;
		}
		return styledDisplayString;
	}
	
	protected String getLabelModuleSuffix() {
		String moduleName = proposal.getModuleName();
		return moduleName == null ? null : " - " + moduleName;
	}
	
	@Override
	public Image getImage() {
		return image;
	};
	
	@Override
	public IContextInformation getContextInformation() {
		return contextInformation;
	};
	
	@Override
	public int getContextInformationPosition() {
		return -1;
	}
	
	@Override
	public void selected(ITextViewer viewer, boolean smartToggle) {
	}
	
	@Override
	public void unselected(ITextViewer viewer) {
	}
	
	@Override
	public IInformationControlCreator getInformationControlCreator() {
		return null; // TODO: browser control
	}
	
	@Override
	public String getAdditionalProposalInfo() {
		Object info = getAdditionalProposalInfo(new NullProgressMonitor());
		return info != null ? info.toString() : null;
	};
	
	@Override
	public Object getAdditionalProposalInfo(IProgressMonitor monitor) {
		return additionalProposalInfo;
	}
	
	/* ----------------- Application ----------------- */
	
	@Override
	public char[] getTriggerCharacters() {
		return null;
	}
	
	protected int getCursorPosition() {
		return cursorPosition;
	}
	
	protected int getReplacementStringEndOffset() {
		return getReplaceOffset() + getEffectiveReplaceString().length();
	}
	
	@Override
	public boolean isValidFor(IDocument document, int offset) {
		return validate(document, offset, null);
	}

	@Override
	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		if(offset < getReplaceOffset())
			return false;
		
		String prefix;
		try {
			prefix = document.get(getReplaceOffset(), offset - getReplaceOffset());
		} catch (BadLocationException e) {
			return false;
		}
		boolean validPrefix = isValidPrefix(prefix);
		
		if(validPrefix && event != null) {
			// adapt replacement length to document event/ 
			int eventEndOffset = event.fOffset + event.fLength;
			// The event should be a common prefix completion (this should be true anyways) :
			int replaceEndPos = getReplaceOffset() + getReplaceLength();
			if(event.fOffset >= getReplaceOffset() && eventEndOffset <= replaceEndPos) {
				int delta = (event.fText == null ? 0 : event.fText.length()) - event.fLength;
				this.replaceLength = Math.max(getReplaceLength() + delta, 0);
			}
		}
		
		return validPrefix;
	}
	
	protected boolean isValidPrefix(String prefix) {
		String rplString = getBaseReplaceString();
		return TextUtils.isPrefix(prefix, rplString, true);
	}
	
	@Override
	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
		return getReplaceOffset();
	}
	
	@Override
	public CharSequence getPrefixCompletionText(IDocument document, int completionOffset) {
		return getBaseReplaceString();
	}
	
	@Override
	public void apply(IDocument document) {
		apply(document, (char) 0, 0);
	}
	
	@Override
	public void apply(IDocument document, char trigger, int offset) {
		try {
			document.replace(getReplaceOffset(), getReplaceLength(), getEffectiveReplaceString());
		} catch (BadLocationException x) {
			// ignore
		}
	}
	
	@Override
	public void apply(ITextViewer viewer, char trigger, int stateMask, int offset) {
		apply(viewer.getDocument(), trigger, offset);
		
		try {
			applyLinkedMode(viewer);
		} catch (BadLocationException e) {
			LangCore.logInternalError(e);
		}
	}
	
	protected void applyLinkedMode(ITextViewer viewer) throws BadLocationException {
		LinkedModeModel model = getLinkedModeModel(viewer);
		if(model == null) {
			return;
		}
		model.forceInstall();
		
		LinkedModeUI ui = new EditorLinkedModeUI(model, viewer);
		ui.setExitPolicy(new CompletionProposalExitPolicy());
		ui.setExitPosition(viewer, getCursorPosition(), 0, Integer.MAX_VALUE);
		if(firstLinkedModeGroupPosition != -1) {
			cursorPosition = firstLinkedModeGroupPosition;
		}
		ui.setCyclingMode(LinkedModeUI.CYCLE_WHEN_NO_PARENT);
		ui.setDoContextInfo(true);
		
//		ui.enableColoredLabels(true);
		
		ui.enter();
	}
	
	protected int firstLinkedModeGroupPosition; 
	
	protected LinkedModeModel getLinkedModeModel(ITextViewer viewer) throws BadLocationException {
		Indexable<SourceRange> sourceSubElements = proposal.getSourceSubElements();
		if(sourceSubElements == null || sourceSubElements.isEmpty()) {
			return null;
		}
		
		LinkedModeModel model = new LinkedModeModel();
		
		IDocument document = viewer.getDocument();
		int replaceOffset = getReplaceOffset();
		
		firstLinkedModeGroupPosition = -1;
		
		for (SourceRange sr : sourceSubElements) {
			LinkedPositionGroup group = new LinkedPositionGroup();
			int posOffset = replaceOffset + sr.getOffset();
			group.addPosition(new LinkedPosition(document, posOffset, sr.getLength()));
			if(firstLinkedModeGroupPosition == -1) {
				firstLinkedModeGroupPosition = posOffset;
			}
			model.addGroup(group);
		}
		
		return model;
	}
	
	@Override
	public Point getSelection(IDocument document) {
		return new Point(getCursorPosition(), 0);
	}
	
	protected class CompletionProposalExitPolicy implements IExitPolicy {
		@Override
		public ExitFlags doExit(LinkedModeModel model, VerifyEvent event, int offset, int length) {
			switch (event.character) {
			case SWT.CR:
				if(offset == getReplacementStringEndOffset()) {
					return new ExitFlags(ILinkedModeListener.EXIT_ALL, true);
				}
				return new ExitFlags(ILinkedModeListener.UPDATE_CARET, false);
			}
			return null;
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public String toString() {
		return proposal.toString();
	}
	
}