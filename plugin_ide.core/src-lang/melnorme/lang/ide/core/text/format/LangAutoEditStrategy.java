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
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;

import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockBalanceResult;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockTokenRule;
import melnorme.lang.ide.core.text.DocumentCommand2;
import melnorme.lang.ide.core.text.TextSourceUtils;

/**
 * LangAutoEditStrategy provides a common auto-edit strategy of smart indenting and de-indenting, 
 * for block based languages (like C-style languages)
 * 
 * TODO smart paste
 * 
 * @author BrunoM
 */
public class LangAutoEditStrategy extends AbstractAutoEditStrategy {
	
	protected static final BlockTokenRule[] BLOCK_RULES_Braces = 
			array(new BlockTokenRule('{', '}'));
	protected static final BlockTokenRule[] BLOCK_RULES_BracesAndParenthesis = 
			array(new BlockTokenRule('{', '}'), new BlockTokenRule('(', ')'));
	
	protected final ILangAutoEditsPreferencesAccess preferences;
	
	protected final String partitioning;
	protected final String contentType;
	
	protected LangAutoEditStrategy(ILastKeyInfoProvider lastKeyInfoProvider,
		ILangAutoEditsPreferencesAccess preferences) {
		this(lastKeyInfoProvider, IDocumentExtension3.DEFAULT_PARTITIONING, IDocument.DEFAULT_CONTENT_TYPE,
			preferences);
	}
	
	public LangAutoEditStrategy(ILastKeyInfoProvider lastKeyInfoProvider,
		String partitioning, String contentType,
		ILangAutoEditsPreferencesAccess preferences
	) {
		super(lastKeyInfoProvider);
		this.preferences = preferences;
		this.partitioning = partitioning;
		this.contentType = contentType;
	}
	
	protected boolean parenthesesAsBlocks;
	protected String indentUnit;
	
	@Override
	protected void doCustomizeDocumentCommand(IDocument doc, DocumentCommand2 cmd) throws BadLocationException {
		parenthesesAsBlocks = preferences.parenthesesAsBlocks();
		indentUnit = preferences.getIndentUnit();
		boolean isSmartIndent = preferences.isSmartIndent();
		
		if(isSmartIndent && isSimpleNewLineKeyPress(cmd)) {
			smartIndentAfterNewLine(doc, cmd);
		} else if(smartDeIndentAfterDeletion(doc, cmd)) {
			return;
		} else if(isSmartIndent && isSimpleKeyPressCommand(cmd)) {
			smartIndentOnKeypress(doc, cmd);
		} else {
		}
	}
	
	protected BlockHeuristicsScannner createBlockHeuristicsScanner(IDocument doc) {
		return new BlockHeuristicsScannner(doc, partitioning, contentType, getBlockRules());
	}
	
	protected BlockTokenRule[] getBlockRules() {
		if(parenthesesAsBlocks) {
			return BLOCK_RULES_BracesAndParenthesis;
		} else {
			return BLOCK_RULES_Braces;
		}
	}
	
	/* ------------------------------------- */
	
	protected void smartIndentAfterNewLine(IDocument doc, DocumentCommand2 cmd) throws BadLocationException {
		if(cmd.length > 0 || cmd.text == null)
			return;
		
		IRegion lineRegion = doc.getLineInformationOfOffset(cmd.offset);
		int lineEnd = getRegionEnd(lineRegion);
		
		int postWsEndPos = TextSourceUtils.findEndOfIndent(docContents, cmd.offset); 
		boolean hasPendingTextAfterEdit = postWsEndPos != lineEnd;
		
		
		BlockHeuristicsScannner bhscanner = createBlockHeuristicsScanner(doc);
		
		int offsetForBalanceCalculation = findOffsetForBalanceCalculation(doc, bhscanner, cmd.offset);
		int lineForBalanceCalculation = doc.getLineOfOffset(cmd.offset);
		
		// Find block balances of preceding text (line start to edit cursor)
		LineIndentResult nli = determineIndent(doc, bhscanner, lineForBalanceCalculation, offsetForBalanceCalculation);
		cmd.text += nli.nextLineIndent;
		
		BlockBalanceResult blockInfo = nli.blockInfo;
		if(blockInfo.unbalancedOpens > 0) {
			if(preferences.closeBraces() && !hasPendingTextAfterEdit){
				
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
			
			
			String startLineIndent = getLineIndentForOffset(blockStartOffset);
			
			// Now calculate the balance for the block start line, before the block start
			int lineOffset = TextSourceUtils.findLineStartForOffset(docContents, blockStartOffset);
			BlockBalanceResult blockStartInfo = bhscanner.calculateBlockBalances(lineOffset, blockStartOffset);
			
			// Add the indent of the start line, plus the unbalanced opens there
			String newLineIndent = addIndent(startLineIndent, blockStartInfo.unbalancedOpens); 
			return new LineIndentResult(null, newLineIndent, blockInfo);
		}
		
		// The indent string to be added to the new line
		String lineIndent = getLineIndentForLineStart(lineStart, editOffset);
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
	
	protected String addIndent(String indentStr, int indentDelta) {
		return indentStr + TextSourceUtils.stringNTimes(indentUnit, indentDelta);
	}
	
	/* ------------------------------------- */
	
	protected boolean smartDeIndentAfterDeletion(IDocument doc, DocumentCommand2 cmd) throws BadLocationException {
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
				int indentEnd = findEndOfIndent(indentLineRegion.getOffset());
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
			int indentEnd = findEndOfIndent(indentLineRegion.getOffset());
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
	
	protected String determineExpectedIndent(IDocument doc, int line) throws BadLocationException {
		BlockHeuristicsScannner bhscanner = createBlockHeuristicsScanner(doc);
		LineIndentResult nli = determineIndent(doc, bhscanner, line);
		String expectedIndentStr = nli.nextLineIndent;
		return expectedIndentStr;
	}
	
	/* ------------------------------------- */
	
	protected void smartIndentOnKeypress(IDocument doc, DocumentCommand2 cmd) {
		assertTrue(cmd.text.length() == 1);
		
		int offset = cmd.offset;
		
		int lineStart = TextSourceUtils.findLineStartForOffset(docContents, offset);
		String beforeCursor = docContents.substring(lineStart, offset);
		
		if(!beforeCursor.trim().isEmpty()) {
			return;
		}
		
		if(!isCloseSymbol(cmd.text)) {
			return;
		}
		char closeBrace = cmd.text.charAt(0);
		
		BlockHeuristicsScannner bhScanner = createBlockHeuristicsScanner(doc);
		int blockStartOffset = bhScanner.findBlockStart(offset, closeBrace);
		
		// Replace current indent
		cmd.offset = lineStart;
		cmd.length = beforeCursor.length();
		// With indent of block-start line
		String startLineIndent = getLineIndentForOffset(blockStartOffset);
		cmd.text = startLineIndent + closeBrace;
	}
	
	protected boolean isCloseSymbol(String string) {
		for (BlockTokenRule blockTokenRule : getBlockRules()) {
			if(string.charAt(0) == blockTokenRule.close) {
				return true;
			}
		}
		return false;
	}
	
}