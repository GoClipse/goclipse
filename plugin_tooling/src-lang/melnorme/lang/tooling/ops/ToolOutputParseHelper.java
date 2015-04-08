/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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

import melnorme.lang.utils.ParseHelper;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;


public class ToolOutputParseHelper extends ParseHelper {
	
	public ToolOutputParseHelper() {
	}
	
	/**
	 * Parse a path+line+column from given string, which are expected to be separated by given separator.
	 * Example sourceString: <code>D:\foo\bar\xpto.go:22:12</code> 
	 */
	protected SourceLineColumnRange parsePathLineColumn(String sourceString, String separator) 
			throws CommonException {
		
		// We need to parse sourceLocationString starting from the end, 
		// because on Windows the filePath can contain the ':' char
		
		String columnStr = StringUtil.segmentAfterLastMatch(sourceString, separator);
		sourceString = StringUtil.substringUntilLastMatch(sourceString, separator);

		String lineStr = StringUtil.segmentAfterLastMatch(sourceString, separator);
		sourceString = StringUtil.substringUntilLastMatch(sourceString, separator);
		
		
		if(columnStr == null || lineStr == null) {
			throw new CommonException("No line or column position given.", null);
		}
		int line = parseInt(lineStr, "Invalid number for line: " + lineStr);
		int column = parseInt(columnStr, "Invalid number for column: " + columnStr);
		
		Path path = parsePath(sourceString);
		
		return new SourceLineColumnRange(path, line, column);
	}
	
}