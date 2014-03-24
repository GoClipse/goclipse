/*******************************************************************************
 * Copyright (c) 2007, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial implementation
 *******************************************************************************/
package melnorme.utilbox.misc;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import melnorme.utilbox.core.fntypes.Function;

/**
 * Miscelleanous String utilities 
 */
public final class StringUtil {
	
	public static final Charset ASCII = Charset.forName("ASCII");
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final Charset UTF16 = Charset.forName("UTF-16");
	
	
	/** @return a String produced from the given coll with the given separator String, 
	 * using the elements's toString() method. */
	public static String collToString(Collection<?> coll, String sep) {
		return collToString(coll, sep, null);
	}
	
	/** @return a String produced from the given coll with the given separator String, 
	 * using give strFunction to produce a string from each element. */
	public static <T> String collToString(Collection<T> coll, String sep, Function<T, String> strFunction) {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for(T item : coll){
			if(!first)
				sb.append(sep);
			first = false;
			
			sb.append(strFunction == null ? item.toString() : strFunction.evaluate(item));
		}
		return sb.toString();
	}
	
	// This helper function is not used in code, but rather for Eclipse IDE debug detail formatters
	@SuppressWarnings("unused")
	private static <T> String debug_collToString(Collection<T> coll) {
//		java.util.Collection<?> coll = this;
		java.lang.StringBuilder sb = new java.lang.StringBuilder();
		boolean first = true;
		for(Object item : coll){
			if(!first)
				sb.append("\n");
			first = false;
			
			String itemStr = item.toString();
			int firstLine = itemStr.indexOf("\n");
			if(firstLine != -1) {
				itemStr = itemStr.substring(0, firstLine); 
			}
			sb.append(itemStr);
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

	/** @return str with the given range (repOffset and repLen) substituted for repStr. */
	public static String replaceStr(String str, int repOffset, int repLen,
			String repStr) {
		return str.substring(0, repOffset) + repStr
				+ str.substring(repOffset + repLen, str.length());
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

	/** @return "" if given string is null, or the given string otherwise. */
	public static String nullAsEmpty(String string) {
		if(string == null)
			return "";
		return string;
	}
	
	/** @return null if given string is empty, or the given string otherwise. */
	public static String emptyAsNull(String string) {
		if(string != null && string.isEmpty())
			return null;
		return string;
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
	
	
	/** Trim given endMatch String from given string if there is a match. 
	 * @return the result. */
	public static String trimEnding(String string, String endMatch) {
		if(string.endsWith(endMatch)) {
			return string.substring(0, string.length() - endMatch.length());
		}
		return string;
	}
	
	/** Trim all leading characters from given string until given ch is found. 
	 * @return the result. Empty string if no match is found. */
	public static String trimUntil(char ch, String string) {
		int indexOf = string.indexOf(ch);
		if(indexOf != -1) {
			return string.substring(indexOf);
		}
		return "";
	}
	
	/** @return a copy of given string without leading spaces. */
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
	
}