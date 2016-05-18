/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.toolchain;

import java.nio.file.Path;

import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.parser.SourceLinesInfo;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.NumberUtil;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StringUtil;

public class SourceFileLocation {
	
	protected final String filePath;
	protected final String line;
	protected final String column;
	
	public SourceFileLocation(String filePath, String line, String column) {
		this.filePath = filePath;
		this.line = line;
		this.column = column;
	}
	
	public String getFileLocationString() {
		return filePath;
	}
	
	public String getLineString() {
		return line;
	}
	
	public String getColumnString() {
		return column;
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Parse a {@link SourceFileLocation} in the format `filePath
	 */
	public static SourceFileLocation parseSourceRange(String positionString, char separator) throws CommonException {
		
		String sourceRangeString = getSourceRangeString(positionString, separator);
		if(sourceRangeString == null) {
			throw new CommonException("Source range not available in `" + positionString + "`");
		}
		
		String filePath = positionString.substring(0, positionString.length() - sourceRangeString.length() -1);
		
		String lineStr_1based = StringUtil.segmentUntilMatch(sourceRangeString, ":");
		String columnStr_1based = StringUtil.segmentAfterMatch(sourceRangeString, ":");
		
		return new SourceFileLocation(filePath, lineStr_1based, columnStr_1based);
	}
	
	protected static String getSourceRangeString(String posString, char separator) {
		int ix = posString.length();
		int count = 0;
		
		while(--ix >= 0) {
			if(posString.charAt(ix) == separator) {
				
				if(++count == 2) {
					return posString.substring(ix + 1, posString.length());  
				}
			}
		}
		return null;
	}
	
	/* -----------------  ----------------- */
	
	public Location getFileLocation() throws CommonException {
		return Location.create(filePath);
	}
	
	public Path getFilePath() throws CommonException {
		return PathUtil.createPath(filePath);
	}
	
	public SourceRange parseSourceRangeFrom1BasedIndex(SourceLinesInfo sourceLinesInfo) throws CommonException {
		int line_1based = NumberUtil.parseInt(line);
		int column_1based = NumberUtil.parseInt(column);
		
		int validatedOffset = sourceLinesInfo.getValidatedOffset_1(line_1based, column_1based);
		int length = sourceLinesInfo.getIdentifierAt(validatedOffset);
		
		return new SourceRange(validatedOffset, length);
	}
	
}