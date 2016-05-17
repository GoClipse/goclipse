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
package com.googlecode.goclipse.tooling;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.tooling.ops.BuildOutputParser;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.lang.utils.parse.LexingUtils;
import melnorme.lang.utils.parse.StringParseSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class GoBuildOutputProcessor extends BuildOutputParser {
	
	public GoBuildOutputProcessor() {
	}
	
	@Override
	protected String getToolProcessName() {
		return "go build";
	}
	
	@Override
	protected ArrayList<ToolSourceMessage> doHandleProcessResult(ExternalProcessResult result) throws CommonException {
		ArrayList<ToolSourceMessage> msgs = parse(result.getStdErrBytes().toString(StringUtil.UTF8));
		msgs.addAll(parse(result.getStdOutBytes().toString(StringUtil.UTF8)));
		return msgs;
	}
	
	public ArrayList2<ToolSourceMessage> getBuildErrors() {
		return buildMessages;
	}
	
	protected static final Pattern ERROR_LINE_Regex = Pattern.compile(
			"^([^:\\n]*):" + // file
			"(\\d*):" + // line
			"(\\d*)?(:)?" + // column
			"((warning|error|info):)?" + // type
			"\\s(.*)$" // error message
	);
	
	public static final Pattern WINDOWS_DRIVE_LETTER = Pattern.compile("[a-zA-Z]:\\\\.*", Pattern.DOTALL);
	
	@Override
	protected ToolMessageData parseMessageData(StringParseSource output) throws CommonException {
		String outputLine = LexingUtils.consumeLine(output);
		
		if(outputLine.startsWith("# ")) {
			// Not necessary for now
			return null;
		}
		
		String pathDevicePrefix = "";
		
		if(WINDOWS_DRIVE_LETTER.matcher(outputLine).matches()) {
			// Remove Windows drive letter from path, cause it will mess up regex
			pathDevicePrefix = outputLine.substring(0, 2);
			outputLine = outputLine.substring(2);
		}
		
		if(!outputLine.contains(":")) {
			return null; // Ignore line
		}
		
		ToolMessageData msgData = new ToolMessageData();
		
		Matcher matcher = ERROR_LINE_Regex.matcher(outputLine);
		if(!matcher.matches()) {
			throw createUnknownLineSyntaxError(outputLine);
		}
		
		msgData.pathString = pathDevicePrefix + matcher.group(1);
		msgData.lineString = matcher.group(2);
		msgData.columnString = matcher.group(3);
		msgData.messageTypeString = matcher.group(6);
		msgData.messageText = matcher.group(7);
		
		while(true) {
			int readChar = output.lookahead();
			if(readChar == '\t') {
				String nextLine = LexingUtils.consumeLine(output);
				msgData.messageText += "\n" + nextLine;
			} else {
				break;
			}
		}
		
		return msgData;
	}
	
	@Override
	protected ToolSourceMessage createMessage(ToolMessageData msgdata) throws CommonException {
		if(msgdata.messageTypeString == null) {
			msgdata.messageTypeString = StatusLevel.ERROR.toString();
		}
		return super.createMessage(msgdata);
	}
	
}