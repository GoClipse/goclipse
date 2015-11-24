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
package melnorme.lang.tooling.parser.lexer;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.utils.parse.StringParseSource;

public class CharacterLexingRule_Test {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		CharacterLexingRule rule = new CharacterLexingRule();
		
		testRule(rule, "'a'", 3);
		
		testRule(rule, "'", 0);
		testRule(rule, "''", 0);
		testRule(rule, "'''", 0);
		testRule(rule, "'a", 0);
		testRule(rule, "'ab'", 0);
		
		testRule(rule, "'\\''", 4);
		testRule(rule, "'\\\\'", 4);
		testRule(rule, "'\\t'", 4);
		testRule(rule, "'\\%'", 0);
		
		testRule(rule, "'\\xA0'", 6);
		testRule(rule, "'\\x1b'", 6);
		testRule(rule, "'\\x1g'", 0);
		testRule(rule, "'\\xg0'", 0);
		
		testRule(rule, "'\\xu'", 0);
		
		testRule(rule, "'\\uA0'", 6);
		testRule(rule, "'\\u'", 4);
		testRule(rule, "'\\u '", 0);
		testRule(rule, "'\\u\\n'", 0);
		testRule(rule, "'\\u", 0);
	}
	
	protected void testRule(CharacterLexingRule rule, String source, int expectedMatchLength) {
		StringParseSource reader = new StringParseSource(source);
		boolean expectedMatches = expectedMatchLength > 0;
		
		assertTrue(rule.tryMatch(reader) == expectedMatches);
		if(expectedMatches) {
			assertTrue(reader.getReadPosition() == expectedMatchLength);
		}
	}
	
}
