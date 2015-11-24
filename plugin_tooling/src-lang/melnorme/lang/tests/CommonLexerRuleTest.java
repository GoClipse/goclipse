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
package melnorme.lang.tests;


import melnorme.lang.tooling.parser.lexer.IPredicateLexingRule;
import melnorme.lang.utils.parse.StringParseSource;

public abstract class CommonLexerRuleTest extends CommonToolingTest {
	
	public CommonLexerRuleTest() {
		super();
	}
	
	public void testRule(String source) {
		testRule(source, true);
	}
	
	public void testRule(String source, boolean terminatedSuccessfuly) {
		testRule(source, source.length());
		if(terminatedSuccessfuly) {
			// if rule terminated successfully, adding a suffix will make no diference to token size
			testRule(source + getRuleNeutralSuffix(), source.length());
		}
	}
	
	protected String getRuleNeutralSuffix() {
		return "xxxx";
	}
	
	public void testRule(String source, int expectedTokenLength) {
		testRule(createLexingRule(), source, expectedTokenLength);
	}
	
	protected abstract IPredicateLexingRule createLexingRule();
	
	public static void testRule(IPredicateLexingRule lexRule, String source, int expectedTokenLength) {
		StringParseSource reader = new StringParseSource(source);
		boolean isMatch = lexRule.tryMatch(reader);

		assertEquals(isMatch, expectedTokenLength > 0);
		if(isMatch) {
			assertEquals(reader.getReadPosition(), expectedTokenLength);
		}
	}
	
}