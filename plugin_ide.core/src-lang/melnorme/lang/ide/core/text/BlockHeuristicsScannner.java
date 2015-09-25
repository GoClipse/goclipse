/*******************************************************************************
 * Copyright (c) 2010 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

/**
 * A scanner to parse block tokens, and determine the balance of open vs. close tokens. 
 * The blocks are specified by pairs of characters (must be one char in length each).
 * The scanning is partition aware, it only parse partitions of a given type.
 * 
 * The scanner is heuristic in that the block balance may not be 100% accurate according to
 * the underlying language sematics of the source being scanned.
 */
public class BlockHeuristicsScannner extends AbstractDocumentScanner {
	
	public static final class BlockTokenRule {
		public final char open;
		public final char close;
		public BlockTokenRule(char open, char close) {
			this.open = open;
			this.close = close;
		}
	}
	
	protected final BlockTokenRule[] blockRules;
	protected final BlockTokenRule[] blockRulesReversed;
	
	public BlockHeuristicsScannner(IDocument document, String partitioning, String contentType, 
			BlockTokenRule... blockRules) {
		super(document, partitioning, contentType);
		this.blockRules = blockRules;
		
		blockRulesReversed = new BlockTokenRule[blockRules.length];
		for (int i = 0; i < blockRules.length; i++) {
			BlockTokenRule blockRule = blockRules[i];
			blockRulesReversed[i] = new BlockTokenRule(blockRule.close, blockRule.open);
		}
	}
	
	public char getClosingPeer(char openChar) {
		return getMatchingPeer(openChar, blockRules);
	}
	
	public char getOpeningPeer(char closeChar) {
		return getMatchingPeer(closeChar, blockRulesReversed);
	}
	
	public static char getMatchingPeer(char openChar, BlockTokenRule[] blockTokenRules) {
		for (int i = 0; i < blockTokenRules.length; i++) {
			BlockTokenRule blockRule = blockTokenRules[i];
			if(blockRule.open == openChar){
				return blockRule.close;
			}
		}
		throw assertFail();
	}
	
	protected int getPriorityOfBlockToken(char blockToken) {
		for (int i = 0; i < blockRules.length; i++) {
			BlockTokenRule blockRule = blockRules[i];
			if(blockRule.open == blockToken || blockRule.close == blockToken) {
				return i;
			}
		}
		throw assertFail();
	}
	
	
	/*-------------------*/
	
	public static class BlockBalanceResult {
		public int unbalancedOpens = 0;
		public int unbalancedCloses = 0;
		public int rightmostUnbalancedBlockCloseOffset = -1;
		public int rightmostUnbalancedBlockOpenOffset = -1;
	}
	
	
	/** Calculate the block balance in given range. */
	public BlockBalanceResult calculateBlockBalances(int beginPos, int endPos) throws BadLocationException {
		// Calculate backwards
		setScanRange(endPos, beginPos);
		// Ideally we would fully parse the code to figure the delta.
		// But ATM we just estimate using number of blocks
		BlockBalanceResult result = new BlockBalanceResult();
		
		while(readPreviousCharacter() != TOKEN_EOF) {
			for (int i = 0; i < blockRules.length; i++) {
				BlockTokenRule blockRule = blockRules[i];
				
				if(token == blockRule.close) {
					int blockCloseOffset = getPosition();
					
					// do a subscan
					int balance = scanToBlockPeer(i, prevTokenFn, blockRules);
					if(balance > 0) {
						// block start not found
						result.unbalancedCloses = balance;
						result.rightmostUnbalancedBlockCloseOffset = blockCloseOffset;
						return result;
					}
					break;
				} 
				if(token == blockRule.open) {
					result.unbalancedOpens++;
					
					if(result.rightmostUnbalancedBlockOpenOffset == -1) {
						result.rightmostUnbalancedBlockOpenOffset = getPosition();
					}
					break;
				}
			}
		}
		return result;
	}
	
	protected abstract class FnTokenAdvance {
		protected abstract int advanceToken() throws BadLocationException;

		protected abstract void revertToken() ;
	}
	
	protected final FnTokenAdvance prevTokenFn = new FnTokenAdvance() {
		@Override
		protected int advanceToken() throws BadLocationException {
			return readPreviousCharacter();
		}
		@Override
		protected void revertToken() {
			revertPreviousCharacter();
		}
	};
	protected final FnTokenAdvance nextTokenFn = new FnTokenAdvance() {
		@Override
		protected int advanceToken() throws BadLocationException {
			return readNextCharacter();
		}
		@Override
		protected void revertToken() {
			revertNextCharacter();
		}
	};
	
	public int scanToBlockStart(int blockCloseOffset) throws BadLocationException {
		setPosition(blockCloseOffset);
		posLimit = 0;
		char blockClose = document.getChar(blockCloseOffset);
		return scanToBlockStartForChar(blockClose, prevTokenFn, blockRules);
	}
	
	public int scanToBlockEnd(int blockOpenOffset) throws BadLocationException {
		setScanRange(blockOpenOffset+1, document.getLength());
		char blockOpen = document.getChar(blockOpenOffset);
		return scanToBlockEnd(blockOpen);
	}
	
	protected int scanToBlockEnd(char blockOpen) throws BadLocationException {
		return scanToBlockStartForChar(blockOpen, nextTokenFn, blockRulesReversed);
	}
	
	protected int scanToBlockStartForChar(char blockClose, FnTokenAdvance fnAdvance, BlockTokenRule[] blockTkRules)
			throws BadLocationException {
		int ix = getPriorityOfBlockToken(blockClose);
		return scanToBlockPeer(ix, fnAdvance, blockTkRules);
	}

	/** Scans in search of a block peer (open/close).
	 * Stops on EOF, or when block peer is found (balance is 0)
	 * @return 0 if block peer token was found (even if assumed by a syntax correction), 
	 * or a count of how many blocks were left open.
	 */
	protected int scanToBlockPeer(int expectedTokenIx, FnTokenAdvance fnAdvance, BlockTokenRule[] blockTkRules)
			throws BadLocationException {
		assertTrue(expectedTokenIx >= 0 && expectedTokenIx < blockTkRules.length);
		while(fnAdvance.advanceToken() != TOKEN_EOF) {
			for (int i = 0; i < blockTkRules.length; i++) {
				BlockTokenRule blockRule = blockTkRules[i];
				
				if(token == blockRule.close) {
					int pendingBlocks = scanToBlockPeer(i, fnAdvance, blockTkRules);
					if(pendingBlocks > 0) {
						return pendingBlocks + 1;
					}
					break;
				}
				if(token == blockRule.open) {
					if(i == expectedTokenIx){
						return 0; 
					} else {
						// syntax error
						if(i < expectedTokenIx) {
							// Stronger rule takes precedence.
							// Assume syntax correction, as if blockRule[expectedTokenIx].open was found:
							fnAdvance.revertToken();
							token = TOKEN_INVALID;
							return 0; 
						} else {
							// ignore token
						}
					}
					break;
				} 
			}
		}
		return 1; // Balance is 1 if we reached the end without finding peer
	}
	
	/** Finds the offset where starts the blocks whose end token is at given blockCloseOffset */
	public int findBlockStart(int blockCloseOffset) throws BadLocationException {
		scanToBlockStart(blockCloseOffset);
		return getPosition();
	}
	
	public boolean shouldCloseBlock(int blockOpenOffset) throws BadLocationException {
		char primaryBlockOpen = document.getChar(blockOpenOffset);
		int primaryBlockPriority = getPriorityOfBlockToken(primaryBlockOpen);
		char blockOpen = primaryBlockOpen;
		
		int leftOffset = blockOpenOffset;
		int rightOffset = blockOpenOffset+1;
		while(true) {
			assertTrue(getPriorityOfBlockToken(blockOpen) == primaryBlockPriority);
			
			setScanRange(rightOffset, document.getLength());
			int balance = scanToBlockEnd(blockOpen);
			
			if(balance == 0 && token == TOKEN_INVALID) {
				return true; // a block close is necessary
			}
			
			if(balance > 0) {
				return true;
			}
			
			// Otherwise look for unmatched block opens on left, that are at least as important at the primary block
			
			rightOffset = getPosition(); // save value for later iterations
			setScanRange(leftOffset, 0);
			int balanceToTheLeft = findUnmatchedOpen(primaryBlockPriority);
			leftOffset = getPosition(); // save value for later iterations
			
			if(balanceToTheLeft <= 0) {
				return false; // relevant balance is zero or less, should not close
			} else {
				// Got an unmatched open
				blockOpen = (char) token;
				if(getPriorityOfBlockToken(blockOpen) < primaryBlockPriority) {
					// opening is from syntax-dominant block, doesn't matter the rest of the balance
					return false;
				}				
				continue;
			}
		}
	}
	
	protected int findUnmatchedOpen(int requiredPriority) throws BadLocationException {
		while(prevTokenFn.advanceToken() != TOKEN_EOF) {
			for (int i = 0; i < blockRules.length; i++) {
				BlockTokenRule blockRule = blockRules[i];
				
				if(token == blockRule.close) {
					// do a subscan
					if(scanToBlockPeer(i, prevTokenFn, blockRules) > 0) {
						// block start not found, so there is an unmatched close
						return -1; 
					}
					break;
				}
				if(token == blockRule.open && (getPriorityOfBlockToken((char) token) <= requiredPriority)) {
					return 1;
				}
			}
		}
		return 0;
	}
	
}