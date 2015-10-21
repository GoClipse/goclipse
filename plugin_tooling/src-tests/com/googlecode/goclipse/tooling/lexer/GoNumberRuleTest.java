/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *     Konstantin Salikhov - test cases for lexing rule
 *******************************************************************************/
package com.googlecode.goclipse.tooling.lexer;


import melnorme.lang.tests.NumberRuleTest;
import melnorme.lang.tooling.parser.lexer.ILexingRule;

public class GoNumberRuleTest extends NumberRuleTest {
	
	@Override
	protected ILexingRule createLexingRule() {
		return new GoNumberLexingRule();
	}
	
	@Override
	public void testRule(String source, int expectedTokenLength) {
		if(source.equals("_") || source.equals("123._E1")) {
			
		} else {
			source = source.replace('_', '0');
		}
		super.testRule(source, expectedTokenLength);
	}
	
	@Override
	protected void testInteger() {
		super.testInteger();
		
		testRule("12i", 3);
	}
	
	@Override
	protected void testFloats() {
		super.testFloats();
		
		testRule("123.1i", 6);
	}
	
}