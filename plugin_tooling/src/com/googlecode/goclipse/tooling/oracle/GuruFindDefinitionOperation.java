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

import com.google.gson.JsonObject;
import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.toolchain.ops.AbstractToolOperation;
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.lang.utils.gson.GsonHelper;
import melnorme.lang.utils.gson.JsonParserX;
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
		
	protected final GsonHelper helper = new GsonHelper();
	
	protected SourceLocation parseJsonResult(String output) throws CommonException {
		try {
			return assertNotNull(doParseJsonResult(output));
		} catch(OperationSoftFailure sf) {
			throw new CommonException(sf.getMessage());
		}
	}
	
	protected SourceLocation doParseJsonResult(String output) 
			throws CommonException, OperationSoftFailure {
		JsonObject describe = new JsonParserX().parseObject(output, true);
		
		String desc = helper.getString(describe, "desc");
		String detail = helper.getString(describe, "detail");
		
		if(areEqual(desc, "source file")) {
			return null;
		}
		
		if(areEqual(desc, "identifier")) {
			JsonObject value = helper.getObject(describe, "value");
			String sourceLocStr = getString(value, "objpos", "Definition not available.");
			return ToolOutputParseHelper.parsePathLineColumn(sourceLocStr, ":");
		}
		if(areEqual(detail, "type")) {
			final String DEFINITION_OF = "definition of ";
			
			if(desc != null && desc.startsWith(DEFINITION_OF)) {
				desc = StringUtil.segmentAfterMatch(desc, DEFINITION_OF);
				throw new CommonException("Already at a definition: " + desc);
			}
			JsonObject value = helper.getObject(describe, "type");
			String sourceLocStr = getString(value, "namepos", "Definition not available.");
			return ToolOutputParseHelper.parsePathLineColumn(sourceLocStr, ":");
		}
		
		throw new CommonException(
			"Selected position does not refer to a definition. Rather, it's a:\n" + desc);
	}
	
	protected String getString(JsonObject value, String key, String resultErrorMessage) throws OperationSoftFailure {
		String pathStr = getStringOrNull(value, key);
		if(pathStr == null) {
			throw new OperationSoftFailure(resultErrorMessage);
		}
		return pathStr;
	}
	
	protected String getStringOrNull(JsonObject element, String key) {
		try {
			return helper.getOptionalString(element, key);
		} catch(CommonException e) {
			return null;
		}
	}

	}

}