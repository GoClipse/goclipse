/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.parser.lexer;


public class CharacterLexingRule extends CommonLexingRule implements ILexingRule {
	
	@Override
	public boolean evaluate(ICharacterReader reader) {
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
		return reader.consume('\'');
	}
	
	protected boolean consumeBody(ICharacterReader reader) {
		if(consume(reader, 0x5c)) {
			return consume(reader, '\'') || consumeCommonEscape(reader) || consumeUnicodeEscapeSequence(reader);
		};
		
		return consumeAnyExceptNullOr(reader, '\'');
	}
	
	protected boolean consumeEnd(ICharacterReader reader) {
		return reader.read() == '\'';
	}
	
	protected static char[] COMMON_ESCAPES = { 0x5c, 'n' , 'r' , 't' , '0' }; 
	
	protected boolean consumeCommonEscape(ICharacterReader reader) {
		for(char ch : COMMON_ESCAPES) {
			if(consume(reader, ch)) {
				return true;
			}
		}
		
		if(consumeHexEscapeSequence(reader)) {
			return true;
		}
		return false;
	}
	
	protected boolean consumeHexEscapeSequence(ICharacterReader reader) {
		if(consume(reader, 'x')) {
			if(consumeHexDigit(reader)) {
				if(consumeHexDigit(reader)) {
					return true;
				}
				reader.unread();
			}
			reader.unread();
		}
		return false;
	}
	
	protected boolean consumeUnicodeEscapeSequence(ICharacterReader reader) {
		if(consume(reader, 'u')) {
			
			while(true) {
				int la = reader.lookahead();
				
				// This is not accurate to any spec, but is good enough.
				if(!(la == '{' || la == '}' || isHexDigit(la))) {
					break;
				}
				reader.read();
			}
			
			return true;
		}
		return false;
	}
	
}