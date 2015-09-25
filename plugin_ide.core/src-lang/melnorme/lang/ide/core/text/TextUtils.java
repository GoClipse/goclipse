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

public class TextUtils {

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
	
}