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


import org.junit.Test;

import melnorme.lang.tooling.parser.lexer.IPredicateLexingRule;
import melnorme.lang.tooling.parser.lexer.NumberLexingRule;
import melnorme.lang.utils.parse.ICharacterReader;

public class NumberRuleTest extends CommonLexerRuleTest {
	
	public NumberRuleTest() {
		super();
	}
	
	@Override
	protected IPredicateLexingRule createLexingRule() {
		return new NumberLexingRule() {
			@Override
			protected boolean consumeIntSuffix(ICharacterReader reader) {
				return false;
			}
			
			@Override
			protected boolean consumeFloatSuffix(ICharacterReader reader) {
				return false;
			}
		};
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		testInteger();
		
		testFloats();
	}
	
	protected void testInteger() {
		testRule("", 0);
		testRule("xxx", 0);
		testRule("xxx123", 0);
		
		testRule("0", 1);
		testRule("2", 1);
		testRule("123", 3);
		testRule("12_3_", 5);
		testRule("10_20_30", 8);
		testRule("_", 0);
		
		testRule("123" + (char) ('9' + 1), 3);
		testRule("123" + (char) ('0' - 1), 3);
		
		// parse even the illegal suffixes
		testRule("123xxx", 3);
		testRule("123.4xxx", 5);
		
		
		testRule("0b_010", 6);
		testRule("0o7_17", 6);
		testRule("0xF1F_", 6);
		
		testRule("0b012", 4);
		testRule("0o718", 4);
		testRule("0xF1G", 4);
	}
	
	protected void testFloats() {
		// Floats
		testRule("123.0", 5);
		testRule("123.19", 6);
		testRule("123.", 4);
		testRule("123. a", 4);
		testRule("123.a", 3);
		

		testRule("123..", 3);
		testRule("123,", 3);
		
		testFractionalPartIfLiteralHasRadixPrefix();
		
		testFloat_exponentPart();
	}
	
	protected void testFractionalPartIfLiteralHasRadixPrefix() {
		testRule("0o0.11", 3);
		testRule("0b0.11", 3);
		testRule("0x0.11", 3);
	}
	
	protected void testFloat_exponentPart() {
		testRule("123.15E", 7);
		testRule("123.15e109", 10);
		testRule("123.15E+19", 10);
		testRule("123.15E-19", 10);
		
		testRule("123.E1", 3); // fractional part can't start with exponent
		testRule("123._E1", 3); // fractional part can't start with _
		testRule("123.1_E1", 8);
	}
	
}