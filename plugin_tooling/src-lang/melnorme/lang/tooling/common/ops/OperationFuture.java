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

import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;

/**
 * A {@link IRunnableFuture2} derived from an {@link ResultOperation}.
 * Has special integration to cancel the operation monitor if the future is cancelled.
 *
 */
public class OperationFuture<RET> extends MonitorRunnableFuture<OperationResult<RET>> {
	
	public static <RET> OperationFuture<RET> fromResultOperation(
		IOperationMonitor om, ResultOperation<RET> resultOperation
	) {
		return new OperationFuture<>(resultOperation, om);
	}
	
	public static OperationFuture<Void> fromOperation(
		IOperationMonitor om, Operation resultOperation
	) {
		return new OperationFuture<>(resultOperation, om);
	}
	
	/* -----------------  ----------------- */

	protected final ResultOperation<RET> resultOperation;
	protected final BiDelegatingOperationMonitor om;
	
	public OperationFuture(ResultOperation<RET> resultOperation, IOperationMonitor om) {
		this(resultOperation, new BiDelegatingOperationMonitor(om));
	}
	
	public OperationFuture(ResultOperation<RET> resultOperation, BiDelegatingOperationMonitor dom) {
		super(dom.secondCancelMonitor);
		this.resultOperation = assertNotNull(resultOperation);
		this.om = assertNotNull(dom);
	}
	
	public IOperationMonitor getOperationMonitor() {
		return om;
	}
	
	@Override
	protected OperationResult<RET> internalInvoke() {
		OperationCallable<RET> toResult = () -> resultOperation.callOp(om);
		return OperationResult.callToOpResult(toResult);
	}
	
}