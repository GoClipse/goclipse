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

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.tooling.toolchain.ops.AbstractToolInvocationOperation;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.lang.tooling.toolchain.ops.ToolResponse;
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.StatusException;

public class GodefOperation extends AbstractToolInvocationOperation<SourceLocation, ToolResponse<SourceLocation>> {
	
	protected GoOperationContext goOpContext;
	protected GoEnvironment goEnv;
	protected SourceOpContext opContext;
	
	public GodefOperation(GoOperationContext goOpContext, String godefPath) {
		super(goOpContext.getToolOpService(), godefPath, false);
		this.goOpContext = assertNotNull(goOpContext);
		this.goEnv = assertNotNull(goOpContext.getGoEnv());
		this.opContext = assertNotNull(goOpContext.opContext);
	}
	
	@Override
	protected ProcessBuilder createProcessBuilder() throws CommonException {
		Location toolLoc = Location.create(toolPath);
		int byteOffset = goOpContext.getByteOffsetFromEncoding(goOpContext.opContext.getOffset());
		
		ArrayList2<String> commandLine = new ArrayList2<>(
			toolLoc.toPathString(),
			/* FIXME: review this*/
			"-f", opContext.getFileLocation().toPathString(),
			"-i",
			"-o", Integer.toString(byteOffset)
		);
		toolInput = opContext.getSource();
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
	@Override
	protected ToolResponse<SourceLocation> doHandleProcessResult(ExternalProcessResult result, ProcessBuilder pb)
			throws StatusValidation, CommonException {
		if(result.exitValue != 0) {
			String errOut = result.getStdErrBytes().toString(StringUtil.UTF8);
			if(!errOut.trim().contains("\n")) {
				return new ToolResponse<>(null, new StatusException(errOut));
			}
		}
		
		return super.doHandleProcessResult(result, pb);
	}
	
	@Override
	public ToolResponse<SourceLocation> parseProcessOutput(StringCharSource output) throws CommonException {
		SourceLocation findDefResult = ToolOutputParseHelper.parsePathLineColumn(output.getSource().trim(), ":");
		return new ToolResponse<>(findDefResult);
	}
	
	@Override
	protected ToolResponse<SourceLocation> createErrorResponse(String errorMessage) {
		return createErrorToolResponse(errorMessage);
	}
	
}