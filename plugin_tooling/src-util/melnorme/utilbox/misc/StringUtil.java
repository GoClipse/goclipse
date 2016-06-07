/*******************************************************************************
 * Copyright (c) 2007 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import melnorme.utilbox.collections.ArrayList2;

/**
 * Miscelleanous String utilities 
 */
public class StringUtil {
	
	public static final Charset ASCII = Charset.forName("ASCII");
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final Charset UTF16 = Charset.forName("UTF-16");
	
	public static String asString(Object obj) {
		return obj == null ? null : obj.toString();
	}
	
	/** @return "" if given string is null, or the given string otherwise. */
	public static String nullAsEmpty(String string) {
		return string == null ? "" : string;
	}
	
	public static String nullAsEmpty(Object obj) {
		if(obj == null)
			return "";
		return obj.toString();
	}
	
	/** @return null if given string is empty, or the given string otherwise. */
	public static String emptyAsNull(String string) {
		if(string != null && string.isEmpty())
			return null;
		return string;
	}
	
	/** @return a String produced from the given coll with the given separator String, 
	 * using the elements's toString() method. */
	public static String toString(Iterable<?> iterable, String separator) {
		return toString(iterable, separator, null);
	}
	public static String collToString(Iterable<?> coll, String separator) {
		return iterToString(coll, separator, null);
	}
	
	public static <T> String iterToString(Iterable<T> iterable, String separator, 
			Function<? super T, String> toStringFn) {
		return toString(iterable, separator, toStringFn);
	}
	
	public static <T> String toString(Iterable<T> iterable, String separator, Function<? super T, String> toStringFn) {
		return iteratorToString(iterable.iterator(), separator, toStringFn);
	}
	
	private static <T> String iteratorToString(Iterator<T> iter, String sep, Function<? super T, String> toStringFn) {
		StringBuilder sb = new StringBuilder();
		
		while(iter.hasNext()) {
			T element = iter.next();
			
			sb.append(toStringFn == null ? element.toString() : toStringFn.apply(element));
			
			if(iter.hasNext()) {
				sb.append(sep);
			}
		}
		return sb.toString();
	}
	
	// This helper function is not used in code, but rather for Eclipse IDE debug detail formatters
	@SuppressWarnings("unused")
	private static <T> String debug_collToString(Collection<T> coll) {
//		java.util.Collection<?> coll = this;
		java.lang.StringBuilder sb = new java.lang.StringBuilder();
		boolean first = true;
		for(Object element : coll){
			if(!first)
				sb.append("\n");
			first = false;
			
			String elementString = element == null ? "null" : element.toString();
			sb.append(elementString.replace("\n", "¶"));
		}
		return sb.toString();
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private static String debug_mapToString(Map<?, ?> map) {
//		java.util.Map<?, ?> map = this;
		java.lang.StringBuilder sb = new java.lang.StringBuilder();
		boolean first = true;
		for(java.util.Map.Entry entry : map.entrySet()){
			if(!first)
				sb.append("\n");
			first = false;
			
			sb.append(entry.getKey());
			sb.append("►");
			Object element = entry.getValue();
			String elementString = element == null ? "null" : element.toString();
			sb.append(elementString.replace("\n", "¶"));
		}
		return sb.toString();
	}
	
	/** @return a String from the given coll with a given separator String. */	
	public static String collToString(Object[] coll, String sep) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(Object item : coll){
			if(!first)
				sb.append(sep);
			first = false;
			sb.append(item.toString());
		}
		return sb.toString();
	}
	
	/** Creates a String array where each element is the to toString()
	 * of each element of the given collection. */
	public static String[] collToStringArray(List<?> coll) {
		if(coll == null) 
			return new String[0];
		String[] strs = new String[coll.size()];
		Iterator<?> iter = coll.iterator();
		for (int i = 0; i < strs.length; i++) {
			strs[i] = iter.next().toString();
		}
		return strs;
	}
	
	/* ----------------- queries ----------------- */
	
	/** @return the number of ocurrences of given character in given string */
	public static int occurrenceCount(String string, char character) {
		int count = 0;
		
		for(int fromIndex = 0; true ; count++, fromIndex++ ) {
			fromIndex = string.indexOf(character, fromIndex);
			if(fromIndex == -1) {
				return count;
			}
		}
	}
	
	/* ----------------- modifications ----------------- */

	/** @return str with the given range (repOffset and repLen) substituted for repStr. */
	public static String replaceStr(String str, int repOffset, int repLen, String repStr) {
		return str.substring(0, repOffset) + repStr + str.substring(repOffset + repLen, str.length());
	}
	
	/**
	 * @return replace all occurrences of given matchStr with repStr, in given string.
	 * Similar to {@link String#replaceAll(String, String)} but doesn't use regexps, 
	 * therefore doesn't require any quoting.
	 */
	public static String replaceAll(String string, String matchStr, String repStr) {
		StringBuilder sb = new StringBuilder();
		int startIx = 0;
		while(true) {
			int matchIx = string.indexOf(matchStr, startIx);
			if(matchIx == -1) {
				sb.append(string, startIx, string.length());
				break;
			}
			
			sb.append(string, startIx, matchIx);
			sb.append(repStr);
			startIx = matchIx + matchStr.length();
		}
		return sb.toString();
	}

	/** Replace str with strRep in the given strb StringBuilder, if str occurs.
	 * @return true if str occurs in strb. */
	public static boolean replace(StringBuilder strb, String str, String repStr) {
		int ix = strb.indexOf(str);
		if(ix != -1) {
			strb.replace(ix, ix + str.length(), repStr);
			return true;
		}
		return false;
	}

	/** @return a substring of given string up until the start of the first occurrence of given match, 
	 * or the whole string if no match is found. */
	public static String substringUntilMatch(String string, String match) {
		int index = string.indexOf(match);
		return (index == -1) ? string : string.substring(0, index);
	}
	/** @return a substring of given string up until the start of the first occurrence of given match,  
	 * or null if no match is found. */
	public static String segmentUntilMatch(String string, String match) {
		int index = string.indexOf(match);
		return (index == -1) ? null   : string.substring(0, index);
	}
	
	/** @return a substring of given string starting from the end of the first occurrence of given match, 
	 * or the whole string if no match is found. */
	public static String substringAfterMatch(String string, String match) {
		int index = string.indexOf(match);
		return (index == -1) ? string : string.substring(index + match.length());
	}
	/** @return a substring of given string starting from the end of the first occurrence of given match, 
	 * or null if no match is found. */
	public static String segmentAfterMatch(String string, String match) {
		int index = string.indexOf(match);
		return (index == -1) ? null   : string.substring(index + match.length());
	}
	
	/** @return a substring of given string up until the start of the last occurrence of given match, 
	 * or the whole string if no match is found. */
	public static String substringUntilLastMatch(String string, String match) {
		int index = string.lastIndexOf(match);
		return (index == -1) ? string : string.substring(0, index);
	}
	/** @return a substring of given string up until the start of the last occurrence of given match,  
	 * or null if no match is found. */
	public static String segmentUntilLastMatch(String string, String match) {
		int index = string.lastIndexOf(match);
		return (index == -1) ? null   : string.substring(0, index);
	}
	
	/** @return a substring of given string starting from the end of the last occurrence of given match, 
	 * or the whole string if no match is found. */
	public static String substringAfterLastMatch(String string, String match) {
		int index = string.lastIndexOf(match);
		return (index == -1) ? string : string.substring(index + match.length());
	}
	/** @return a substring of given string starting from the end of the last occurrence of given match, 
	 * or null if no match is found. */
	public static String segmentAfterLastMatch(String string, String match) {
		int index = string.lastIndexOf(match);
		return (index == -1) ? null   : string.substring(index + match.length());
	}
	
	/** @return A substring of given string starting from the first occurrence of given match. 
	 * Empty string if no match is found. */
	public static String substringFromMatch(char match, String string) {
		int indexOf = string.indexOf(match);
		return indexOf == -1 ? "" : string.substring(indexOf);
	}
	/** @return A substring of given string starting from the first occurrence of given match. 
	 * Empty string if no match is found. */
	public static String substringFromMatch(String match, String string) {
		int indexOf = string.indexOf(match);
		return indexOf == -1 ? "" : string.substring(indexOf);
	}
	
	/** If given string starts with given startMatch, trim that from string. 
	 * @return the result. */
	public static String trimStart(String string, String startMatch) {
		if(string.startsWith(startMatch)) {
			return string.substring(startMatch.length());
		}
		return string;
	}
	
	/** If given string ends with given endMatch, trim that from string. 
	 * @return the result. */
	public static String trimEnd(String string, String endMatch) {
		if(string.endsWith(endMatch)) {
			return string.substring(0, string.length() - endMatch.length());
		}
		return string;
	}
	@Deprecated
	public static String trimEnding(String string, String endMatch) {
		return trimEnd(string, endMatch);
	}
	
	/** @return a substring of given string without leading spaces. */
	public static String trimLeadingSpaces(String string) {
		int pos = 0;
		while(pos < string.length() && string.charAt(pos) == ' ')
			pos++;
		return string.substring(pos);
	}

	/** @return a String of given length filled with spaces. */
	public static String newSpaceFilledString(int length) {
		return newFilledString(length, ' ');
	}
	
	/** @return a String of given length filled with given ch. */
	public static String newFilledString(int length, char ch) {
		char str[] = new char[length];
		Arrays.fill(str, ch);
		return new String(str);
	}
	
	/** @return a String of given length filled with given str. */
	public static String newFilledString(int length, String str) { 
		StringBuffer sb = new StringBuffer(length * str.length());
		for (int i = 0; i < length; i++ )
			sb = sb.append(str);
		
		return sb.toString();
	}
	
	/** Splits given string acording to given delimiter.
	 * @return an array with all the segments from the split string. */
	public static String[] splitString(String string, char delimiter) {
		final int count = occurrenceCount(string, delimiter);
		String[] segments = new String[count + 1];
		
		int startIx = 0;
		
		for (int i = 0; i < count; i++) {
			int endIx = string.indexOf(delimiter, startIx);
			segments[i] = string.substring(startIx, endIx);
			startIx = endIx + 1;
		}
		
		segments[count] = string.substring(startIx);
		return segments;
	}
	
	/** Splits given string using given regex. Return the result as an {@link ArrayList}. */
	public static ArrayList2<String> splitToList(String string, String regex) {
		assertNotNull(string);
		return new ArrayList2<>(string.split(regex));
	}
	
	public static String prefixStr(String prefix, String string) {
		return string == null ? "" : prefix + string;
	}
	
	public static String asString(String prefix, Object obj) {
		return obj == null ? "" : prefix + obj.toString();
	}
	
	public static String commonPrefix(String... strings) {
		assertTrue(strings.length > 0);
		String firstString = strings[0];
		
		int ix = 0;
		boolean finished = false;
		while(!finished) {
			if(ix >= firstString.length()) {
				break;
			}
			
			char ch = firstString.charAt(ix);
			
			for(int othersIx = 1; othersIx < strings.length; othersIx++) {
				String string = strings[othersIx];
				if(ix >= string.length() || string.charAt(ix) != ch) {
					finished = true;
					break;
				}
			}
			
			if(finished) {
				break;
			} else {
				ix++;
			}
			
		}
		return firstString.substring(0, ix);
	}
	
}