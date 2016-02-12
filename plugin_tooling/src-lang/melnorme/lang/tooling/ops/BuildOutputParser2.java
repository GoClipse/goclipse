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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;
import java.util.ArrayList;

import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class BuildOutputParser2 extends AbstractToolOutputParser<ArrayList<ToolSourceMessage>> {
	
	protected ArrayList2<ToolSourceMessage> buildMessages;
	
	public BuildOutputParser2(String toolPath) {
		super(toolPath);
	}
	
	public ArrayList2<ToolSourceMessage> getBuildMessages() {
		return buildMessages;
	}
	
	public ArrayList<ToolSourceMessage> parseOutput(ExternalProcessResult buildResult) throws CommonException {
		return doParse(buildResult);
	}
	
	@Override
	public ArrayList<ToolSourceMessage> parseToolResult(ExternalProcessResult result) throws CommonException {
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
		
		while(output.hasCharAhead()) {
			doParseToolMessage(output);
		}
		
		return buildMessages;
	}
	
	protected void doParseToolMessage(StringParseSource output) {
		try {
			ToolMessageData toolMessage = parseMessageData(output);
			
			if(toolMessage != null) {
				addBuildMessage(toolMessage);
			}
		} catch (CommonException ce) {
			handleMessageParseError(ce);
		}
	}
	
	protected void addBuildMessage(ToolMessageData toolMessage) throws CommonException {
		buildMessages.add(createMessage(toolMessage));
	}
	
	protected abstract ToolMessageData parseMessageData(StringParseSource output) throws CommonException;
	
	protected final void handleMessageParseError(CommonException ce) {
		handleParseError(ce);
	}
	
	@Override
	protected abstract void handleParseError(CommonException ce);
	
	protected CommonException createUnknownLineSyntaxError(String line) {
		return new CommonException("Unknown error line syntax: " + line);
	}
	
	/* -----------------  ----------------- */
	
	public static class ToolMessageData {
		
		public String pathString;
		public String lineString;
		public String columnString;
		
		public String endLineString;
		public String endColumnString;
		
		public String messageTypeString;
		
		public String sourceBeforeMessageText;
		public String messageText;
		
	}
	
	protected ToolSourceMessage createMessage(ToolMessageData msgdata) throws CommonException {
		
		Path filePath = parsePath(msgdata.pathString).normalize();
		int lineNo = parsePositiveInt(msgdata.lineString);
		int column = parseOptionalPositiveInt(msgdata.columnString);
		
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