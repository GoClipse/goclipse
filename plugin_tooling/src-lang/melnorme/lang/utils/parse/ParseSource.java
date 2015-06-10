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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;


/**
 * Utility methods for an ICharSource, oriented to parsing a char sequence.
 */
public interface ParseSource<EXC extends Exception> extends ICharSource<EXC> {
	
	default boolean lookaheadIsEOF() throws EXC {
		return !hasCharAhead();
	}
	
	default void consumeAhead(String string) throws EXC {
		assertNotNull(string);
		assertTrue(lookaheadMatches(string));
		consume(string.length());
	}
	
	default boolean tryConsume(String string) throws EXC {
		if(lookaheadMatches(string)) {
			consume(string.length());
			return true;
		}
		return false;
	}
	
	default String stringUntil(String string) throws EXC {
		StringBuilder sb = new StringBuilder();
		
		int ix = 0;
		while(true) {
			int charAtIx = lookahead(ix);
			if(charAtIx == -1 || lookaheadMatches(string, ix)) {
				break;
			}
			sb.append((char) charAtIx);
			ix++;
		}
		return sb.toString();
	}
	
	default String consumeUntil(String string) throws EXC {
		String stringUntil = stringUntil(string);
		consume(stringUntil.length());
		return stringUntil;
	}
	
	/**
	 * Consume characters until lookhead position matches given endString
	 * If consumeEndString, also consume endString from source (although this is not included in result)
	 * 
	 * @return the consumed characters until endString 
	 */
	default String consumeUntil(String endString, boolean consumeEndString) throws EXC {
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
	default String consumeDelimitedString(int delimiter, int escapeChar) throws EXC {
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
	
	/* -----------------  Line helpers  ----------------- */
	
	default String determineNewlineSequenceAt(int offset) throws EXC {
		
		int la = lookahead(offset);
			
		if(la == -1) {
			return "";
		}
		if(la == '\n') {
			return "\n";
		}
		if(la == '\r') {
			if(lookahead(offset + 1) == '\n') {
				return "\r\n";
			}
			return "\r";
		}
		
		return null;
	}
	
	default String stringUntilNewline() throws EXC {
		return stringUntilNewline(0);
	}
	
	default String stringUntilNewline(int offset) throws EXC {
		StringBuilder sb = new StringBuilder();
		
		int ix = offset;
		while(true) {
			if(determineNewlineSequenceAt(ix) != null) {
				break;
			}
			sb.append((char) lookahead(ix));
			ix++;
		}
		return sb.toString();
	}
	
	default String consumeLine() throws EXC {
		int offset = 0;
		
		if(lookaheadIsEOF()) {
			return null;
		}
		String line = stringUntilNewline(offset);
		consumeAhead(line);
		consumeAhead(determineNewlineSequenceAt(offset));
		return line;
	}
}