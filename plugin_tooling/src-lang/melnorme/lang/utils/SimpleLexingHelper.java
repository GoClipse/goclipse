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
package melnorme.lang.utils;

import melnorme.lang.tooling.parser.lexer.StringCharacterReader;

public class SimpleLexingHelper extends StringCharacterReader {
	
	public SimpleLexingHelper(String source) {
		super(source);
	}
	
	public final boolean tryConsume(String string) {
		if(source.startsWith(string, offset)) {
			offset += string.length();
			return true;
		}
		return false;
	}
	
	/**
	 * Consume a string delimited by give delimiter char, 
	 * with given escapeChar acting a possible escape (use -1 for no escapeChar)
	 */
	public String consumeDelimitedString(int delimiter, int escapeChar) {
		StringBuilder sb = new StringBuilder();
		
		int start = getOffset();
		
		while(true) {
			int consumedChar = read();
			
			if(consumedChar == -1 || consumedChar == delimiter) {
				sb.append(getSource().subSequence(start, getOffset()-1));
				break;
			}
			else if(consumedChar == escapeChar) {
				if(lookahead() == delimiter || lookahead() == escapeChar) {
					
					sb.append(getSource().subSequence(start, getOffset()-1));
					
					int escapedChar = read();
					sb.append((char) escapedChar);
					start = getOffset();
				}
			}
			
		}
		return sb.toString();
	}
	
	public String consumeUntil(String string) {
		int startPos = getOffset();
		
		while(!lookaheadIsEOF()) {
			
			if(source.startsWith(string, getOffset())) {
				break;
			}
			read();
		}
		String token = source.substring(startPos, getOffset());
		return token;
	}
	
}