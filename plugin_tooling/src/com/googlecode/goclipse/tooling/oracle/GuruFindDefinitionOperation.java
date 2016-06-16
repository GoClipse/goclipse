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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.json.JSONException;
import org.json.JSONObject;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GuruFindDefinitionOperation extends GuruDescribeOperation implements AbstractToolOperation<SourceLocation> {
	
	protected final GoOperationContext goOperationContext;
	
	public GuruFindDefinitionOperation(GoOperationContext goOperationContext, String goOraclePath) {
		super(goOraclePath);
		this.goOperationContext = assertNotNull(goOperationContext);
	}
	
	@Override
	public SourceLocation executeToolOperation(IOperationMonitor om) 
			throws CommonException, OperationCancellation, OperationSoftFailure {
		Location inputLoc = goOperationContext.getFileLocation();
		int byteOffset = goOperationContext.getByteOffsetFromEncoding(goOperationContext.opContext.getOffset());
		
		GoEnvironment goEnv = goOperationContext.goEnv;
		ProcessBuilder pb = createProcessBuilder(goEnv, inputLoc, byteOffset);
		
		ExternalProcessResult result = goOperationContext.getToolOpService().runProcess(pb, null, om);
		if(result.exitValue != 0) {
			throw new OperationSoftFailure("`guru` did not complete successfully.");
		}
		
		return parseToolResult(result);
	}
	
	public SourceLocation parseToolResult(ExternalProcessResult result) throws CommonException {
		if(result.exitValue != 0) {
			throw new CommonException("Program exited with non-zero status: " + result.exitValue, null);
		}
		
		String resultMessage = result.getStdOutBytes().toString();
		return new GuruFindDefinitionResultParser().parseJsonResult(resultMessage);
	}
	
	public static class GuruFindDefinitionResultParser {
	
	protected SourceLocation parseJsonResult(String output) throws CommonException {
		try {
			return assertNotNull(doParseJsonResult(output));
		} catch(JSONException e) {
			throw new CommonException("Error parsing JSON output: ", e);
		} catch(OperationSoftFailure sf) {
			throw new CommonException(sf.getMessage());
		}
	}
	
	protected SourceLocation doParseJsonResult(String output) 
			throws JSONException, CommonException, OperationSoftFailure {
		JSONObject describe = new JSONObject(output);
		
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

}