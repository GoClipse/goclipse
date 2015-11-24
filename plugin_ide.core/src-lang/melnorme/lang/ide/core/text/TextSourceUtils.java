/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.text;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Arrays;

import melnorme.lang.utils.parse.StringParseSource;

public class TextSourceUtils {

	public static char getBracePair(char braceChar) {
		
		switch (braceChar) {
		case '(': return ')';
		case ')': return '(';
		case '[': return ']';
		case ']': return '[';
		case '{': return '}';
		case '}': return '{';
		case '\"': return '\"';
		case '\'': return '\'';
		}		
		return braceChar;
	}
	
	public static boolean isPrefix(String prefix, String string, boolean ignoreCase) {
		if(prefix.length() > string.length()) {
			return false;
		}
		String subString = string.substring(0, prefix.length());
		return ignoreCase ? subString.equalsIgnoreCase(prefix) : subString.equals(prefix);
	}
	
	/* -----------------  ----------------- */
	
	public static String getLineIndentForOffset(String source, int offset) {
		int lineStart = TextSourceUtils.findLineStartForOffset(source, offset);
		return getLineIndentForLineStart(source, lineStart);
	}
	
	public static String getLineIndentForLineStart(String source, int lineStart) {
		return getLineIndentForLineStart(source, lineStart, source.length());
	}
	
	public static String getLineIndentForLineStart(String source, int lineStart, int endLimit) {
		int indentEnd = TextSourceUtils.findEndOfIndent(source, lineStart, endLimit);
		return source.substring(lineStart, indentEnd);
	}
	
	public static int findEndOfIndent(String source, int offset) {
		return findEndOfIndent(source, offset, source.length());
	}
	public static int findEndOfIndent(String source, int offset, int endLimit) {
		assertTrue(endLimit <= source.length());
		assertTrue(offset <= endLimit);
		
		StringParseSource parseSource = new StringParseSource(source);
		parseSource.consume(offset);
		
		while(offset < endLimit) {
			
			if(parseSource.tryConsume(' ') || parseSource.tryConsume('\t')) {
				offset++;
				continue;
			} 
			break;
		}
		return parseSource.getReadPosition();
	}
	
	public static int findLineStartForOffset(String source, int offset) {
		assertTrue(offset <= source.length());
		
		StringParseSource parseSource = new StringParseSource(source);
		
		while(offset > 0) {
			int previousChar = parseSource.lookahead(offset-1);
			if(previousChar == '\n' || previousChar == '\r') {
				break;
			}
			offset--;
		}
		return offset;
	}
	
	public static String getLineSegmentBeforeOffset(String source, int offset) {
		int lineStart = TextSourceUtils.findLineStartForOffset(source, offset);
		return source.substring(lineStart, offset);
	}
	
	/* -----------------  ----------------- */
	
	public static String getNSpaces(int spaces) {
		return getNChars(spaces, ' ');
	}
	
	public static String getNChars(int count, char ch) {
		char[] array = new char[count];
		Arrays.fill(array, ch);
		return new String(array);
	}
	
	public static String stringNTimes(String string, int count) {
		assertTrue(count >=0);
		
		StringBuffer result = new StringBuffer(string.length() * count);
		while(count-- > 0) {
			result.append(string);
		}
		return result.toString();
	}
	
}