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
package melnorme.lang.tooling.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.tooling.parser.TextBlocksReader.TextBlocksSubReader;
import melnorme.lang.tooling.parser.TextBlocksReader.TokenKind;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTestUtils;

public class TextBlocksReader_Test extends CommonTestUtils {
	
	protected TextBlocksReader createReader(String source) {
		return new TextBlocksReader(new StringCharSource(str(source)));
	}
	
	public static String str(String str) {
		return str.replace("|", "\"");
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		assertEquals(createReader(" blah").consumeText(), "blah");
		assertEquals(createReader(" ||").consumeText(), "");
		assertEquals(createReader(" ||||").consumeText(), "");
		assertEquals(createReader(" |\\\\|").consumeText(), "\\");
		
		assertEquals(createReader(" ||blah").consumeText(), "blah");
		
		final String seqA = "blah 1.0 |--\\|--| aa.-=+\"xx\\\"xx\"cc";
		
		testReaderBasicSequenceA(createReader(seqA));
		
		{
			TextBlocksReader reader = createReader(" {blah}{blah|}|}{xxx{ccc}}");
			try(TextBlocksSubReader subReader = reader.enterBlock()) {
				assertEquals(subReader.consumeText(), "blah");
			}
			assertTrue(reader.charSource.lookaheadChar() == '{');
			try(TextBlocksSubReader subReader = reader.enterBlock()) {
				assertEquals(subReader.consumeText(), "blah}");
			}
			try(TextBlocksSubReader subReader = reader.enterBlock()) {
				assertEquals(subReader.consumeText(), "xxx");
				try(TextBlocksSubReader subReader2 = reader.enterBlock()) {
					assertEquals(subReader2.consumeText(), "ccc");
				}
			}
		}
		
		{
			TextBlocksReader reader = createReader(" {" +seqA+ "} " + seqA + " { " +seqA+ " } ");
			reader.consumeBlock((subReader) -> testReaderBasicSequenceA(subReader));
			testReaderBasicSequenceA(reader);
			reader.consumeBlock((subReader) -> testReaderBasicSequenceA(subReader));
			assertTrue(reader.tokenAhead() == TokenKind.EOS);
		}
		
		{
			TextBlocksReader reader = createReader(" asfd|}| aaa {} bbb {" +seqA+ " { axdfxd } } ccc");
			reader.skipNextElement();
			assertEquals(reader.consumeText(), "aaa");
			reader.skipNextElement();
			assertEquals(reader.consumeText(), "bbb");
			reader.skipNextElement();
			assertEquals(reader.consumeText(), "ccc");
		}
		
		testErrorCases();
	}
	
	protected Void testReaderBasicSequenceA(TextBlocksReader reader) throws CommonException {
		assertTrue(reader.peekTokenStart() == 'b');
		reader.expectText("blah");
		reader.expectText("1.0");
		reader.expectText("--\"--");
		reader.expectText("aa.-=+xx\"xxcc");
		
		assertTrue(createReader("").tokenAhead() == TokenKind.EOS);
		assertTrue(createReader("  ").tokenAhead() == TokenKind.EOS);
		assertTrue(createReader(" }").tokenAhead() == TokenKind.EOS);
		return null;
	}
	
	protected void testErrorCases() {
		verifyThrows(() -> createReader("  {").consumeText(), null, 
				"Expected text, found `{`.");
		verifyThrows(() -> createReader("  \"abc").consumeText(), null, 
				"Unterminated text `abc`.");
		
		verifyThrows(() -> createReader("  blah").expectText("xxx"), null, 
			"Expected text `xxx`, found text `blah`.");
		verifyThrows(() -> createReader("  ( )").expectText("xxx"), null, 
			"Expected text `xxx`, found `(`.");
		
		
		verifyThrows(() -> {
			try(TextBlocksSubReader subReader = createReader(" (  ").enterBlock()) {
				throw new CommonException("foo");
			}
		}, null, "foo");
		
		verifyThrows(() -> {
			try(TextBlocksSubReader subReader = createReader(" { ) ").enterBlock()) {
			}
		}, null, "Expected BLOCK_CLOSE `}`, found `)`");
		
		verifyThrows(() -> {
			try(TextBlocksSubReader subReader = createReader(" {  ").enterBlock()) {
			}
		}, null, "Expected BLOCK_CLOSE `}`, found EOS");
	}
	
}