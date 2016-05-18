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
package melnorme.lang.tooling.ops;

import java.nio.file.Path;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.misc.NumberUtil;
import melnorme.utilbox.misc.Pair;
import melnorme.utilbox.misc.StringUtil;


public interface ToolOutputParseHelper {
	
	default int parsePositiveInt(String integerString) throws CommonException {
		return NumberUtil.parsePositiveInt(integerString);
	}
	
	default int parseInt(String integerString) throws CommonException {
		return NumberUtil.parseInt(integerString);
	}

	default int parseInt(String integerString, String errorMessage) throws CommonException {
		return NumberUtil.parseInt(integerString, errorMessage);
	}
	
	default Path parsePath(String pathString) throws CommonException {
		return MiscUtil.createPath(pathString);
	}
	
	default Location parseLocation(String pathString) throws CommonException {
		return Location.create(pathString);
	}
	
	public static FindDefinitionResult parsePathLineColumn(String sourceString, String separator) 
			throws CommonException {
		
		Pair<String, LineColumnPosition> pair = parsePathLineColumn2(sourceString, separator);
		Location loc = Location.create(pair.getFirst());
		LineColumnPosition lcPos = pair.getSecond();
		
		return new FindDefinitionResult(loc, new SourceLineColumnRange(lcPos.line, lcPos.column), null);
	}
	
	/**
	 * Parse a path+line+column from given string, which are expected to be separated by given separator.
	 * Example sourceString: <code>D:\foo\bar\xpto.go:22:12</code> 
	 */
	public static Pair<String, LineColumnPosition> parsePathLineColumn2(String sourceString, String separator)
			throws CommonException {
		// We need to parse sourceLocationString starting from the end, 
		// because on Windows the filePath can contain the ':' char
		
		String columnStr = StringUtil.segmentAfterLastMatch(sourceString, separator);
		sourceString = StringUtil.substringUntilLastMatch(sourceString, separator);

		String lineStr = StringUtil.segmentAfterLastMatch(sourceString, separator);
		sourceString = StringUtil.substringUntilLastMatch(sourceString, separator);
		String pathString = sourceString;
		
		LineColumnPosition second = parseLineColumn(lineStr, columnStr, 0, 0);
		
		return Pair.create(pathString, second);
	}
	
	public static LineColumnPosition parseLineColumn(String lineStr, String columnStr, int line_start,
			int column_start) throws CommonException {
		if(columnStr == null || lineStr == null) {
			throw new CommonException("No line or column position given.", null);
		}
		int line = NumberUtil.parseInt(lineStr, "Line is not a number: " + lineStr);
		if(line < line_start) {
			throw new CommonException("Line is not a positive number: " + lineStr);
		}
		int column = NumberUtil.parseInt(columnStr, "Column is not a number: " + columnStr);
		if(column < column_start) {
			throw new CommonException("Column is not a positive number: " + columnStr);
		}
		
		return new LineColumnPosition(line, column);
	}
	
}