/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Konstantin Salikhov - number lexing rule implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.lexer;

import melnorme.lang.tooling.parser.lexer.NumberLexingRule;
import melnorme.lang.utils.parse.ICharacterReader;

public class GoNumberLexingRule extends NumberLexingRule {
	
	@Override
	protected boolean consumeDigits(ICharacterReader reader, int radix, boolean allowUnderScore) {
		// No underscore
		return super.consumeDigits(reader, radix, false);
	}
	
	@Override
	protected boolean consumeIntSuffix(ICharacterReader reader) {
		return reader.tryConsume("i");
	}
	
	@Override
	protected boolean consumeFloatSuffix(ICharacterReader reader) {
		return reader.tryConsume("i");
	}
	
}