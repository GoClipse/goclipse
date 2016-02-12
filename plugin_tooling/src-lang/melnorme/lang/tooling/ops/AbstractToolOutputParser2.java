/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.tooling.ops;


import melnorme.lang.tooling.data.InfoResult;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolOutputParser2<RESULT> extends ToolOutputParseHelper {
	
	protected final String toolPath;
	protected final boolean nonZeroExitIsFatal;
	
	public AbstractToolOutputParser2(String toolPath, boolean nonZeroResultIsFatal) {
		super();
		this.toolPath = toolPath;
		this.nonZeroExitIsFatal = nonZeroResultIsFatal;
	}
	
	public FindDefinitionResult execute(IProcessRunner opRunner,
			ICancelMonitor cm) throws OperationCancellation, CommonException, InfoResult {
		ProcessBuilder pb = createProcessBuilder();
		
		ExternalProcessResult result = opRunner.runProcess(pb, null, cm);
		if(result.exitValue != 0) {
			handleNonZeroExitValue(result);
		}
		
		return parseToolResult(result);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
	@SuppressWarnings("unused")
	protected void handleNonZeroExitValue( ExternalProcessResult result) throws CommonException, InfoResult {
		String nonZeroExitMsg = getToolName() + " did not complete successfully.";
		if(nonZeroExitIsFatal) {
			throw new CommonException(nonZeroExitMsg);
		} else {
			throw new InfoResult(nonZeroExitMsg);
		}
	}
	
	protected abstract String getToolName();
	
	public FindDefinitionResult parseToolResult(ExternalProcessResult result) throws CommonException {
		return parseToolResult(result.getStdOutBytes().toString());
	}
	
	public abstract FindDefinitionResult parseToolResult(String output) throws CommonException;
	
}