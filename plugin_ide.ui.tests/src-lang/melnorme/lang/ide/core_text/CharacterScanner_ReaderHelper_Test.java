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
package melnorme.lang.ide.core_text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import melnorme.lang.ide.core_text.BufferedRuleBasedScannerExt;
import melnorme.lang.ide.core_text.CharacterScanner_ReaderHelper;
import melnorme.lang.utils.parse.ICharSource;
import melnorme.lang.utils.parse.ICharacterReader;
import melnorme.lang.utils.parse.ParseSource_Test;

public class CharacterScanner_ReaderHelper_Test {
	
	public static class ReaderHelper_ParseSource_Test extends ParseSource_Test {
		@Override
		protected ICharacterReader createParseSource(String source) {
			BufferedRuleBasedScannerExt charScanner = new BufferedRuleBasedScannerExt();
			charScanner.setRange(new Document(source), 0, source.length());
			return new CharacterScanner_ReaderHelper(charScanner); 
		}
		
		@Override
		protected void checkBufferedCount(ICharSource<?> parseSource, int expected) {
			// Ignore
		}
	}
	
	@Test
	public void testBasic() throws Exception { testBasic$(); }
	public void testBasic$() throws Exception {
		BufferedRuleBasedScannerExt charScanner = new BufferedRuleBasedScannerExt();
		charScanner.setRange(new Document("abcd___"), 0, 4);
		
		CharacterScanner_ReaderHelper reader;
		reader = new CharacterScanner_ReaderHelper(charScanner);
		
		assertTrue(charScanner.getOffset() == 0);
		assertTrue(charScanner.lookahead(0) == 'a');
		assertTrue(charScanner.lookaheadString(0, 0).equals(""));
		assertTrue(charScanner.lookaheadString(0, 2).equals("ab"));
		assertTrue(charScanner.lookaheadString(1, 2).equals("bc"));
		assertTrue(charScanner.lookaheadString(4, 0).equals(""));
		assertTrue(reader.lookahead() == 'a');
		
		reader.consume();
		assertTrue(charScanner.getOffset() == 1);
		assertTrue(charScanner.lookahead(0) == 'b');
		assertTrue(charScanner.lookaheadString(0, 0).equals(""));
		assertTrue(charScanner.lookaheadString(0, 2).equals("bc"));
		assertTrue(charScanner.lookaheadString(1, 2).equals("cd"));
		assertTrue(charScanner.lookaheadString(3, 0).equals(""));
		
		reader.consume();
		reader.consume();
		assertTrue(charScanner.getOffset() == 3);
		assertTrue(charScanner.lookahead(0) == 'd');
		assertTrue(reader.lookahead() == 'd');
		
		charScanner.setRange(new Document("abcd___"), 0, 4);
		assertTrue(charScanner.getOffset() == 0);
		assertTrue(charScanner.lookahead(0) == 'a');
		
		reader = new CharacterScanner_ReaderHelper(charScanner);
		assertTrue(charScanner.lookahead(0) == 'a');
		assertTrue(reader.consume() == 'a');
		assertTrue(charScanner.lookahead(0) == 'b');
		assertTrue(charScanner.getOffset() == 1);
		assertTrue(reader.lookahead(1) == 'c');
		
		/* -----------------  ----------------- */
		
		charScanner.setBufferSize(2);
		charScanner.setRange(new Document("abcd___"), 0, 4);
		
		reader = new CharacterScanner_ReaderHelper(charScanner);
		assertTrue(charScanner.lookahead(2) == 'c');
		assertTrue(charScanner.lookahead(0) == 'a');
		
	}
	
}