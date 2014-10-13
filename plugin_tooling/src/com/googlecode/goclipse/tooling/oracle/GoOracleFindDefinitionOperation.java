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
import java.text.MessageFormat;

import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.SourceLineColumnLocation;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.goclipse.tooling.JsonDeserializeHelper;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoOracleFindDefinitionOperation extends JsonDeserializeHelper {
	
	protected final String goOraclePath;
	
	public GoOracleFindDefinitionOperation(String goOraclePath) {
		this.goOraclePath = goOraclePath;
	}
	
	public ProcessBuilder createProcessBuilder(GoEnvironment goEnv, Path filePath, int offset) throws CommonException {
		Path goPackage = goEnv.getGoPackageFromSourceModule(filePath);
		if(goPackage == null) {
			throw new CommonException(MessageFormat.format(
				"Could not determine Go package for Go file ({0}), file not in the Go environment.", filePath), 
				null);
		}
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			goOraclePath,
			"-pos=" + filePath.toString() + ":#" + offset + ",#" + offset,
			"-format=json",
			"describe",
			goPackage.toString()
		);
		
		return goEnv.createProcessBuilder(commandLine);
	}
	
	public FindDefinitionResult parseJsonResult(ExternalProcessResult result) throws CommonException {
		if(result.exitValue != 0) {
			throw new CommonException("Program exited with non-zero status: " + result.exitValue, null);
		}
		
		String output = result.getStdOutBytes().toString();
		
		try {
			return parseJsonResult(output);
		} catch (JSONException e) {
			throw new CommonException("Error parsing JSON output: ", e);
		}
	}
	
	protected FindDefinitionResult parseJsonResult(String output) throws JSONException, CommonException {
		JSONObject jsonResult = new JSONObject(output);
		
		JSONObject describe = jsonResult.getJSONObject("describe");
		
		String desc = describe.getString("desc");
		
		if(areEqual(desc, "source file")) {
			return new FindDefinitionResult(null, null);
		}
		if(!areEqual(desc, "identifier")) {
			return new FindDefinitionResult(
				"Selected name does not refer to a source element, rather it's a:\n" + desc, null);
		}
		
		
		JSONObject value = describe.getJSONObject("value");
		
		String pathStr = value.getString("objpos");
		// We will need to parse objpos from the end, because on Windows the filePath can contain the ':' char
		
		String columnStr = StringUtil.segmentAfterLastMatch(pathStr, ":");
		pathStr = StringUtil.substringUntilLastMatch(pathStr, ":");

		String lineStr = StringUtil.segmentAfterLastMatch(pathStr, ":");
		pathStr = StringUtil.substringUntilLastMatch(pathStr, ":");
		
		
		if(columnStr == null || lineStr == null) {
			throw new CommonException("No line or column position given.", null);
		}
		int line = parseInt(lineStr, "Invalid number for line: " + lineStr);
		int column = parseInt(columnStr, "Invalid number for column: " + columnStr);
		
		Path path = parsePath(pathStr);
		
		return new FindDefinitionResult(null, new SourceLineColumnLocation(path, line, column));
	}
	
}