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
package melnorme.lang.utils.parse;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import melnorme.lang.tests.CommonToolingTest;

import org.junit.Test;

public class ParseSource_Test extends CommonToolingTest {
	
	protected final String TEST_SOURCE = "abcdef";
	
	protected String source;
	protected int sourceIx;
	protected int lookahead;

	
	protected void doTest______(String source, IParseSource parseSource) throws IOException {
		
		this.source = source;
		this.sourceIx = 0;
		
		// Test lookahead
		checkBufferedCount(parseSource, 0);
		lookahead = testLookahead(parseSource, source.charAt(sourceIx));
		
		// Test lookahead(1)
		assertTrue(parseSource.lookahead(1) == source.charAt(sourceIx + 1));
		
		// Test consume with buffered
		checkBufferedCount(parseSource, 2);
		assertTrue(parseSource.consume() == lookahead); sourceIx++;
		checkBufferedCount(parseSource, 1);
		assertTrue(parseSource.consume() == source.charAt(sourceIx)); sourceIx++;
		checkBufferedCount(parseSource, 0);
		
		
		checkBufferedCount(parseSource, 0);
		lookahead = testLookahead(parseSource, source.charAt(sourceIx));
		// Test consume with buffered
		checkBufferedCount(parseSource, 1);
		assertTrue(lookahead == parseSource.consume()); sourceIx++;
		
		assertTrue(lookahead == 'c');
		
		
		checkBufferedCount(parseSource, 0);
		assertTrue(parseSource.consume() == source.charAt(sourceIx));
		sourceIx++;

		while(sourceIx < source.length()) {
			int ch = testLookahead(parseSource, source.charAt(sourceIx));
			assertTrue(parseSource.consume() == ch);
			sourceIx++;
		}
		
		// EOF
		testLookahead(parseSource, -1);
		assertTrue(parseSource.consume() == -1);
	}

	protected void checkBufferedCount(IParseSource parseSource, int expected) {
		assertTrue(parseSource.bufferedCharCount() == expected);
	}
	
	protected int testLookahead(IParseSource parseSource, int expected) throws IOException {
		int lookahead = parseSource.lookahead();
		assertTrue(lookahead == expected);
		assertTrue(lookahead == parseSource.lookahead(0));
		return lookahead;
	}
	
	public static class ReaderParseSource_Test extends ParseSource_Test {
		
		@Test
		public void test() throws Exception { test$(); }
		public void test$() throws Exception {
			doTest______(TEST_SOURCE, new ReaderParseSource(new StringReader(TEST_SOURCE)));
		}
	}
	
	public static class StringParseSource_Test extends ParseSource_Test {
		
		@Test
		public void test() throws Exception { test$(); }
		public void test$() throws Exception {
			doTest______(TEST_SOURCE, new StringParseSource(TEST_SOURCE));
		}
		
		@Override
		protected void checkBufferedCount(IParseSource parseSource, int expected) {
			assertTrue(parseSource.bufferedCharCount() == source.length() - sourceIx);
		}
	}
	
}