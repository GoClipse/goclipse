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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.tooling.ToolingMessages;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.lang.utils.parse.StringCharSource;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.PathUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusMessage;

public abstract class AbstractToolInvocationOperation<RESULTDATA, RESPONSE extends ToolResponse<RESULTDATA>> 
	implements ResultOperation<RESPONSE> {
	
	protected final IToolOperationService opHelper;
	protected final String toolPath;
	protected final boolean nonZeroExitIsFatal;

	protected String toolInput = "";
	
	public AbstractToolInvocationOperation(IToolOperationService opHelper, String toolPath, 
			boolean nonZeroResultIsFatal) {
		this.opHelper = assertNotNull(opHelper);
		this.toolPath = assertNotNull(toolPath);
		this.nonZeroExitIsFatal = nonZeroResultIsFatal;
	}
	
	@Override
	public final RESPONSE executeOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		return execute(om);
	}
	
	public RESPONSE execute(ICancelMonitor cm) throws CommonException, OperationCancellation {
		ProcessBuilder pb = createProcessBuilder();
		ExternalProcessResult result = opHelper.runProcess(pb, toolInput, cm);
		return handleProcessResult(result, pb);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
	public RESPONSE handleProcessResult(ExternalProcessResult result, ProcessBuilder pb)
			throws CommonException, OperationCancellation {
		try {
			return doHandleProcessResult(result, pb);
		} catch(StatusValidation e) {
			return createErrorResponse(e.getMessage());
		}
	}
	
	protected abstract RESPONSE createErrorResponse(String errorMessage);
	
	protected ToolResponse<RESULTDATA> createErrorToolResponse(String errorMessage) {
		return new ToolResponse<>(null, new StatusMessage(Severity.ERROR, errorMessage));
	}
	
	protected RESPONSE doHandleProcessResult(ExternalProcessResult result, ProcessBuilder pb) 
			throws StatusValidation, CommonException {
		if(result.exitValue != 0) {
			handleNonzeroExit(result, pb);
		}
		return parseProcessOutput(result.getStdOutBytes().toString(StringUtil.UTF8));
	}
	
	protected void handleNonzeroExit(ExternalProcessResult result, ProcessBuilder pb) 
			throws StatusValidation, CommonException {
		String errorMsg = ToolingMessages.PROCESS_CompletedWithNonZeroValue(getToolName(pb), result.exitValue);
		if(nonZeroExitIsFatal) {
			throw new CommonException(errorMsg);
		} else {
			throw new StatusValidation(errorMsg);
		}
	}
	
	protected String getToolName(ProcessBuilder pb) throws CommonException {
		String commandExe = pb.command().get(0);
		return PathUtil.createPath(commandExe).getFileName().toString();
	}
	
	/* -----------------  ----------------- */
	
	public final RESPONSE parseProcessOutput(String source) throws CommonException {
		return parseProcessOutput(new StringCharSource(source));
	}
	
	public abstract RESPONSE parseProcessOutput(StringCharSource outputParseSource) throws CommonException;
	
}