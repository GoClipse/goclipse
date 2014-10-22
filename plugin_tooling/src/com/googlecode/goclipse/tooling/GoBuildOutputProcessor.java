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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import melnorme.lang.tooling.ops.SourceLineColumnLocation;
import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.lang.utils.ParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class GoBuildOutputProcessor extends ParseHelper {
	
	protected ArrayList2<ToolSourceError> buildErrors;
	
	public GoBuildOutputProcessor() {
	}
	
	public ArrayList2<ToolSourceError> getBuildErrors() {
		return buildErrors;
	}
	
	public void parseOutput(ExternalProcessResult result) {
		parseErrors(result.stderr.toString());
	}
	
	public void parseErrors(String stderr) {
		buildErrors = new ArrayList2<>();
		
		StringReader sr = new StringReader(stderr);
		try {
			parseErrors(sr);
		} catch (IOException e) {
			e.printStackTrace();
			assertFail();
		}
	}
	
	protected final void handleUnknownLineSyntax(String line) {
		handleParseError(new CommonException("Unknown error line syntax: " + line));
	}
	
	protected abstract void handleParseError(CommonException ce);
	
	protected static final Pattern ERROR_LINE_Regex = Pattern.compile(
			"^([^:\\n]*):" + // file
			"(\\d*):" + // line
			"((\\d*):)?" + // column
			"\\s(.*)$" // error message
	);
	
	public static final Pattern WINDOWS_DRIVE_LETTER = Pattern.compile("[a-zA-Z]:\\\\.*", Pattern.DOTALL);
	
	protected void parseErrors(StringReader sr) throws IOException {
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
				handleParseError(ce);
				continue;
			}
		}
	}
	
	protected ToolSourceError parseError(String pathString, String lineString,
			String columnString, String errorMessage) throws CommonException {
		Path filePath = parsePath(pathString).normalize();
		int lineNo = parsePositiveInt(lineString);
		int column = parseColumnString(columnString);
		
		return new ToolSourceError(new SourceLineColumnLocation(filePath, lineNo, column), errorMessage);
	}
	
	protected int parseColumnString(String columnStr) throws CommonException {
		return columnStr == null ? -1 : parsePositiveInt(columnStr);
	}
	
	protected void addBuildError(ToolSourceError be) {
		buildErrors.add(be);
	}
	
}