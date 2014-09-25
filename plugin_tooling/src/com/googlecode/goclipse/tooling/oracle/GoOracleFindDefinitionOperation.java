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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import java.nio.file.Path;

import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.goclipse.tooling.GoEnvironment;
import com.googlecode.goclipse.tooling.JsonDeserializeHelper;
import com.googlecode.goclipse.tooling.StatusException;

public class GoOracleFindDefinitionOperation extends JsonDeserializeHelper {
	
	protected final String goOraclePath;
	
	public GoOracleFindDefinitionOperation(String goOraclePath) {
		this.goOraclePath = goOraclePath;
	}
	
	public ProcessBuilder createProcessBuilder(GoEnvironment goEnv, Path filePath, int offset) {
		Path goPackage = goEnv.getGoPackageFromGoModule(filePath);
		/*BUG here goPackage == null*/
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			goOraclePath,
			"-pos=" + filePath.toString() + ":#" + offset + ",#" + offset,
			"-format=json",
			"describe",
			goPackage.toString()
		);
		
		return goEnv.createProcessBuilder(commandLine);
	}
	
	public GoOracleFindDefinitionResult parseJsonResult(ExternalProcessResult result) throws StatusException {
		if(result.exitValue != 0) {
			throw new StatusException("Program exited with non-zero status: " + result.exitValue, null);
		}
		
		String output = result.getStdOutBytes().toString();
		
		try {
			return parseJsonResult(output);
		} catch (JSONException e) {
			throw new StatusException("Error parsing JSON output: ", e);
		}
	}
	
	protected GoOracleFindDefinitionResult parseJsonResult(String output) throws JSONException, StatusException {
		JSONObject jsonResult = new JSONObject(output);
		
		JSONObject describe = jsonResult.getJSONObject("describe");
		
		String desc = describe.getString("desc");
		if(!areEqual(desc, "identifier")) {
			throw new StatusException("Selection is not an identifier", null);
		}
		
		JSONObject value = describe.getJSONObject("value");
		
		String pathStr = value.getString("objpos");
		// We will need to parse objpos from the end, because on Windows the filePath can contain the ':' char
		
		String columnStr = StringUtil.segmentAfterLastMatch(pathStr, ":");
		pathStr = StringUtil.substringUntilLastMatch(pathStr, ":");

		String lineStr = StringUtil.segmentAfterLastMatch(pathStr, ":");
		pathStr = StringUtil.substringUntilLastMatch(pathStr, ":");
		
		
		if(columnStr == null || lineStr == null) {
			throw new StatusException("No line or column position given.", null);
		}
		int line = parseInt(lineStr, "Invalid number for line: " + lineStr);
		int column = parseInt(columnStr, "Invalid number for column: " + columnStr);
		
		Path path = parsePath(pathStr);
		
		return new GoOracleFindDefinitionResult(path, line, column);
	}
	
	public class GoOracleFindDefinitionResult {
		
		public final Path path;
		public final int line; // 1-based index
		public final int column; // 1-based index
		
		public GoOracleFindDefinitionResult(Path path, int line, int column) {
			this.path = path;
			this.line = line;
			this.column = column;
		}
		
		public int getLineIndex() {
			return line - 1;
		}
		
		public int getColumnIndex() {
			return column - 1;
		}
		
	}
	
}