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
package melnorme.lang.utils.parse;

public class LexingUtils {
	
	/** 
	 * Consume until given delimiter char is found (also included).	 
	 */
	public static <E extends Exception> String consumeUntilDelimiter(ICharSource<E> reader, 
			char delimiter) throws E {
		return consumeUntilDelimiter(reader, delimiter, delimiter);
	}
	
	/** 
	 * Consume until given delimiter char is found (also included).
	 * Given escapeChar can escape itself, and delimiter 	 
	 */
	public static <E extends Exception> String consumeUntilDelimiter(ICharSource<E> reader, 
			char delimiter, char escapeChar) throws E {
		StringBuilder sb = new StringBuilder();
		
		while(reader.hasCharAhead()) {
			
			char consumedChar = reader.nextChar();
			
			if(consumedChar == delimiter) {
				break;
			}
			else if(consumedChar == escapeChar && reader.hasCharAhead()) {
				
				char secondChar = reader.lookaheadChar();
				if(secondChar == delimiter || secondChar == escapeChar) {
					reader.nextChar();
					sb.append(secondChar);
					continue;
				}
			}
			
			sb.append(consumedChar);
			
		}
		return sb.toString();
	}
	
	
	public static <E extends Exception> void advanceDelimitedString(ICharSource<E> reader, 
			char delimiter, char escapeChar) throws E {
		
		while(reader.hasCharAhead()) {
			
			char consumedChar = reader.nextChar();
			
			if(consumedChar == delimiter) {
				break;
			}
			else if(consumedChar == escapeChar) {
				reader.consumeAny(); // consumed escaped char
			}
			
		}
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
	
	public static <E extends Exception> int skipWhitespaceExceptNL(IBasicCharSource<E> reader) throws E {
		int count = 0;
		while(Character.isWhitespace(reader.lookahead())) {
			
			if(reader.lookahead() == '\n' || reader.lookahead() == '\r') {
				break;
			}
			
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