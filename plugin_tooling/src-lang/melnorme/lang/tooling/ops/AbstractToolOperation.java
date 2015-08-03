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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.lang.tooling.ToolingMessages;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;


public abstract class AbstractToolOperation {
	
	protected final IOperationHelper operationHelper;
	
	public AbstractToolOperation(IOperationHelper opHelper) {
		this.operationHelper = assertNotNull(opHelper);
	}
	
	protected ExternalProcessResult runToolProcess2(ProcessBuilder pb, String input) 
			throws CommonException, OperationCancellation {
		return operationHelper.runProcess(pb, input);
	}
	
	protected ExternalProcessResult runToolProcess_validateExitValue(ProcessBuilder pb, String input)
			throws CommonException, OperationCancellation, OperationSoftFailure {
		ExternalProcessResult runProcess = operationHelper.runProcess(pb, input);
		if(runProcess.exitValue != 0) {
			throw new OperationSoftFailure(
				ToolingMessages.PROCESS_CompletedWithNonZeroVAlue(getToolProcessName(), runProcess.exitValue));
		}
		return runProcess;
	}
	
	protected abstract String getToolProcessName();
	
	public IOperationHelper getOperationHelper() {
		return operationHelper;
	}
	
}