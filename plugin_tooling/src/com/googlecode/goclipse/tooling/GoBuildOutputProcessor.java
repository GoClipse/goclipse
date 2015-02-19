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
package com.googlecode.goclipse.tooling;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.tooling.ops.BuildOutputParser;
import melnorme.lang.tooling.ops.SourceLineColumnRange;
import melnorme.lang.tooling.ops.ToolSourceMessage;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;

public abstract class GoBuildOutputProcessor extends BuildOutputParser {
	
	public GoBuildOutputProcessor() {
	}
	
	public ArrayList2<ToolSourceMessage> getBuildErrors() {
		return buildMessages;
	}
	
	protected static final Pattern ERROR_LINE_Regex = Pattern.compile(
			"^([^:\\n]*):" + // file
			"(\\d*):" + // line
			"((\\d*):)?" + // column
			"\\s(.*)$" // error message
	);
	
	public static final Pattern WINDOWS_DRIVE_LETTER = Pattern.compile("[a-zA-Z]:\\\\.*", Pattern.DOTALL);
	
	@Override
	protected ArrayList<ToolSourceMessage> parseErrors(StringReader sr) throws IOException {
		buildMessages = new ArrayList2<>();

		BufferedReader br = new BufferedReader(sr);
		
		while(true) {
			String outputLine = br.readLine();
			if(outputLine == null) {
				break;
			}
			if(outputLine.startsWith("# ")) {
				// Not necessary for now
				continue;
			}
			
			String pathDevicePrefix = "";
			
			if(WINDOWS_DRIVE_LETTER.matcher(outputLine).matches()) {
				// Remove Windows drive letter from path, cause it will mess up regex
				pathDevicePrefix = outputLine.substring(0, 2);
				outputLine = outputLine.substring(2);
			}
			
			if(!outputLine.contains(":")) {
				continue; // Ignore line
			}
			
			Matcher matcher = ERROR_LINE_Regex.matcher(outputLine);
			if(!matcher.matches()) {
				handleUnknownLineSyntax(outputLine);
				continue;
			}
			
			String pathString = pathDevicePrefix + matcher.group(1);
			String lineString = matcher.group(2);
			String columnString = matcher.group(4);
			String errorMessage = matcher.group(5);
			
			while(true) {
				br.mark(1);
				int readChar = br.read();
				if(readChar == '\t') {
					br.reset();
					errorMessage = errorMessage + "\n" + br.readLine(); 
				} else {
					if(readChar != -1) {
						br.reset(); // reset not supported on EOF
					}
					break;
				}
			}
			
			try {
				addBuildError(parseError(pathString, lineString, columnString, errorMessage));
			} catch (CommonException ce) {
				handleLineParseError(ce);
				continue;
			}
		}
		
		return buildMessages;
	}
	
	@Override
	protected void doParseLine(String outputLine) {
		 assertFail();
	}
	
	protected ToolSourceMessage parseError(String pathString, String lineString,
			String columnString, String errorMessage) throws CommonException {
		Path filePath = parsePath(pathString).normalize();
		int lineNo = parsePositiveInt(lineString);
		int column = parseOptionalPositiveInt(columnString);
		
		return new ToolSourceMessage(new SourceLineColumnRange(filePath, lineNo, column), 
			StatusLevel.ERROR, errorMessage);
	}
	
	protected void addBuildError(ToolSourceMessage be) {
		buildMessages.add(be);
	}
	
}