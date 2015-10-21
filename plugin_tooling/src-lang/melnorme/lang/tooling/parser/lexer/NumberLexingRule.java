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


import melnorme.lang.utils.parse.ICharacterReader;
import melnorme.utilbox.misc.NumberUtil;

public abstract class NumberLexingRule implements ILexingRule {
	
	public NumberLexingRule() {
		super();
	}

	@Override
	public boolean doEvaluate(ICharacterReader reader) {
		
		int radix = 10;
		
		if(reader.tryConsume('0')) {
			if(reader.tryConsume('b')) {
				radix = 2;
			} 
			else if(reader.tryConsume('o')) {
				radix = 8;
			}
			else if(reader.tryConsume('x')) {
				radix = 16;
			}
		} else {
			if(!consumeDigit(reader, radix)) {
				return false;
			}
		}
		
		consumeDigits(reader, radix, true);
		
		if (consumeIntSuffix(reader)) {
			return true;
		}
		
		tryConsumeFractionalPart(reader, radix);
		
		return true;
	}
	
	protected boolean consumeDigits(ICharacterReader reader, int radix, boolean allowUnderScore) {
		boolean consumedAny = false;
		while(consumeDigit(reader, radix) || (allowUnderScore && reader.tryConsume('_'))) {
			consumedAny = true; 
		}
		return consumedAny;
	}
	
	protected abstract boolean consumeIntSuffix(ICharacterReader reader);
	
	protected void tryConsumeFractionalPart(ICharacterReader reader, int radix) {
		boolean hasPrefix = radix != 10;
		
		if(!hasPrefix) {
			consumeFractionalPart(reader, radix);
		}
	}
	
	protected void consumeFractionalPart(ICharacterReader reader, int radix) {
		if(reader.lookahead() != '.' || !isAllowedAfterFractionDot(reader.lookahead(1))) {
			return;
		}
		
		reader.consume(); // Consume dot
		
		boolean hasFractionalDigits = tryconsumeFractionalPart(reader, radix);
		
		if(hasFractionalDigits && (reader.tryConsume('e') || reader.tryConsume('E'))) {
			if(reader.tryConsume('+') || reader.tryConsume('-')) {
			}
			
			consumeDigits(reader, 10, true);
		}
		
		consumeFloatSuffix(reader);
	}
	
	protected boolean isAllowedAfterFractionDot(int afterDot) {
		return afterDot == -1 || !(afterDot == '.' || afterDot == '_' || Character.isLetter(afterDot));
	}
	
	protected boolean tryconsumeFractionalPart(ICharacterReader reader, int radix) {
		
		if(consumeDigit(reader, radix)) {
			consumeDigits(reader, radix, true);
			return true;
		}
		return false;
	}
	
	protected static boolean consumeDigit(ICharacterReader reader, int radix) {
		if(reader.hasCharAhead() && NumberUtil.isDigit(reader.lookaheadChar(), radix)) {
			reader.consume();
			return true;
		}
		return false;
	}
	
	protected abstract boolean consumeFloatSuffix(ICharacterReader reader);
	
}