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

import java.text.MessageFormat;

import melnorme.lang.tooling.completion.CompletionSoftFailure;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.ToolOutputParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.goclipse.tooling.GoPackageName;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

public class GoOracleFindDefinitionOperation extends ToolOutputParseHelper {
	
	protected final String goOraclePath;
	
	public GoOracleFindDefinitionOperation(String goOraclePath) {
		this.goOraclePath = goOraclePath;
	}
	
	public ProcessBuilder createProcessBuilder(GoEnvironment goEnv, Location fileLoc, int offset) 
			throws CommonException {
		GoPackageName goPackage = goEnv.findGoPackageForSourceModule(fileLoc);
		if(goPackage == null) {
			throw new CommonException(MessageFormat.format(
				"Could not determine Go package for Go file ({0}), file not in the Go environment.", fileLoc), 
				null);
		}
		
		// go oracle requires these variables are properly set
		goEnv.validateGoArch();
		goEnv.validateGoOs();
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			goOraclePath,
			"-pos=" + fileLoc.toPathString() + ":#" + offset + ",#" + offset,
			"-format=json",
			"describe",
			goPackage.getFullNameAsString()
		);
		
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
	public FindDefinitionResult parseToolResult(ExternalProcessResult result) throws CommonException {
		if(result.exitValue != 0) {
			throw new CommonException("Program exited with non-zero status: " + result.exitValue, null);
		}
		
		return parseJsonResult(result.getStdOutBytes().toString());
	}
	
	protected FindDefinitionResult parseJsonResult(String output) throws CommonException {
		try {
			return doParseJsonResult(output);
		} catch (JSONException e) {
			throw new CommonException("Error parsing JSON output: ", e);
		} catch (CompletionSoftFailure sf) {
			return new FindDefinitionResult(sf.getMessage());
		}
	}
	
	protected FindDefinitionResult doParseJsonResult(String output) 
			throws JSONException, CommonException, CompletionSoftFailure {
		JSONObject jsonResult = new JSONObject(output);
		
		JSONObject describe = jsonResult.getJSONObject("describe");
		
		String desc = describe.getString("desc");
		String detail = describe.getString("detail");
		
		if(areEqual(desc, "source file")) {
			return new FindDefinitionResult(null, null);
		}
		
		if(areEqual(desc, "identifier")) {
			JSONObject value = describe.getJSONObject("value");
			String pathStr = getString(value, "objpos", "Definition not available.");
			return new FindDefinitionResult(null, parsePathLineColumn(pathStr, ":"));
		}
		if(areEqual(detail, "type")) {
			final String DEFINITION_OF = "definition of ";
			
			if(desc != null && desc.startsWith(DEFINITION_OF)) {
				desc = StringUtil.segmentAfterMatch(desc, DEFINITION_OF);
				return new FindDefinitionResult("Already at a definition: " + desc);
			}
			JSONObject value = describe.getJSONObject("type");
			String pathStr = getString(value, "namepos", "Definition not available.");
			return new FindDefinitionResult(null, parsePathLineColumn(pathStr, ":"));
		}
		
		return new FindDefinitionResult(
				"Selected name does not refer to a source element, rather it's a:\n" + desc);
		
	}
	
	protected String getString(JSONObject value, String key, String resultErrorMessage) throws CompletionSoftFailure {
		String pathStr = getStringOrNull(value, key);
		if(pathStr == null) {
			throw new CompletionSoftFailure(resultErrorMessage);
		}
		return pathStr;
	}
	
	protected String getStringOrNull(JSONObject value, String key) {
		if(value.has(key)) {
			try {
				return value.getString(key);
			} catch (JSONException e) {
				return null;
			}
		}
		return null;
	}
	
}