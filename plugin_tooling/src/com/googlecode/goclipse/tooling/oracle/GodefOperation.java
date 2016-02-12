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

import melnorme.lang.tooling.data.InfoResult;
import melnorme.lang.tooling.ops.AbstractToolOutputParser2;
import melnorme.lang.tooling.ops.FindDefinitionResult;
import melnorme.lang.tooling.ops.ToolOutputParseHelper;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class GodefOperation extends AbstractToolOutputParser2<FindDefinitionResult> {
	
	protected GoEnvironment goEnv;
	protected Location fileLocation;
	protected int byteOffset;
	
	public GodefOperation(String godefPath, GoEnvironment goEnv, Location fileLocation, int byteOffset) {
		super(godefPath, false);
		this.goEnv = assertNotNull(goEnv);
		this.fileLocation = fileLocation;
		this.byteOffset = byteOffset;
	}
	
	@Override
	protected String getToolName() {
		return "godef";
	}
	
	@Override
	protected ProcessBuilder createProcessBuilder() throws CommonException {
		Location toolLoc = Location.create(toolPath);
		ArrayList2<String> commandLine = new ArrayList2<>(
			toolLoc.toPathString(),
			"-f", fileLocation.toPathString(),
			"-o", Integer.toString(byteOffset)
		);
		return goEnv.createProcessBuilder(commandLine, null, true);
	}
	
	@Override
	protected void handleNonZeroExitValue(ExternalProcessResult result) throws CommonException, InfoResult {
		String errOut = result.getStdErrBytes().toString(StringUtil.UTF8);
		if(errOut.trim().contains("\n")) {
			super.handleNonZeroExitValue(result);
			return;
		}
		throw new InfoResult(errOut);
	}
	
	@Override
	public FindDefinitionResult parseToolResult(String output) throws CommonException {
		output = output.trim();
		return ToolOutputParseHelper.parsePathLineColumn(output, ":");
	}
	
}