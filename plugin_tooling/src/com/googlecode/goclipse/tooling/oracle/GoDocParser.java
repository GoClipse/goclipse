/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.tooling.oracle;

import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringCharSource;

public class GoDocParser {
	
	public String parseDocForDefinitionAt(String text, int offset) {
		StringBuilder docSB = null;
		
		String commentPrefix = null;
		
		while(true) {
			int lineStart = findStartPosOfPreviousLine(text, offset);
			if(lineStart == -1) {
				break;
			}
			
			if(commentPrefix == null) {
				// Determine comment prefix (some optional whitespace followed by "//"
				commentPrefix = getCommentPrefix(text, lineStart);
				if(commentPrefix == null) {
					// No valid comment found, no doc available
					return null;
				}
			} else {
				// For next line above to be added, it must have the same comment prefix 
				// (same amount of whitespace and "//") 
				if(!text.regionMatches(lineStart, commentPrefix, 0, commentPrefix.length())) {
					break;
				}
			}
			
			if(docSB == null) {
				docSB = new StringBuilder();
			}
			docSB.insert(0, getLineTextAt(text, lineStart+commentPrefix.length()));
			offset = lineStart;
		}
		if(docSB == null) {
			return null;
		}
		return docSB.toString().trim();
	}
	
	public int findStartPosOfPreviousLine(String source, int offset) {
		int index = source.lastIndexOf('\n', offset-1); // note, -1 is a valid argument
		if(index == -1) {
			return -1; // There is no previous line
		}
		// Index now at end of previous line, find the start
		index = source.lastIndexOf('\n', index-1); // note, -1 is a valid argument
		if(index == -1) {
			return 0; // It's the first line, which starts at 0 
		}
		return index + 1;
	}
	
	public String getCommentPrefix(String text, int lineStart) {
		for(int ix = lineStart; ix+1 < text.length(); ix++) {
			char ch = text.charAt(ix);
			if(Character.isSpaceChar(ch) || ch == '\t') {
				continue;
			}
			if(ch == '/' && text.charAt(ix+1) == '/' ) {
				return text.substring(lineStart, ix+2);
			} else {
				break;
			}
		}
		return null; // Not found
	}
	
	public String getLineTextAt(String text, int offset) {
		StringCharSource charSource = new StringCharSource(text);
		charSource.consume(offset);
		return LexingUtils.stringUntilNewline(charSource);
	}
	
}