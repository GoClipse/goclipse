/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *     Pieter Penninckx - added copy constructor for ToolMessageData
 *******************************************************************************/
package melnorme.lang.tooling.toolchain.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusLevel;


public class ToolMessageParser  
	implements ToolOutputParseHelper 
{
	
	public ToolSourceMessage createMessage(ToolMessageData msgdata) throws CommonException {
		
		Path filePath = parsePath(msgdata.pathString).normalize();
		int lineNo = parsePositiveInt(msgdata.lineString);
		int column = parseOptionalPositiveInt(StringUtil.emptyAsNull(msgdata.columnString));
		
		int endline = parseOptionalPositiveInt(msgdata.endLineString);
		int endColumn = parseOptionalPositiveInt(msgdata.endColumnString);
		
		// messageTypeString should be valid to parse
		Severity severity = Severity.fromString(msgdata.messageTypeString);
		assertNotNull(severity);
		
		SourceLineColumnRange sourceRange = new SourceLineColumnRange(lineNo, column, endline, endColumn);
		return new ToolSourceMessage(filePath, sourceRange, severity, msgdata.messageText);
	}
	
	protected StatusLevel parseMessageKind(String messageTypeString) throws CommonException {
		return StatusLevel.fromString(messageTypeString);
	}
	
	protected int parseOptionalPositiveInt(String columnStr) throws CommonException {
		return columnStr == null ? -1 : parsePositiveInt(columnStr);
	}
	
}