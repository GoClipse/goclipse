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


public class CommonLexingRule {
	
	public CommonLexingRule() {
		super();
	}
	
	public static boolean consume(ICharacterReader reader, int character) {
		return reader.consume(character);
	}
	
	public static boolean consumeAnyExceptNullOr(ICharacterReader reader, int character) {
		int ch = reader.lookahead();
		if(ch != character && ch != 0) {
			reader.read();
			return true;
		}
		return false;
	}
	
	public static boolean consumeHexDigit(ICharacterReader reader) {
		int la = reader.lookahead();
		
		if(isHexDigit(la)) {
			reader.read();
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