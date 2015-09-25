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
import melnorme.utilbox.misc.StringUtil;


@SuppressWarnings("static-method")
public class ToolOutputParseHelper {
	
	public ToolOutputParseHelper() {
	}
	
	/* ----------------- Common parsing utils ----------------- */
	
	protected final int parsePositiveInt(String integerString) throws CommonException {
		return NumberUtil.parsePositiveInt(integerString);
	}
	
	protected final int parseInt(String integerString) throws CommonException {
		return NumberUtil.parseInt(integerString);
	}

	protected final int parseInt(String integerString, String errorMessage) throws CommonException {
		return NumberUtil.parseInt(integerString, errorMessage);
	}
	
	protected final Path parsePath(String pathString) throws CommonException {
		return MiscUtil.createPath(pathString);
	}
	
	protected final Location parseLocation(String pathString) throws CommonException {
		return Location.create(pathString);
	}
	
	/**
	 * Parse a path+line+column from given string, which are expected to be separated by given separator.
	 * Example sourceString: <code>D:\foo\bar\xpto.go:22:12</code> 
	 */
	public static FindDefinitionResult parsePathLineColumn(String sourceString, String separator) 
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
		int line = NumberUtil.parseInt(lineStr, "Invalid number for line: " + lineStr);
		int column = NumberUtil.parseInt(columnStr, "Invalid number for column: " + columnStr);
		
		Location loc = Location.create(sourceString);
		
		return new FindDefinitionResult(loc, new SourceLineColumnRange(line, column), null);
	}
	
}