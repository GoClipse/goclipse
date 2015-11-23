/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text.format;


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertUnreachable;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITextViewerExtension;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Event;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockBalanceResult;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockTokenRule;

/**
 * LangAutoEditStrategy provides a common auto-edit strategy of smart indenting and de-indenting, 
 * for block based languages (like C-style languages)
 * 
 * TODO smart paste
 * TODO smart indent on block character keypress
 * 
 * @author BrunoM
 */
public class LangAutoEditStrategy extends DefaultIndentLineAutoEditStrategy {
	
	protected final ILangAutoEditsPreferencesAccess preferences;
	
	protected Event lastKeyEvent;
	
	
	public LangAutoEditStrategy(ITextViewer viewer, ILangAutoEditsPreferencesAccess preferences) {
		this.preferences = preferences;
		
		lastKeyEvent = new Event();
		if (viewer instanceof ITextViewerExtension) {
			VerifyKeyRecorder verifyKeyRecorder = new VerifyKeyRecorder();
			((ITextViewerExtension) viewer).appendVerifyKeyListener(verifyKeyRecorder);
			// Minor issue: we should remove verifyKeyRecorder if viewer is unconfigured
		} else {
			// allways use blank event in lastKeyEvent
		}
	}
	
	public final class VerifyKeyRecorder implements VerifyKeyListener {
		@Override
		public void verifyKey(VerifyEvent event) {
			lastKeyEvent.character = event.character;
			lastKeyEvent.keyCode = event.keyCode;
			lastKeyEvent.stateMask = event.stateMask;
		}
	}
	
	protected boolean keyWasBackspace() {
		return lastKeyEvent.character == SWT.BS;
	}
	
	protected boolean keyWasDelete() {
		return lastKeyEvent.character == SWT.DEL;
	}
	
	protected String indentUnit;
	
	@Override
	public void customizeDocumentCommand(IDocument doc, DocumentCommand cmd) {
		if (cmd.doit == false)
			return;
		
		boolean isSmartIndent = preferences.isSmartIndent();
		indentUnit = preferences.getIndentUnit();
		
		try {
			if(isSmartIndent && AutoEditUtils.isNewLineInsertionCommand(doc, cmd)) {
				smartIndentAfterNewLine(doc, cmd);
			} else if(smartDeIndentAfterDeletion(doc, cmd)) {
				return;
			} else if(lastKeyEvent.character == SWT.TAB && areEqual(cmd.text, "\t")) {
				smartTab(doc, cmd);
			} else if(isSmartIndent && AutoEditUtils.isSingleCharactedInsertionOrReplaceCommand(cmd)) {
				smartIndentOnKeypress(doc, cmd);
			} else if(preferences.isSmartPaste() && cmd.text.length() > 1) {
				smartPaste(doc, cmd); // no smart backspace for paste
			} else {
				super.customizeDocumentCommand(doc, cmd);
			}
		} catch (BadLocationException e) {
			LangCore.logError("Error in LangAutoEditStrategy", e);
		}
	}
	
	protected BlockHeuristicsScannner createBlockHeuristicsScanner(IDocument doc) {
		// Default implementation
		String partitioning = IDocumentExtension3.DEFAULT_PARTITIONING;
		String contentType = IDocument.DEFAULT_CONTENT_TYPE;
		return new BlockHeuristicsScannner(doc, partitioning, contentType, new BlockTokenRule('{', '}'));
	}
	
	/* ------------------------------------- */
	
	public static int getRegionEnd(IRegion region) {
		return region.getOffset() + region.getLength();
	}
	
	protected void smartIndentAfterNewLine(IDocument doc, DocumentCommand cmd) throws BadLocationException {
		IRegion lineRegion = doc.getLineInformationOfOffset(cmd.offset);
		int lineEnd = getRegionEnd(lineRegion);
		
		int postWsEndPos = AutoEditUtils.findEndOfWhiteSpace(doc, cmd.offset, lineEnd); 
		boolean hasPendingTextAfterEdit = postWsEndPos != lineEnd;
		
		
		BlockHeuristicsScannner bhscanner = createBlockHeuristicsScanner(doc);
		
		int offsetForBalanceCalculation = findOffsetForBalanceCalculation(doc, bhscanner, cmd.offset);
		int lineForBalanceCalculation = doc.getLineOfOffset(cmd.offset);
		
		// Find block balances of preceding text (line start to edit cursor)
		LineIndentResult nli = determineIndent(doc, bhscanner, lineForBalanceCalculation, offsetForBalanceCalculation);
		cmd.text += nli.nextLineIndent;
		
		BlockBalanceResult blockInfo = nli.blockInfo;
		if(blockInfo.unbalancedOpens > 0) {
			if(preferences.closeBlocks() && !hasPendingTextAfterEdit){
				
				if(bhscanner.shouldCloseBlock(blockInfo.rightmostUnbalancedBlockOpenOffset)) {
					//close block
					cmd.caretOffset = cmd.offset + cmd.text.length();
					cmd.shiftsCaret = false;
					String delimiter = TextUtilities.getDefaultLineDelimiter(doc);
					char openChar = doc.getChar(blockInfo.rightmostUnbalancedBlockOpenOffset);
					char closeChar = bhscanner.getClosingPeer(openChar); 
					cmd.text += delimiter + addIndent(nli.editLineIndent, blockInfo.unbalancedOpens - 1) + closeChar;
				}
			}
			return;
		}
	}
	
	protected int findOffsetForBalanceCalculation(IDocument doc, BlockHeuristicsScannner bhscanner, int offset) {
		while(offset < doc.getLength()) {
			char ch;
			try {
				ch = doc.getChar(offset);
			} catch(BadLocationException e) {
				break;
			}
			if(!bhscanner.isClosingBrace(ch)) {
				break;
			}
			offset++;
		}
		return offset;
	}
	
	public static class LineIndentResult {
		String editLineIndent;
		String nextLineIndent;
		BlockBalanceResult blockInfo;
		public LineIndentResult(String editLineIndent, String nextLineIndent, BlockBalanceResult blockInfo) {
			this.editLineIndent = editLineIndent;
			this.nextLineIndent = nextLineIndent;
			this.blockInfo = blockInfo;
		}
	}
	
	protected final LineIndentResult determineIndent(IDocument doc, BlockHeuristicsScannner bhscanner, int line) 
			throws BadLocationException {
		IRegion lineRegion = doc.getLineInformation(line);
		return determineIndent(doc, bhscanner, line, getRegionEnd(lineRegion));
	}
	
	protected LineIndentResult determineIndent(IDocument doc, BlockHeuristicsScannner bhscanner, final int line,
			final int editOffset) throws BadLocationException {
		
		IRegion lineRegion = doc.getLineInformation(line);
		final int lineStart = lineRegion.getOffset();
		assertTrue(lineStart <= editOffset && editOffset <= getRegionEnd(lineRegion));
		
		ITypedRegion partition = bhscanner.getPartition(lineStart);
		if(partitionIsIgnoredForLineIndentString(editOffset, partition)) {
			if(line == 0) {
				// empty/zero block balance
				return new LineIndentResult("", "", new BlockBalanceResult());
			} else {
				return determineIndent(doc, bhscanner, line-1);
			}
		}
		
		BlockBalanceResult blockInfo = bhscanner.calculateBlockBalances(lineStart, editOffset);
		
		if(blockInfo.unbalancedOpens == 0 && blockInfo.unbalancedCloses > 0) {
			int blockStartOffset = bhscanner.findBlockStart(blockInfo.rightmostUnbalancedBlockCloseOffset);
			assertTrue(doc.getLineOfOffset(blockStartOffset) <= doc.getLineOfOffset(lineStart));
			
			IRegion blockStartLineInfo = doc.getLineInformationOfOffset(blockStartOffset);
			
			String startLineIndent = getLineIndent(doc, blockStartLineInfo);
			
			// Now calculate the balance for the block start line, before the block start
			int lineOffset = blockStartLineInfo.getOffset();
			BlockBalanceResult blockStartInfo = bhscanner.calculateBlockBalances(lineOffset, blockStartOffset);
			
			// Add the indent of the start line, plus the unbalanced opens there
			String newLineIndent = addIndent(startLineIndent, blockStartInfo.unbalancedOpens); 
			return new LineIndentResult(null, newLineIndent, blockInfo);
		}
		
		// The indent string to be added to the new line
		String lineIndent = getLineIndent(doc, lineStart, editOffset);
		if(blockInfo.unbalancedOpens == 0 && blockInfo.unbalancedCloses == 0) {
			return new LineIndentResult(null, lineIndent, blockInfo); // finished
		}
		
		if(blockInfo.unbalancedOpens > 0) {
			String newLineIndent = addIndent(lineIndent, blockInfo.unbalancedOpens);
			// cache lineIndent so as to not recalculate
			return new LineIndentResult(lineIndent, newLineIndent, blockInfo); // finished
		}
		throw assertUnreachable();
	}
	
	/** Subclasses may override. */
	protected boolean partitionIsIgnoredForLineIndentString(final int editOffset, ITypedRegion partition) {
		return partitionTypeIsIgnoredForLineIndentString(partition) && editOffset <= getRegionEnd(partition);
	}
	
	/** Subclasses may override. */
	protected boolean partitionTypeIsIgnoredForLineIndentString(ITypedRegion partition) {
		return !partition.getType().equals(IDocument.DEFAULT_CONTENT_TYPE);
	}
	
	protected static String getLineIndent(IDocument doc, IRegion line) throws BadLocationException {
		return getLineIndent(doc, line.getOffset(), getRegionEnd(line));
	}
	
	protected static String getLineIndent(IDocument doc, int start, int end) throws BadLocationException {
		assertTrue(start <= end);
		int indentEnd = AutoEditUtils.findEndOfWhiteSpace(doc, start, end);
		return doc.get(start, indentEnd - start);
	}
	
	protected String addIndent(String indentStr, int indentDelta) {
		return indentStr + LangAutoEditUtils.stringNTimes(indentUnit, indentDelta);
	}
	
	/* ------------------------------------- */
	
	protected boolean smartDeIndentAfterDeletion(IDocument doc, DocumentCommand cmd) throws BadLocationException {
		if(!preferences.isSmartDeIndent())
			return false;
		
		if(!cmd.text.isEmpty())
			return false;
		
		IRegion lineRegion = doc.getLineInformationOfOffset(cmd.offset);
		int lineEnd = getRegionEnd(lineRegion);
		int line = doc.getLineOfOffset(cmd.offset);
		
		
		// Delete at beginning of NL
		if(cmd.offset == lineEnd && lengthMatchesLineDelimiter(cmd.length, doc.getLineDelimiter(line))) {
			if(keyWasBackspace())
				return false; // Only Delete key should trigger this edit
			
			int indentLine = line+1;
			if(indentLine < doc.getNumberOfLines()) {
				assertTrue(doc.getLineInformation(indentLine).getOffset() == cmd.offset + cmd.length);
				
				IRegion indentLineRegion = doc.getLineInformation(indentLine);
				int indentEnd = findEndOfWhiteSpace(doc, indentLineRegion);
				String deletableIndentStr = calculateDeletableIndent(doc, indentLine, indentEnd);
				if(equalsDocumentString(deletableIndentStr, doc, indentLineRegion)) {
					cmd.length += deletableIndentStr.length();
					return true;
				}
			}
			return false;
		}
		
		// Backspace at end of indent case
		if(cmd.length == 1 && isIndentWhiteSpace(doc.getChar(cmd.offset)) && line > 0) {
			if(keyWasDelete())
				return false; // Only Backspace key should trigger this
			
			
			IRegion indentLineRegion = lineRegion;
			int indentLine = line;
			int indentEnd = findEndOfWhiteSpace(doc, indentLineRegion);
			if(cmd.offset < indentEnd) {
				// potentially true
				
				String deletableIndentStr = calculateDeletableIndent(doc, indentLine, indentEnd);
				if(equalsDocumentString(deletableIndentStr, doc, indentLineRegion)) {
					int acceptedIndentEnd = indentLineRegion.getOffset() + deletableIndentStr.length();
					if(cmd.offset == acceptedIndentEnd-1) {
						int lineDelimLen = doc.getLineDelimiter(line-1).length();
						cmd.offset = indentLineRegion.getOffset() - lineDelimLen;
						cmd.length = lineDelimLen + deletableIndentStr.length();
						return true;
					}
				}
			}
			return false;
		}
		
		return false;
	}
	
	protected static boolean lengthMatchesLineDelimiter(int length, String lineDelimiter) {
		return lineDelimiter != null && length == lineDelimiter.length();
	}
	
	protected boolean isIndentWhiteSpace(char ch) throws BadLocationException {
		return ch == ' ' || ch == '\t';
	}
	
	protected String calculateDeletableIndent(IDocument doc, int indentedLine, int indentEnd)
			throws BadLocationException {
		IRegion indentedLineRegion = doc.getLineInformation(indentedLine);

		String expectedIndentStr = determineExpectedIndent(doc, indentedLine-1);
		int indentLength = indentEnd - indentedLineRegion.getOffset(); 
		if(indentLength < expectedIndentStr.length()) {
			// cap expected length
			expectedIndentStr = expectedIndentStr.substring(0, indentLength);
		}
		return expectedIndentStr;
	}
	
	protected boolean equalsDocumentString(String expectedIndentStr, IDocument doc, IRegion lineRegion)
			throws BadLocationException {
		int length = Math.min(lineRegion.getLength(), expectedIndentStr.length());
		String lineIndent = doc.get(lineRegion.getOffset(), length);
		return expectedIndentStr.equals(lineIndent);
	}
	
	protected int findEndOfWhiteSpace(IDocument doc, IRegion region) throws BadLocationException {
		return AutoEditUtils.findEndOfWhiteSpace(doc, region.getOffset(), getRegionEnd(region));
	}
	
	protected String determineExpectedIndent(IDocument doc, int line) throws BadLocationException {
		BlockHeuristicsScannner bhscanner = createBlockHeuristicsScanner(doc);
		LineIndentResult nli = determineIndent(doc, bhscanner, line);
		String expectedIndentStr = nli.nextLineIndent;
		return expectedIndentStr;
	}
	
	/* ------------------------------------- */
	
	protected void smartIndentOnKeypress(IDocument doc, DocumentCommand cmd) throws BadLocationException {
		super.customizeDocumentCommand(doc, cmd);
	}
	
	protected void smartPaste(IDocument doc, DocumentCommand cmd) throws BadLocationException {
		super.customizeDocumentCommand(doc, cmd);
	}
	
	protected void smartTab(IDocument doc, DocumentCommand cmd) {
		cmd.text = preferences.getIndentUnit();
		super.customizeDocumentCommand(doc, cmd);
	}
	
}