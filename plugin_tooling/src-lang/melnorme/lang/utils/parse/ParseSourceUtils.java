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
package melnorme.lang.utils.parse;

import java.io.IOException;


public interface ParseSourceUtils extends IParseSource {
	
	default boolean lookaheadIsEOF() throws IOException {
		return !hasCharAhead();
	}
	
	default boolean tryConsume(String string) throws IOException {
		if(lookaheadMatches(string)) {
			consume(string.length());
			return true;
		}
		return false;
	}
	
	default String consumeUntil(String string) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		while(hasCharAhead() && !lookaheadMatches(string)) {
			sb.append(consumeNonEOF());
		}
		return sb.toString();
	}
	
	default String consumeUntil(String endString, boolean consumeEndString) throws IOException {
		String firstString = consumeUntil(endString);
		if(consumeEndString) {
			tryConsume(endString);
		}
		return firstString;
	}
	
	/**
	 * Consume a string delimited by give delimiter char, 
	 * with given escapeChar acting a possible escape (use -1 for no escapeChar)
	 */
	default String consumeDelimitedString(int delimiter, int escapeChar) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		while(hasCharAhead()) {
			
			char consumedChar = consumeNonEOF();
			
			if(consumedChar == delimiter) {
				break;
			}
			else if(consumedChar == escapeChar && hasCharAhead()) {
				int lookahead = lookahead();
				if(lookahead == delimiter || lookahead == escapeChar) {
					
					char escapedChar = consumeNonEOF();
					sb.append(escapedChar);
					continue;
				}
			}
			
			sb.append(consumedChar);
			
		}
		return sb.toString();
	}
	
}