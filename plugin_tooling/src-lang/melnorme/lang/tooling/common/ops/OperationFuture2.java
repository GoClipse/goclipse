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
package melnorme.lang.tooling.common.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.MonitorTaskFuture;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;

/**
 * An alternative version of {@link OperationFuture}.
 * 
 * A task future that is completed by executing {@link #execute(IOperationMonitor)}.
 * Has special integration to cancel the operation monitor if the future is cancelled.
 *
 */
public class OperationFuture2<RET> extends MonitorTaskFuture<OperationResult<RET>>
	implements ResultOperation<RET>
{
	
	public static <RET> OperationFuture2<RET> fromResultOperation(ResultOperation<RET> resultOperation) {
		return new OperationFuture2<>(resultOperation);
	}
	
	public static OperationFuture2<Void> fromOperation(Operation operation) {
		return new OperationFuture2<>(operation);
	}
	
	/* -----------------  ----------------- */

	protected final ResultOperation<RET> resultOperation;
	
	public OperationFuture2(ResultOperation<RET> resultOperation) {
		super();
		this.resultOperation = assertNotNull(resultOperation);
	}
	
	protected BiDelegatingOperationMonitor om;
	
	@Override
	public RET callOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		return executeToFutureResult(om).get();
	}
	
	public OperationResult<RET> executeToFutureResult(IOperationMonitor om) {
		this.om = new BiDelegatingOperationMonitor(om, getCancelMonitor());
		runFuture();
		try {
			return getResult_forTerminated();
		} catch(OperationCancellation e) {
			return OperationResult.cancellationResult();
		}
	}
	
	@Override
	protected void runFuture() {
		assertNotNull(om);
		super.runFuture();
	}
	
	@Override
	protected OperationResult<RET> internalInvoke() {
		assertNotNull(om);
		OperationCallable<RET> toResult = () -> resultOperation.callOp(om);
		return OperationResult.callToOpResult(toResult);
	}
	
}