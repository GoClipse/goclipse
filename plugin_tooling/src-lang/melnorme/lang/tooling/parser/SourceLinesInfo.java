/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.parser;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Collections;

import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public class SourceLinesInfo {
	
	protected final String source;
	protected final ArrayList2<Integer> lines; // entries contain lineStart
	
	public SourceLinesInfo(String source) {
		this.source = source;
		this.lines = calculateLines(new StringCharSource(source));
	}
	
	protected ArrayList2<Integer> calculateLines(StringCharSource parser) {
		ArrayList2<Integer> lines = new ArrayList2<>();
		
		int lineStartOffset = 0;
		do {
			consumeNewLine(parser);
			
			lines.add(lineStartOffset);
			lineStartOffset = parser.getReadPosition();
		} while(parser.hasCharAhead());
		
		return lines;
	}
	
	protected void consumeNewLine(StringCharSource parser) {
		while(true) {
			String newlineSequence = LexingUtils.determineNewlineSequenceAt(parser, 0);
			if(newlineSequence != null) {
				parser.consumeAhead(newlineSequence);
				return;
			}
			
			parser.consume();
		}
		
	}
	
	public String getSource() {
		return source;
	}
	
	/* -----------------  ----------------- */

	public void validateOffset(int offset) throws CommonException {
		if(offset > source.length()) {
			throw CommonException.fromMsgFormat("Invalid offset {0}, it is out of bounds.", offset);
		}
	}
	
	public int getNumberOfLines() {
		return lines.size();
	}
	
	public int getOffsetForLine(int lineIndex) {
		assertTrue(lineIndex >= 0 && lineIndex < lines.size());
		return lines.get(lineIndex);
	}
	
	public int getLineForOffset(int offset) throws CommonException {
		validateOffset(offset);
		
		int binarySearchResult = Collections.binarySearch(lines, offset);
		return binarySearchResult >= 0 ? binarySearchResult : -(binarySearchResult + 1) -1;
	}
	
	public int getLineStartForOffset(int offset) throws CommonException {
		return getOffsetForLine(getLineForOffset(offset));
	}
	
	public int getColumnForOffset(int offset) throws CommonException {
		return offset - getLineStartForOffset(offset);
	}
	
	/* -----------------  ----------------- */
	
	public int getValidatedOffset_1(int line_1, int column_1) throws CommonException {
		if(line_1 < 1) {
			throw new CommonException("Invalid line number: " + line_1);
		}
		if(column_1 < 1) {
			throw new CommonException("Invalid column number: " + line_1);
		}
		
		int lineIndex = line_1 - 1;
		int columnIndex = column_1 - 1;
		
		if(lineIndex >= lines.size()) {
			throw CommonException.fromMsgFormat("Invalid line: {0} is over the max bound: {1}.", 
				line_1, lines.size() + 1);
		}
		
		return getValidateOffset_do(lineIndex, columnIndex);
	}
	
	public int getValidatedOffset_0(int line_0, int column_0) throws CommonException {
		if(line_0 < 0) {
			throw new CommonException("Invalid line number: " + line_0);
		}
		if(column_0 < 0) {
			throw new CommonException("Invalid column number: " + line_0);
		}
		
		if(line_0 >= lines.size()) {
			throw CommonException.fromMsgFormat("Invalid line: {0} is over the max bound: {1}.", 
				line_0, lines.size());
		}
		
		return getValidateOffset_do(line_0, column_0);
	}
	
	protected int getValidateOffset_do(int lineIndex, int columnIndex) throws CommonException {
		int offset = getOffsetForLine(lineIndex) + columnIndex;
		
		if(lineIndex + 1 < lines.size()) {
			if(offset >= getOffsetForLine(lineIndex + 1)) {
				throw new CommonException("Invalid column, out of bounds.");
			}
		}
		
		if(offset > source.length()) {
			throw new CommonException("Invalid line+column, out of bounds.");
		}
		return offset;
	}
	
	/* -----------------  ----------------- */
	
	public int getIdentifierAt(int validatedOffset) {
		StringCharSource parser = new StringCharSource(source);
		parser.consume(validatedOffset);
		return LexingUtils.matchJavaIdentifier(parser);
	}
	
}