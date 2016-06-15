/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.toolchain.ops;


import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolResultParser<RESULT> {
	
	public AbstractToolResultParser() {
		super();
	}
	
	public ToolResponse<RESULT> parseResult(ExternalProcessResult result) {
		try {
			return new ToolResponse<>(doParseResult(result));
		} catch(StatusValidation e) {
			return new ToolResponse<>(null, e);
		} catch(CommonException e) {
			return ToolResponse.newError(e.getMultiLineRender());
		}
	}
	
	public RESULT doParseResult(ExternalProcessResult result) throws StatusValidation, CommonException {
		validateExitCode(result);
		
		return parseOutput(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	protected void validateExitCode(ExternalProcessResult result) throws CommonException, StatusValidation {
		if(result.exitValue != 0) {
			handleNonZeroExitCode(result);
		}
	}
	
	protected void handleNonZeroExitCode(ExternalProcessResult result) throws CommonException, StatusValidation {
		throw new CommonException(
			ToolingMessages.PROCESS_CompletedWithNonZeroValue(getToolName(), result.exitValue));
	}
	
	protected abstract String getToolName() throws CommonException;
	
	public RESULT parseOutput(String output) throws StatusValidation, CommonException {
		return parseOutput(new StringCharSource(output));
	}
	
	public abstract RESULT parseOutput(StringCharSource output) throws StatusValidation, CommonException;
	
}