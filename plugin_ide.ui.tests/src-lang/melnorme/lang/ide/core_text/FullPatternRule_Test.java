/*******************************************************************************
 * Copyright (c) 2011, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core_text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.array;

import melnorme.lang.ide.core_text.FullPatternRule;
import melnorme.utilbox.misc.ArrayUtil;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.junit.Test;

public class FullPatternRule_Test {
	
	private final class SampleJavaWordDetector implements IWordDetector {
		@Override
		public boolean isWordStart(char ch) {
			return false;
		}
		
		@Override
		public boolean isWordPart(char ch) {
			return Character.isJavaIdentifierPart(ch) || ch == (char) ICharacterScanner.EOF;
		}
	}
	
	public static class StringCharacterScanner implements ICharacterScanner {
		
		protected final String text;
		protected int textOffset = 0;
		
		public StringCharacterScanner(String text) {
			this.text = text;
		}
		
		@Override
		public char[][] getLegalLineDelimiters() {
			throw assertFail();
		}
		
		@Override
		public int getColumn() {
			return 0;
		}
		
		@Override
		public int read() {
			int nextChar = peekNext();
			assertTrue(textOffset <= text.length());
			++textOffset;
			return nextChar;
		}
		
		@Override
		public void unread() {
			--textOffset;
		}
		
		public final int peekNext() {
			assertTrue(textOffset >= 0);
			return textOffset < text.length() ? text.charAt(textOffset) : EOF;
		}
		
	}
	
	public static final String[] sequences = array("!in", "!i\uFFFF", "!into");
	
	@Test
	public void testBasic() throws Exception { testBasic$(); }
	public void testBasic$() throws Exception {
		String text = "blah ! !a xxx !i xxx !in zzz !ind !into xxx";
		runRuleTest(text, 
				array(text.indexOf("!in zzz"), text.indexOf("!into ")), 
				array("!in".length(), "!into".length()));
		
		
		// Test EOF and boundaries
		runRuleTest("", array(666), array(666));
		runRuleTest("!", array(666), array(666));
		runRuleTest("!i", array(666), array(666));
		runRuleTest("!in", array(0), array("!in".length()));
		runRuleTest("!into", array(0), array("!into".length()));
	}
	
	protected void runRuleTest(String text, Integer[] tokenIndexes, Integer[] tokenLen) {
		StringCharacterScanner scanner = new StringCharacterScanner(text);
		 
		FullPatternRule fpRule = new FullPatternRule(new Token(null), sequences, new SampleJavaWordDetector());
		 
		while (scanner.peekNext() != ICharacterScanner.EOF) {
			int beginOffset = scanner.textOffset;
			IToken token = fpRule.evaluate(scanner);
			if(token.isUndefined()) {
				assertTrue(scanner.textOffset == beginOffset);
				assertTrue(ArrayUtil.contains(tokenIndexes, beginOffset) == false);
				scanner.read(); // advance
			} else {
				int indexOf = ArrayUtil.indexOf(tokenIndexes, beginOffset);
				assertTrue(indexOf != -1);
				assertTrue(scanner.textOffset == beginOffset + tokenLen[indexOf]);
			}
		}
	}
	
	
}
