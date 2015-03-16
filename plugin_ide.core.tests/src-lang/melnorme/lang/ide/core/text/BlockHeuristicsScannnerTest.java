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
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.BlockTokenRule;

import org.eclipse.jface.text.BadLocationException;
import org.junit.Test;


public class BlockHeuristicsScannnerTest extends Scanner_BaseTest {
	
	public static BlockTokenRule[] SAMPLE_BLOCK_TOKENS = new BlockTokenRule[] {
		new BlockTokenRule('{', '}'),
		new BlockTokenRule('(', ')')
	};
	
	public static final boolean LEFT_INVALID = true;
	
	protected BlockHeuristicsScannner setupSource(String source) {
		getDocument().set(source);
		return setupScanner();
	}
	
	public BlockHeuristicsScannner setupScanner() {
		return new BlockHeuristicsScannner(getDocument(), null, null, SAMPLE_BLOCK_TOKENS);
	}
	
	@Test
	public void testBasic() throws Exception { testBasic$(); }
	public void testBasic$() throws Exception {
		testScanToBlockStart("", "{blah", "}", 0);
		testScanToBlockStart("", "blah", "}", 1);
		testScanToBlockStart("", "(blah", ")", 0);
		testScanToBlockStart("", "blah", ")", 1);
		
		testScanToBlockStart("", "{", "}", 0);
		testScanToBlockStart("", "", "}", 1);
	}
	
	protected void testScanToBlockStart(String srcPre, String scrBlock, String srcAfter, int expecBalance)
			throws BadLocationException {
		testScanToBlockStart(srcPre, scrBlock, srcAfter, expecBalance, false);
	}
	
	protected void testScanToBlockStart(String srcPre, String srcBlock, String srcAfter, int expecBalance,
			boolean expecInvalidLeft)
			throws BadLocationException {
		String srcBefore = srcPre + srcBlock;
		
		BlockHeuristicsScannner scanner = setupSource(srcBefore + srcAfter);
		
		char closeChar = srcAfter.charAt(0);
		assertNotNull(scanner.getOpeningPeer(closeChar));
		
		int balance = scanner.scanToBlockStart(srcBefore.length());
		
		assertTrue(balance == expecBalance);
		assertTrue(scanner.getPosition() == srcPre.length());
		assertTrue((scanner.token == AbstractDocumentScanner.TOKEN_INVALID) == expecInvalidLeft);
		
		if(balance == 0) {
			int blockEndOffset = srcBefore.length();
			if (scanner.token != AbstractDocumentScanner.TOKEN_INVALID) {
				balance = scanner.scanToBlockEnd(scanner.getPosition());
				assertTrue(balance == 0);
				assertEquals(scanner.getPosition()-1, blockEndOffset);
			} else {
				// There must be a brace next to where the scanner stopped
				scanner.scanToBlockEnd(scanner.getPosition()-1); 
				assertTrue(scanner.getPosition() == document.getLength() || scanner.getPosition() > blockEndOffset);
			}
		}
		
		// Now let's try a reverted test case, which should yield the same result
		String srcBeforeR = reverse(srcAfter);
		String srcAfterR = reverse(srcBefore);
		scanner = setupSource(srcBeforeR + srcAfterR);
		balance = scanner.scanToBlockEnd(srcBeforeR.length()-1);
		
		assertTrue(balance == expecBalance);
		assertTrue(scanner.getPosition() == reversePosition(srcPre.length()));
		assertTrue((scanner.token == AbstractDocumentScanner.TOKEN_INVALID) == expecInvalidLeft);
	}
	
	protected String reverse(String string) {
		StringBuffer buffer = new StringBuffer();
		for (int i = string.length()-1; i >= 0; i--) {
			char ch = string.charAt(i);
			char revertedCh = TextUtils.getBracePair(ch);
			buffer.append(revertedCh);
		}
		return buffer.toString();
	}
	protected int reversePosition(int position) {
		return document.getLength() - position;
	}
	
	@Test
	public void testScanToBlockStart() throws Exception { testScanToBlockStart$(); }
	public void testScanToBlockStart$() throws Exception {
		testScanToBlockStart("{{", "{blah", "}", 0);
		testScanToBlockStart("}}", "{blah", "} {}", 0);
		
		testScanToBlockStart("", "}}aaaa", "}" +NEUTRAL_SRC1, 3);
		testScanToBlockStart("", "} {abc(foo) blah;} }} aaaa", "}" +NEUTRAL_SRC2, 4);
		testScanToBlockStart("", "} (abc{foo} blah;) }} aaaa", "}" +NEUTRAL_SRC2, 4);
		
		testScanToBlockStart(NEUTRAL_SRC1+"{{() ({", "{blah(aaa)", "}", 0);
		
		testScanToBlockStart(NEUTRAL_SRC2+"{{() ({", "{blah((aaa { (asd) }  ))", "}", 0);
		
		// Now some syntax errors:
		testScanToBlockStart(NEUTRAL_SRC1+"{(", "{ )", "}", 0);
		testScanToBlockStart(NEUTRAL_SRC1+"{(", "{ (", "}", 0);
		
		testScanToBlockStart(NEUTRAL_SRC2+"{(", "( {( }", ")", 0);
		testScanToBlockStart(NEUTRAL_SRC2+"{", "", ")", 0, LEFT_INVALID);
		testScanToBlockStart(NEUTRAL_SRC2+"{", "   ", ")", 0, LEFT_INVALID);
		testScanToBlockStart(NEUTRAL_SRC2+"{", "   ))", ")", 0, LEFT_INVALID);

		testScanToBlockStart("", "   ()", "}", 1);
		testScanToBlockStart("", "   ) (", "}", 2);
		testScanToBlockStart("(({", "   ()", ")", 0, LEFT_INVALID);
		testScanToBlockStart("(({", "   ) [", ")", 0, LEFT_INVALID);
		
		testScanToBlockStart(NEUTRAL_SRC1+"(", "(({)) ))})", ")", 0);
		testScanToBlockStart(NEUTRAL_SRC1+"(", "( {(( ((} ", ")", 0);
		testScanToBlockStart(NEUTRAL_SRC1+"(({", "  {} {()} ) {(( ((} ", ")", 0, LEFT_INVALID);
		
	}
	
}