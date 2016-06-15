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
package melnorme.lang.tooling.toolchain.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;
import java.util.ArrayList;

import melnorme.lang.tooling.common.SourceLineColumnRange;
import melnorme.lang.tooling.common.ToolSourceMessage;
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusLevel;


public abstract class BuildOutputParser2 extends AbstractToolResultParser<ArrayList<ToolSourceMessage>> 
	implements ToolOutputParseHelper {
	
	protected ArrayList2<ToolSourceMessage> buildMessages;
	
	public BuildOutputParser2() {
	}
	
	public ArrayList2<ToolSourceMessage> getBuildMessages() {
		return buildMessages;
	}
	
	@Override
	protected String getToolName() {
		return "Build tool";
	}
	
	@Override
	public ArrayList<ToolSourceMessage> doParseResult(ExternalProcessResult result)
			throws CommonException {
		try {
			validateExitCode(result);
			
			return parseOutput(result.getStdOutBytes().toString(StringUtil.UTF8));
		} catch(StatusValidation e) {
			// There shouldn't even be a StatusValidation error for build, 
			// because the source should always be able to be analyis. (might need  to refactor this)
			throw new CommonException(e.getMessage());
		}
	}
	
	@Override
	public final ArrayList<ToolSourceMessage> parseOutput(String output) throws CommonException {
		return parseOutput(new StringCharSource(output));
	}
	
	@Override
	public ArrayList<ToolSourceMessage> parseOutput(StringCharSource output) throws CommonException {
		buildMessages = new ArrayList2<>();
		
		while(output.hasCharAhead()) {
			doParseToolMessage(output);
		}
		
		return buildMessages;
	}
	
	protected void doParseToolMessage(StringCharSource output) {
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
	
	protected abstract ToolMessageData parseMessageData(StringCharSource output) throws CommonException;
	
	protected final void handleMessageParseError(CommonException ce) {
		handleParseError(ce);
	}
	
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