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
package melnorme.utilbox.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.core.Assert.AssertFailedException;
import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.core.fntypes.RunnableX;

/**
 * An extension to {@link FutureTask}, implementing {@link BasicFuture}. 
 */
public class FutureTaskX<RET> extends FutureTask<RET> implements Future2<RET> {

	/* FIXME: review interaction with OperationMonitor */
	
	public FutureTaskX(RunnableX<RuntimeException> runnable) {
		this(() -> { runnable.run(); return null; });
	}
	
	public FutureTaskX(CallableX<RET, RuntimeException> callable) {
		super(callable);
	}
	
	public FutureTask<RET> asFutureTask() {
		return this;
	}
	
	@Override
	public boolean tryCancel() {
		return super.cancel(true);
	}
	
	public void before_cancel(@SuppressWarnings("unused") boolean mayInterruptIfRunning) {
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public RET awaitResult() throws OperationCancellation, InterruptedException {
		try {
			return get();
		} catch(CancellationException e) {
			throw new OperationCancellation();
		} catch(ExecutionException e) {
			throw rethrowCause(e);
		}
	}
	
	@Override
	public RET awaitResult(long timeout, TimeUnit unit)
			throws OperationCancellation, InterruptedException, TimeoutException {
		try {
			return get(timeout, unit);
		} catch(CancellationException e) {
			throw new OperationCancellation();
		} catch(ExecutionException e) {
			throw rethrowCause(e);
		}
	}
	
	protected AssertFailedException rethrowCause(ExecutionException e) {
		Throwable cause = e.getCause();
		if(cause instanceof RuntimeException) {
			throw (RuntimeException) cause;
		}
		// it can only be a RuntimeException
		throw assertFail(); 
	}
	
	/* -----------------  ----------------- */
	
	public FutureTaskX<RET> submitTo(Executor executor) {
		return ThreadPoolExecutorExt.submitTo(executor, this);
	}
	
}