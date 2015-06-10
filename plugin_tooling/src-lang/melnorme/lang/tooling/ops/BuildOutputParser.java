/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
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
import java.util.ArrayList;

import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class BuildOutputParser extends AbstractToolOutputParser<ArrayList<ToolSourceMessage>> {
	
	protected ArrayList2<ToolSourceMessage> buildMessages;
	
	public BuildOutputParser() {
	}
	
	public ArrayList2<ToolSourceMessage> getBuildMessages() {
		return buildMessages;
	}
	
	public ArrayList<ToolSourceMessage> parseOutput(ExternalProcessResult buildResult) throws CommonException {
		return doParse(buildResult);
	}
	@Override
	protected ArrayList<ToolSourceMessage> doParse(ExternalProcessResult result) throws CommonException {
		return parse(result.getStdErrBytes().toString(StringUtil.UTF8));
	}
	
	public ArrayList<ToolSourceMessage> parseMessages(String stderr) throws CommonException {
		return parse(stderr);
	}
	@Override
	protected ArrayList<ToolSourceMessage> parse(StringParseSource output) throws CommonException {
		return parseMessages(output);
	}
	
	protected ArrayList<ToolSourceMessage> parseMessages(StringParseSource output) {
		buildMessages = new ArrayList2<>();
		
		while(!output.lookaheadIsEOF()) {
			doParseLine(output);
		}
		
		return buildMessages;
	}
	
	protected void doParseLine(StringParseSource output) {
		String outputLine = output.consumeLine();
		doParseLine(outputLine, output);
	}
	
	protected abstract void doParseLine(String outputLine, StringParseSource output);
	
	protected void addMessage(String pathString, String lineString, String columnString, String endLineString,
			String endColumnString, String messageTypeString, String message) {
		try {
			buildMessages.add(createMessage(pathString, lineString, columnString, endLineString, endColumnString, 
				messageTypeString, message));
		} catch (CommonException ce) {
			handleLineParseError(ce);
		}
	}
	
	protected void handleUnknownLineSyntax(String line) {
		handleLineParseError(new CommonException("Unknown error line syntax: " + line));
	}
	
	@Override
	protected abstract void handleLineParseError(CommonException ce);
	
	/* -----------------  ----------------- */
	
	protected ToolSourceMessage createMessage(String pathString, String lineString, String columnString, 
			String endLineString, String endColumnString, 
			String messageTypeString, String message) throws CommonException {
		
		Path filePath = parsePath(pathString).normalize();
		int lineNo = parsePositiveInt(lineString);
		int column = parseOptionalPositiveInt(columnString);
		
		int endline = parseOptionalPositiveInt(endLineString);
		int endColumn = parseOptionalPositiveInt(endColumnString);
		
		// due to the REGEXP, can only be WARNING or ERROR
		StatusLevel msgKind = StatusLevel.fromString(messageTypeString);  
		
		SourceLineColumnRange location = new SourceLineColumnRange(filePath, lineNo, column, endline, endColumn);
		return new ToolSourceMessage(location, msgKind, message);
	}
	
	protected StatusLevel parseMessageKind(String messageTypeString) throws CommonException {
		return StatusLevel.fromString(messageTypeString);
	}
	
	protected int parseOptionalPositiveInt(String columnStr) throws CommonException {
		return columnStr == null ? -1 : parsePositiveInt(columnStr);
	}
	
}