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

import melnorme.lang.utils.parse.IBasicCharSource;
import melnorme.lang.utils.parse.ICharSource;

public class LexingUtils {
	
	public static <E extends Exception> boolean consumeAnyExceptNullOr(IBasicCharSource<E> reader, int character) 
			throws E {
		int ch = reader.lookahead();
		if(ch != character && ch != 0) {
			reader.consume();
			return true;
		}
		return false;
	}
	
	
	/**
	 * Consume a string delimited by give delimiter char, 
	 * with given escapeChar acting a possible escape (use -1 for no escapeChar)
	 */
	public static <E extends Exception> String consumeDelimitedString(IBasicCharSource<E> reader, 
			int delimiter, int escapeChar) throws E {
		StringBuilder sb = new StringBuilder();
		
		while(reader.hasCharAhead()) {
			
			char consumedChar = reader.nextChar();
			
			if(consumedChar == delimiter) {
				break;
			}
			else if(consumedChar == escapeChar && reader.hasCharAhead()) {
				int lookahead = reader.lookahead();
				if(lookahead == delimiter || lookahead == escapeChar) {
					
					char escapedChar = reader.nextChar();
					sb.append(escapedChar);
					continue;
				}
			}
			
			sb.append(consumedChar);
			
		}
		return sb.toString();
	}
	
	/* ----------------- whitespace ----------------- */
	
	public static <E extends Exception> int skipWhitespace(IBasicCharSource<E> reader) throws E {
		int count = 0;
		while(Character.isWhitespace(reader.lookahead())) {
			reader.consume();
			count++;
		}
		return count;
	}
	
	/* ----------------- Identifier helpers ----------------- */
	
	public static <E extends Exception> String tryConsumeJavaIdentifier(ICharSource<E> reader) throws E {
		int length = matchJavaIdentifier(reader);
		if(length == 0) {
			return null;
		}
		return reader.consumeString(length);
	}
	
	/**
	 * @return length of token matching a Java identifier. Zero if nothing matches
	 */
	public static <E extends Exception> int matchJavaIdentifier(ICharSource<E> reader) throws E {
		int length = 0;
		
		int la = reader.lookahead(length);
		if(la == -1 || !Character.isJavaIdentifierStart(la)) {
			return length;
		}
		length++;
		
		while(true) {
			la = reader.lookahead(length);
			if(la == -1 || !Character.isJavaIdentifierPart(la)) {
				break;
			}
			length++;
		}
		return length;
	}
	
	public static <E extends Exception> String readJavaIdentifier(ICharSource<E> reader) throws E {
		int length = matchJavaIdentifier(reader);
		return reader.consumeString(length);
	}

	
	/* -----------------  Line helpers  ----------------- */
	
	public static <E extends Exception> String determineNewlineSequenceAt(ICharSource<E> reader, 
			int offset) throws E {
		
		int la = reader.lookahead(offset);
			
		if(la == -1) {
			return "";
		}
		if(la == '\n') {
			return "\n";
		}
		if(la == '\r') {
			if(reader.lookahead(offset + 1) == '\n') {
				return "\r\n";
			}
			return "\r";
		}
		
		return null;
	}
	
	public static <E extends Exception> String stringUntilNewline(ICharSource<E> reader) throws E {
		return stringUntilNewline(reader, 0);
	}
	
	public static <E extends Exception> String stringUntilNewline(ICharSource<E> reader, int offset) throws E {
		int endPos = offset;
		while(true) {
			if(determineNewlineSequenceAt(reader, endPos) != null) {
				break;
			}
			endPos++;
		}
		
		return reader.lookaheadString(offset, endPos - offset);
	}
	
	public static <E extends Exception> String consumeLine(ICharSource<E> reader) throws E {
		int offset = 0;
		
		if(reader.lookaheadIsEOF()) {
			return null;
		}
		String line = stringUntilNewline(reader, offset);
		reader.consumeAhead(line);
		reader.consumeAhead(determineNewlineSequenceAt(reader, offset));
		return line;
	}
	
}