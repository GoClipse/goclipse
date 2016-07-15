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

import melnorme.lang.tooling.common.ops.IOperationMonitor.DelegatingOperationMonitor;
import melnorme.lang.utils.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.RunnableFuture2;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;

/**
 * A {@link RunnableFuture2} derived from an {@link ResultOperation}.
 * Has special integration to cancel the operation monitor if the future is cancelled.
 *
 */
public class OperationFuture<RET> extends MonitorRunnableFuture<OperationResult<RET>> {
	
	protected final ResultOperation<RET> resultOperation;
	protected final BiDelegatingOperationMonitor om;
	
	public OperationFuture(ResultOperation<RET> resultOperation, IOperationMonitor om) {
		this(resultOperation, new BiDelegatingOperationMonitor(om));
	}
	
	public OperationFuture(ResultOperation<RET> resultOperation, BiDelegatingOperationMonitor dom) {
		super(dom.futureTaskMonitor);
		this.resultOperation = assertNotNull(resultOperation);
		this.om = assertNotNull(dom);
	}
	
	@Override
	protected OperationResult<RET> invokeToResult() {
		OperationCallable<RET> toResult = () -> resultOperation.callOp(om);
		return OperationResult.callToResult(toResult);
	}
	
	/* -----------------  ----------------- */
	
	public static class BiDelegatingOperationMonitor extends DelegatingOperationMonitor {
		
		protected final CancelMonitor futureTaskMonitor = new CancelMonitor();
		
		public BiDelegatingOperationMonitor(IOperationMonitor om) {
			super(om);
		}
		
		@Override
		public boolean isCanceled() {
			return super.isCanceled() || futureTaskMonitor.isCanceled();
		}
		
	}
	
}