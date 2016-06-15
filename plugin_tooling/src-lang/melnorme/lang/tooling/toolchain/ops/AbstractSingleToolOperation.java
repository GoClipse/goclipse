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

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.Severity;
import melnorme.utilbox.status.StatusMessage;

@Deprecated
public abstract class AbstractSingleToolOperation<RESULT> extends AbstractToolOperation2<RESULT> 
	implements ResultOperation<ToolResponse<RESULT>> {
	
	protected final IToolOperationService opHelper;
	protected final String toolPath;
	protected final boolean nonZeroExitIsFatal;

	protected String toolInput = "";
	
	public AbstractSingleToolOperation(IToolOperationService opHelper, String toolPath, boolean nonZeroResultIsFatal) {
		this.opHelper = assertNotNull(opHelper);
		this.toolPath = assertNotNull(toolPath);
		this.nonZeroExitIsFatal = nonZeroResultIsFatal;
	}
	
	@Override
	public final ToolResponse<RESULT> executeOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		return execute(om);
	}
	
	public ToolResponse<RESULT> execute(ICancelMonitor cm) throws CommonException, OperationCancellation {
		ProcessBuilder pb = createProcessBuilder();
		ExternalProcessResult result = opHelper.runProcess(pb, toolInput, cm);
		return handleProcessResult(result);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
	@Override
	public ToolResponse<RESULT> handleProcessResult(ExternalProcessResult result)
			throws CommonException, OperationCancellation {
		if(!nonZeroExitIsFatal) {
			try {
				validateExitCode(result);
			} catch(CommonException e) {
				return new ToolResponse<>(null, new StatusMessage(Severity.ERROR, e.getMessage()));
			}
		}
		return super.handleProcessResult(result);
	}
	
	protected abstract String getToolName();

	
}