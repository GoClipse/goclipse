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
import melnorme.lang.tooling.toolchain.ops.ToolResponse.StatusValidation;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.status.StatusMessage;

public abstract class AbstractToolInvocationOperation<RESULTDATA, RESPONSE extends ToolResponse<RESULTDATA>>
	extends AbstractToolResultParser<RESULTDATA>
	implements ResultOperation<RESPONSE> {
	
	protected final IToolOperationService opHelper;
	protected final String toolPath;

	protected String toolInput = "";
	
	public AbstractToolInvocationOperation(IToolOperationService opHelper, String toolPath) {
		this.opHelper = assertNotNull(opHelper);
		this.toolPath = assertNotNull(toolPath);
	}
	
	@Override
	public final RESPONSE executeOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		return execute(om);
	}
	
	public RESPONSE execute(ICancelMonitor cm) throws CommonException, OperationCancellation {
		ProcessBuilder pb = createProcessBuilder();
		ExternalProcessResult result = opHelper.runProcess(pb, toolInput, cm);
		return parseProcessResultToResponse(result);
	}
	
	protected abstract ProcessBuilder createProcessBuilder() throws CommonException;
	
	public RESPONSE parseProcessResultToResponse(ExternalProcessResult result) {
		try {
			return createToolResponse(doParseResult(result), null);
		} catch(StatusValidation e) {
			return createToolResponse(null, e.getMessage());
		} catch(CommonException e) {
			return createToolResponse(null, e.getSingleLineRender());
		}
	}
	
	protected abstract RESPONSE createToolResponse(RESULTDATA resultData, String errorMessage);
	
	protected ToolResponse<RESULTDATA> createDefaultToolResponse(RESULTDATA resultData, String errorMessage) {
		return new ToolResponse<RESULTDATA>(resultData, new StatusMessage(errorMessage));
	}
	
}