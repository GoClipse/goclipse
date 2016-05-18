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
package com.googlecode.goclipse.tooling.oracle;

import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.common.ops.IProcessRunner;
import melnorme.lang.tooling.toolchain.ops.FindDefinitionResult;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GoOracleFindDefinitionOperation extends GoOracleDescribeOperation {
	
	public GoOracleFindDefinitionOperation(String goOraclePath) {
		super(goOraclePath);
	}
	
	public FindDefinitionResult execute(Location inputLoc, int byteOffset, GoEnvironment goEnv, IProcessRunner opRunner,
			ICancelMonitor cm) throws OperationCancellation, CommonException, OperationSoftFailure {
		ProcessBuilder pb = createProcessBuilder(goEnv, inputLoc, byteOffset);
		
		ExternalProcessResult result = opRunner.runProcess(pb, null, cm);
		if(result.exitValue != 0) {
			throw new OperationSoftFailure("Go oracle did not complete successfully.");
		}
		
		return parseToolResult(result);
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
		} catch(JSONException e) {
			throw new CommonException("Error parsing JSON output: ", e);
		} catch(OperationSoftFailure sf) {
			throw new CommonException(sf.getMessage());
		}
	}
	
	protected FindDefinitionResult doParseJsonResult(String output) 
			throws JSONException, CommonException, OperationSoftFailure {
		JSONObject jsonResult = new JSONObject(output);
		
		JSONObject describe = jsonResult.getJSONObject("describe");
		
		String desc = describe.getString("desc");
		String detail = describe.getString("detail");
		
		if(areEqual(desc, "source file")) {
			return null;
		}
		
		if(areEqual(desc, "identifier")) {
			JSONObject value = describe.getJSONObject("value");
			String sourceLocStr = getString(value, "objpos", "Definition not available.");
			return ToolOutputParseHelper.parsePathLineColumn(sourceLocStr, ":");
		}
		if(areEqual(detail, "type")) {
			final String DEFINITION_OF = "definition of ";
			
			if(desc != null && desc.startsWith(DEFINITION_OF)) {
				desc = StringUtil.segmentAfterMatch(desc, DEFINITION_OF);
				throw new CommonException("Already at a definition: " + desc);
			}
			JSONObject value = describe.getJSONObject("type");
			String sourceLocStr = getString(value, "namepos", "Definition not available.");
			return ToolOutputParseHelper.parsePathLineColumn(sourceLocStr, ":");
		}
		
		throw new CommonException(
			"Selected position does not refer to a definition. Rather, it's a:\n" + desc);
	}
	
	protected String getString(JSONObject value, String key, String resultErrorMessage) throws OperationSoftFailure {
		String pathStr = getStringOrNull(value, key);
		if(pathStr == null) {
			throw new OperationSoftFailure(resultErrorMessage);
		}
		return pathStr;
	}
	
	protected String getStringOrNull(JSONObject value, String key) {
		if(value.has(key)) {
			try {
				return value.getString(key);
			} catch(JSONException e) {
				return null;
			}
		}
		return null;
	}
	
}