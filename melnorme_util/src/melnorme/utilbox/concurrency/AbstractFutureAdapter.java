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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * Adapt a {@link Future} with the {@link Future2} interface
 * 
 */
public abstract class AbstractFutureAdapter<VALUE, RESULT> implements Future2<RESULT> {
	
	protected final Future<VALUE> future;
	protected volatile boolean hasStarted;
	
	public AbstractFutureAdapter(Future<VALUE> future) {
		this.future = assertNotNull(future);
	}
	
	public Future<VALUE> asStdLibFuture() {
		return future;
	}
	
	@Override
	public boolean isTerminated() {
		return future.isDone();
	}
	
	@Override
	public boolean isCancelled() {
		return future.isCancelled();
	}
	
	@Override
	public boolean tryCancel() {
		return future.cancel(true);
	}
	
	@Override
	public void awaitTermination() throws InterruptedException {
		try {
			awaitTermination(-1, null);
		} catch(TimeoutException e) {
			throw assertFail();
		}
	}
	
	@Override
	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		try {
			if(timeout == -1) {
				future.get();
			} else {
				future.get(timeout, unit);
			}
		} catch(ExecutionException | CancellationException e) {
			// Ignore
		}
	}
	
	@Override
	public RESULT getResult_forSuccessfulyCompleted() {
		assertTrue(isCompletedSuccessfully());
		VALUE resultValue;
		try {
			try {
				resultValue = future.get();
			} catch(InterruptedException e) {
				throw assertFail("Not completed");
			}
		} catch(ExecutionException e) {
			return createExceptionResult(e);
		}
		return createResult(resultValue);
	}
	
	protected abstract RESULT createResult(VALUE resultValue);
	
	protected abstract RESULT createExceptionResult(ExecutionException e);
	
}