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

import melnorme.lang.tooling.ops.ToolSourceError;
import melnorme.lang.tooling.ops.SourceLineColumnLocation;
import melnorme.lang.utils.ParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class GoBuildOutputProcessor extends ParseHelper {
	
	protected final Path basePath;
	protected ArrayList2<ToolSourceError> buildErrors;
	
	public GoBuildOutputProcessor(Path basePath) {
		this.basePath = basePath;
	}

	public ArrayList2<ToolSourceError> getBuildErrors() {
		return buildErrors;
	}
	
	protected void parseOutput(ExternalProcessResult result) {
		if(result.exitValue != 0) {
			
		}
		
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
		} catch (CommonException ce) {
			handleParseError(ce);
		}
	}
	
	protected final void handleUnknownLineFormat(String line) {
		handleParseError(new CommonException("Unknown error line syntax:" + line));
	}
	
	protected abstract void handleParseError(CommonException ce);
	
	protected static final Pattern ERROR_LINE_Regex = Pattern.compile(
			"^([^:\\n]*):" + // file
			"(\\d*):" + // line
			"((\\d*):)?" + // column
			"\\s(.*)$" // error message
	);
	
	protected void parseErrors(StringReader sr) throws IOException, CommonException {
		BufferedReader br = new BufferedReader(sr);
		
		Path currentPackagePath = basePath;
		while(true) {
			String line = br.readLine();
			if(line == null) {
				break;
			}
			if(line.startsWith("# ")) {
				String pathString = line.substring("# ".length());
				try {
					Path parsedPath = parsePath(pathString).getParent();
					currentPackagePath = basePath.resolve(parsedPath);
				} catch (CommonException ce) {
					handleParseError(ce);
				} 
				continue;
			}
			
			Matcher matcher = ERROR_LINE_Regex.matcher(line);
			if(!matcher.matches()) {
				handleUnknownLineFormat(line);
			}
			
			Path filePath = currentPackagePath.resolve(parsePath(matcher.group(1))).normalize();
			int lineNo = parsePositiveInt(matcher.group(2));
			int column = parseColumnString(matcher);
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
			
			addBuildError(new ToolSourceError(new SourceLineColumnLocation(filePath, lineNo, column), errorMessage));
		}
	}
	
	protected int parseColumnString(Matcher matcher) throws CommonException {
		String columnStr = matcher.group(4);
		return columnStr == null ? 0 : parsePositiveInt(columnStr);
	}
	
	protected void addBuildError(ToolSourceError be) {
		buildErrors.add(be);
	}
	
}