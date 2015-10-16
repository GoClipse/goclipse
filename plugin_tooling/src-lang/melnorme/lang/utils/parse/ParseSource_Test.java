/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import org.junit.Test;

import melnorme.lang.tests.CommonToolingTest;
import melnorme.lang.tooling.parser.lexer.CharacterReader_SubReader;
import melnorme.lang.tooling.parser.lexer.LexingUtils;
import melnorme.utilbox.core.Assert.AssertFailedException;

public abstract class ParseSource_Test extends CommonToolingTest {
	
	protected final String TEST_SOURCE = "abcdef";
	
	protected ICharacterReader parseSource;
	protected String source;
	protected int sourceIx;
	protected int lookahead;
	
	protected void init(String source) {
		this.source = source;
		this.parseSource = createParseSource(source);
		this.sourceIx = 0;
	}
	
	protected abstract ICharacterReader createParseSource(String source);
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		doTest______();
	}
	
	protected void doTest______() throws Exception {
		init(TEST_SOURCE);
		testLookahead('a');
		
		assertTrue(parseSource.lookaheadMatches("abc", 0));
		assertTrue(parseSource.lookaheadMatches("bc", 0) == false);
		assertTrue(parseSource.lookaheadMatches("bc", 1));
		assertTrue(parseSource.lookaheadMatches("abc", TEST_SOURCE.length()) == false);
		assertTrue(parseSource.lookaheadMatches("", TEST_SOURCE.length()));
		
		assertTrue(parseSource.lookaheadString(0, 6).equals(TEST_SOURCE));
		parseSource.consume(2);
		assertTrue(parseSource.lookaheadString(0, 4).equals("cdef"));
		
		init(TEST_SOURCE);
		testCharSource();
		
		init(TEST_SOURCE);
		assertTrue(parseSource.tryConsume("abc"));
		assertTrue(parseSource.lookaheadMatches("def"));
		
		init(TEST_SOURCE);
		assertTrue(parseSource.stringUntil("z").equals("abcdef"));
		assertTrue(parseSource.stringUntil("a").equals(""));
		assertTrue(parseSource.stringUntil("def").equals("abc"));
		parseSource.consumeAhead("abc");
		assertTrue(parseSource.stringUntil("def").equals(""));
		assertTrue(parseSource.stringUntil("z").equals("def"));
		
		init(TEST_SOURCE);
		assertTrue(parseSource.consumeUntil("def").equals("abc"));
		assertTrue(parseSource.lookaheadMatches("def"));
		
		init(TEST_SOURCE);
		assertTrue(parseSource.consumeUntil("de", true).equals("abc"));
		assertTrue(parseSource.lookahead() == 'f');
		assertTrue(parseSource.lookaheadMatches("f"));
	}
	
	protected void testCharSource() throws Exception {
		
		// Test lookahead
		checkBufferedCount(parseSource, 0);
		lookahead = testLookahead(source.charAt(sourceIx));
		
		// Test lookahead(1)
		assertTrue(parseSource.lookahead(1) == source.charAt(sourceIx + 1));
		
		// Test consume with buffered
		checkBufferedCount(parseSource, 2);
		assertTrue(parseSource.consume2() == lookahead); sourceIx++;
		checkBufferedCount(parseSource, 1);
		assertTrue(parseSource.consume2() == source.charAt(sourceIx)); sourceIx++;
		checkBufferedCount(parseSource, 0);
		
		
		checkBufferedCount(parseSource, 0);
		lookahead = testLookahead(source.charAt(sourceIx));
		// Test consume with buffered
		checkBufferedCount(parseSource, 1);
		assertTrue(lookahead == parseSource.consume2()); sourceIx++;
		
		assertTrue(lookahead == 'c');
		
		
		checkBufferedCount(parseSource, 0);
		assertTrue(parseSource.consume2() == source.charAt(sourceIx));
		sourceIx++;

		while(sourceIx < source.length()) {
			int ch = testLookahead(source.charAt(sourceIx));
			assertTrue(parseSource.consume2() == ch);
			sourceIx++;
		}
		
		// EOF
		testLookahead(-1);
		verifyThrows(() -> parseSource.consume2(), AssertFailedException.class);
	}

	protected void checkBufferedCount(ICharSource<?> parseSource, int expected) {
		assertTrue(parseSource.bufferedCharCount() == expected);
	}
	
	protected int testLookahead(int expected) throws Exception {
		return testLookAhead(parseSource, expected);
	}
	
	public static int testLookAhead(ICharSource<?> parseSource, int expected) throws Exception {
		int lookahead = parseSource.lookahead();
		assertTrue(lookahead == expected);
		assertTrue(lookahead == parseSource.lookahead(0));
		return lookahead;
	}
	
	@Test
	public void test_consumeDelimited() throws Exception { test_consumeDelimited$(); }
	public void test_consumeDelimited$() throws Exception {
		
		init("blah");
		testConsumeDelimitedString(parseSource, '|', '#', "blah");
		
		init("one|two|three##|four#|xxx|###|five");
		testConsumeDelimitedString(parseSource, '|', '#', "one");
		testConsumeDelimitedString(parseSource, '|', '#', "two");
		testConsumeDelimitedString(parseSource, '|', '#', "three#");
		testConsumeDelimitedString(parseSource, '|', '#', "four|xxx");
		testConsumeDelimitedString(parseSource, '|', '#', "#|five");
	}
	
	protected void testConsumeDelimitedString(ICharacterReader parseSource, char delimiter, char escapeChar, 
			String expected) throws Exception {
		
		CharacterReader_SubReader subReader = new CharacterReader_SubReader(parseSource);
		CharacterReader_SubReader subReaderAlt2 = new CharacterReader_SubReader(parseSource);
		
		assertEquals(LexingUtils.consumeUntilDelimiter(subReader, delimiter, escapeChar), expected);
		
		// ensure advanceDelimitedString reads same number of strings;
		LexingUtils.advanceDelimitedString(subReaderAlt2, delimiter, escapeChar);
		assertTrue(subReader.readOffset == subReaderAlt2.readOffset);
		
		assertEquals(LexingUtils.consumeUntilDelimiter(parseSource, delimiter, escapeChar), expected);
	}
	
	@Test
	public void testConsumeNewline() throws Exception { testConsumeNewline$(); }
	public void testConsumeNewline$() throws Exception {
		init("abc\ndef\r\nzzz");
		assertAreEqual(LexingUtils.stringUntilNewline(parseSource, 0), "abc");
		assertAreEqual(LexingUtils.stringUntilNewline(parseSource, 1), "bc");
		assertAreEqual(LexingUtils.stringUntilNewline(parseSource, 3), "");
		assertAreEqual(LexingUtils.consumeLine(parseSource), "abc");
		assertAreEqual(LexingUtils.consumeLine(parseSource), "def");
		assertAreEqual(LexingUtils.consumeLine(parseSource), "zzz");
		assertAreEqual(LexingUtils.consumeLine(parseSource), null);
	}
	
	
	/* ----------------- Actual tests ----------------- */
	
	public static class StringParseSource_Test extends ParseSource_Test {
		@Override
		protected ICharacterReader createParseSource(String source) {
			return new StringParseSource(source);
		}
		
		@Override
		protected void checkBufferedCount(ICharSource<?> parseSource, int expected) {
			assertTrue(parseSource.bufferedCharCount() == source.length() - sourceIx);
		}
	}
	
}