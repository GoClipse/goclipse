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
package melnorme.lang.tooling.ops;


import melnorme.lang.tooling.ops.util.SourceLinesInfo;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.NumberUtil;
import melnorme.utilbox.misc.StringUtil;

public abstract class AbstractStructureParser extends ToolOutputParseHelper {
	
	protected final Location location;
	protected final SourceLinesInfo sourceLinesInfo;
	
	public AbstractStructureParser(Location location, String langSource) {
		this.location = location;
		this.sourceLinesInfo = new SourceLinesInfo(langSource);
	}
	
	public abstract SourceFileStructure parse(String outputParseSource) throws CommonException;
	
	
	public int parseSourceLocation(String locationString) throws CommonException {
		if(locationString.contains(":")) {
			String lineStr_0based = StringUtil.segmentUntilMatch(locationString, ":");
			String columnStr_0based = StringUtil.segmentAfterMatch(locationString, ":");
			int line_0 = NumberUtil.parsePositiveInt(lineStr_0based); 
			int col_0 = NumberUtil.parsePositiveInt(columnStr_0based);
			
			return sourceLinesInfo.getValidatedOffset_0(line_0, col_0);
		}
		if(locationString.startsWith("@")) {
			locationString = locationString.substring(1);
		}
		return parsePositiveInt(locationString);
	}
	
}