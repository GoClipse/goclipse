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
package melnorme.lang.ide.core_text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.BadLocationException;
import org.junit.Test;

import melnorme.lang.ide.core.text.AbstractDocumentScanner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner;
import melnorme.lang.ide.core.text.BlockHeuristicsScannner.FnTokenAdvance;

public class BlockHeuristicsScannner_PartitionsTest extends BlockHeuristicsScannnerTest {
	
	@Override
	public BlockHeuristicsScannner setupScanner() {
		return Scanner_BaseTest.createBlockHeuristicScannerWithSamplePartitioning(getDocument());
	}
	
	@Test
	public void testScanWithPartitions() throws Exception { testScanWithPartitions$(); }
	public void testScanWithPartitions$() throws Exception {
		testTokenStream("{()}", array("{", "(", ")", "}"));
		testTokenStream("{(//)}"+NL+"ab", array("{", "(", "//)}"+NL, "a","b"));
		testTokenStream("/**/)}", array("/**/", ")", "}"));
		testTokenStream(")}'aa'", array(")", "}", "'aa'"));
		testTokenStream("/**/abcd'bb''aa'/**/x/**/", array("/**/","a","b","c","d","'bb'","'aa'","/**/","x","/**/"));
	}
	
	protected void testTokenStream(String source, String[] expectedTokens) throws BadLocationException {
		BlockHeuristicsScannner scanner = setupSource(source);
		
		testTokenizing(expectedTokens, scanner, true, false);
		testTokenizing(expectedTokens, scanner, true, true);
		testTokenizing(expectedTokens, scanner, false, false);
		testTokenizing(expectedTokens, scanner, false, true);
	}
	
	protected void testTokenizing(String[] expectedTokens, BlockHeuristicsScannner scanner, boolean forward,
			boolean bounce)
			throws BadLocationException {
		
		FnTokenAdvance advanceTokenFn = forward ? scanner.nextTokenFn : scanner.prevTokenFn;
		
		if(forward) {
			scanner.setScanRange(0, document.getLength());
		} else {
			scanner.setScanRange(document.getLength(), 0);
		}
		for (int i = 0; i < expectedTokens.length; i++) {
			String expectedToken = expectedTokens[forward ? i : expectedTokens.length-i-1];
			int oldPos = scanner.getPosition();
			advanceTokenFn.advanceToken();
			if(bounce) {
				if(i != 0) {
					int tokenSaved = scanner.getLastToken();
					int positionSaved = scanner.getPosition();
					advanceTokenFn.revertToken();
					advanceTokenFn.advanceToken();
					assertTrue(tokenSaved == scanner.getLastToken());
					assertTrue(positionSaved == scanner.getPosition());
				}
			}
			String tokenStr = forward ? 
					document.get(oldPos, scanner.getPosition() - oldPos) : 
					document.get(scanner.getPosition(), oldPos - scanner.getPosition());
			assertEquals(tokenStr, expectedToken);
			assertEquals(scanner.getLastToken(), expectedToken.length() == 1 ? 
					expectedToken.charAt(0) : AbstractDocumentScanner.TOKEN_OUTSIDE);
		}
		assertTrue(scanner.getPosition() == (forward ? document.getLength() : 0));
		assertTrue(scanner.readNextCharacter() == AbstractDocumentScanner.TOKEN_EOF);
	}
	
	@Test
	public void testScanToBlockWithPartitions() throws Exception { testScanToBlockWithPartitions$(); }
	public void testScanToBlockWithPartitions$() throws Exception {
		document = createDocument();
		Scanner_BaseTest.setupSamplePartitioner(document);
		
		
		testScanToBlockStart("{{", "{blah/*{*/", "}", 0);
		testScanToBlockStart("}}", "('('blah", ") {}", 0);
		testScanToBlockStart("}}", "{`{`/'{'/blah", "} {}", 0);
		
		testScanToBlockStart("", "/*{*//*{*/'{'`{` blah"+NL+"foo", "} {}", 1);
		
		testScanToBlockStart("", "{{{abcdef/*{{*/}}", "}", 0);
		
		// Now some syntax errors:
		testScanToBlockStart(NEUTRAL_SRC1+"{(''", "{'''' )", "}/**/", 0);
		testScanToBlockStart(NEUTRAL_SRC1+"{(", "{ (''''", "}/**/", 0);
		
		testScanToBlockStart(NEUTRAL_SRC2+"{(", "( ''{''('' }", ")", 0);
		testScanToBlockStart(NEUTRAL_SRC2+"{", "/**/", ")", 0, LEFT_INVALID);
		testScanToBlockStart(NEUTRAL_SRC2+"{", "/**/   /**/", ")/**/", 0, LEFT_INVALID);
		testScanToBlockStart(NEUTRAL_SRC2+"''{", "      ))", ")/**/", 0, LEFT_INVALID);

		testScanToBlockStart("", "/*     */()", "}", 1);
		testScanToBlockStart("", "/*     */) (", "}", 2);
		testScanToBlockStart("(({", "''      ()''", ")", 0, LEFT_INVALID);
		testScanToBlockStart("(({", "''       ) [''", ")", 0, LEFT_INVALID);
	}
	
}
