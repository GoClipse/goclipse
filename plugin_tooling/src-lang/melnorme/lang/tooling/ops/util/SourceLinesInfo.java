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
package melnorme.lang.tooling.ops.util;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.tooling.parser.lexer.LexingUtils;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public class SourceLinesInfo {
	
	protected final String source;
	protected ArrayList2<Integer> lines;
	
	public SourceLinesInfo(String source) {
		this.source = source;
		
		StringParseSource parser = new StringParseSource(source);
		
		calculateLines(parser);
	}
	
	protected void calculateLines(StringParseSource parser) {
		lines = new ArrayList2<>();
		
		int lineStartOffset = 0;
		while(parser.hasCharAhead()) {
			consumeNewLine(parser);
			
			lines.add(lineStartOffset);
			lineStartOffset = parser.getReadOffset();
		}
		
	}
	
	protected void consumeNewLine(StringParseSource parser) {
		while(true) {
			String newlineSequence = LexingUtils.determineNewlineSequenceAt(parser, 0);
			if(newlineSequence != null) {
				parser.consumeAhead(newlineSequence);
				return;
			}
			
			parser.consume();
		}
		
	}
	
	public int getOffsetForLine(int lineIndex) {
		assertTrue(lineIndex >= 0 && lineIndex < lines.size());
		return lines.get(lineIndex);
	}
	
	public int getValidatedOffset(int line_1, int column_1) throws CommonException {
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
		
		int offset = getOffsetForLine(lineIndex) + columnIndex;
		
		if(offset >= source.length()) {
			throw new CommonException("Invalid line+column, out of bounds.");
		}
		return offset;
	}

	public int getIdentifierAt(int validatedOffset) {
		StringParseSource parser = new StringParseSource(source);
		parser.consume(validatedOffset);
		return LexingUtils.matchJavaIdentifier(parser);
	}
	
}