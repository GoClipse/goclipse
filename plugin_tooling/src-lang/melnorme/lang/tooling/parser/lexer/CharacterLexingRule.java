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
import melnorme.lang.utils.parse.LexingUtils;

public class CharacterLexingRule extends LexingUtils implements IPredicateLexingRule {
	
	@Override
	public boolean doEvaluate(ICharacterReader reader) {
		if(!consumeStart(reader)) {
			return false;
		}
		if(!consumeBody(reader)) {
			return false;
		}
		if(!consumeEnd(reader)) {
			return false;
		}
		return true;
	}
	
	protected boolean consumeStart(ICharacterReader reader) {
		return reader.tryConsume('\'');
	}
	
	protected boolean consumeBody(ICharacterReader reader) {
		if(reader.tryConsume((char) 0x5c)) {
			return reader.tryConsume('\'') || reader.tryConsume('\"') ||
				   consumeCommonEscape(reader) || consumeUnicodeEscapeSequence(reader);
		};
		
		if(reader.lookaheadIsEOS() || reader.lookahead() == '\'') {
			return false;
		}
		if(reader.lookahead() == 0) {
			return false; // Should the null character really not be allowed?
		}
		return reader.consumeAny();
	}
	
	protected boolean consumeEnd(ICharacterReader reader) {
		return reader.tryConsume('\'');
	}
	
	protected static char[] COMMON_ESCAPES = { 0x5c, 'n' , 'r' , 't' , '0' }; 
	
	protected boolean consumeCommonEscape(ICharacterReader reader) {
		for(char ch : COMMON_ESCAPES) {
			if(reader.tryConsume(ch)) {
				return true;
			}
		}
		
		if(consumeHexEscapeSequence(reader)) {
			return true;
		}
		return false;
	}
	
	protected boolean consumeHexEscapeSequence(ICharacterReader reader) {
		if(reader.lookahead(0) == 'x' 
				&& isHexDigit(reader.lookahead(1)) && isHexDigit(reader.lookahead(2))) {
			reader.consumeAmount(3);
			return true;
		}
		return false;
	}
	
	protected boolean consumeUnicodeEscapeSequence(ICharacterReader reader) {
		if(reader.tryConsume('u')) {
			
			while(true) {
				int la = reader.lookahead();
				
				// This is not accurate to any spec, but is good enough.
				if(la == '{' || la == '}' || isHexDigit(la)) {
					reader.consume();
				} else {
					break;
				}
			}
			
			return true;
		}
		return false;
	}
	
	public static boolean isHexDigit(int ch) {
		if(ch >= '0' && ch <= '9') {
			return true;
		}
		if((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
			return true;
		}
		return false;
	}
	
}