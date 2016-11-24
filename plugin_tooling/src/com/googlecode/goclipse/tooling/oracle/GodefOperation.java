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
import melnorme.lang.tooling.toolchain.ops.OperationSoftFailure;
import melnorme.lang.tooling.toolchain.ops.SourceLocation;
import melnorme.lang.tooling.toolchain.ops.SourceOpContext;
import melnorme.lang.tooling.toolchain.ops.ToolOutputParseHelper;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GodefOperation extends AbstractToolInvocationOperation<SourceLocation> {
	
	protected GoOperationContext goOpContext;
	protected GoEnvironment goEnv;
	protected SourceOpContext opContext;
	
	public GodefOperation(GoOperationContext goOpContext, String godefPath) {
		super(goOpContext.getToolOpService(), godefPath);
		this.goOpContext = assertNotNull(goOpContext);
		this.goEnv = assertNotNull(goOpContext.getGoEnv());
		this.opContext = assertNotNull(goOpContext.opContext);
	}
	
	@Override
	protected String getToolName() throws CommonException {
		return "godef";
	}
	
	@Override
	protected ProcessBuilder createProcessBuilder() throws CommonException {
		Location toolLoc = Location.create(toolPath);
		int byteOffset = goOpContext.getByteOffsetFromEncoding(goOpContext.opContext.getOffset());
		
		ArrayList2<String> commandLine = ArrayList2.create(
			toolLoc.toPathString(),
			"-f", opContext.getFileLocation().toPathString(),
			"-i",
			"-o", Integer.toString(byteOffset)
		);
		toolInput = opContext.getSource();
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
	@Override
	protected void handleNonZeroExitCode(ExternalProcessResult result) throws OperationSoftFailure {
		String errOut = result.getStdErrBytes().toString(StringUtil.UTF8);
		if(!errOut.trim().contains("\n")) {
			throw new OperationSoftFailure(errOut);
		}
	}
	
	@Override
	public SourceLocation parseOutput(StringCharSource output) throws CommonException, OperationSoftFailure {
		int char0 = output.lookahead(0);
		int char1 = output.lookahead(1);
		if(char0 == '-' && (char1 == -1 || char1 == '\n')) {
			throw new OperationSoftFailure("Target element is not defined in any source file.");
		}
		return ToolOutputParseHelper.parsePathLineColumn(output.getSource().trim(), ":");
	}
	
}