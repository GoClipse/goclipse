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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A future meant to be completed (or cancelled) by an explicit call.
 * 
 * Similar to {@link CompletableFuture} but with a simplified API,
 * particularly the throws clause for methods that await a result: 
 * Here, the {@link #awaitResult()} method is preferable to {@link #get()}), 
 * since it doesn't throw an unchecked exception for cancellation.
 *
 */
public class ResultFuture<DATA, EXC extends Throwable> implements Future<DATA>, FutureX<DATA, EXC> {
	
	protected final CountDownLatch completionLatch = new CountDownLatch(1);
	protected final Object lock = new Object();
	
    protected volatile ResultStatus status = ResultStatus.INITIAL;
	protected volatile DATA resultValue;
	/** Note: resultException is either a EXC, or a RuntimeException. */
	protected volatile Throwable resultException;
	
	public enum ResultStatus { INITIAL, RESULT_SET, CANCELLED }
	
	public ResultFuture() {
		super();
	}
	
	@Override
	public boolean isDone() {
		return status != ResultStatus.INITIAL;
	}
	
	public boolean isCompleted() {
		return isDone();
	}
	
	@Override
	public boolean isCancelled() {
		return status == ResultStatus.CANCELLED;
	}
	
	public CountDownLatch getCompletionLatch() {
		return completionLatch;
	}
	
	public void setResult(DATA resultValue) {
		synchronized (lock) {
			if(isDone()) {
				handleReSetResult();
				return;
			}
			this.resultValue = resultValue;
			status = ResultStatus.RESULT_SET;
			completionLatch.countDown();
		}
	}
	
	public void setExceptionResult(EXC exceptionResult) {
		doSetExceptionResult(exceptionResult);
	}
	
	public void setRuntimeExceptionResult(RuntimeException exceptionResult) {
		doSetExceptionResult(exceptionResult);
	}
	
	protected void doSetExceptionResult(Throwable exceptionResult) {
		synchronized (lock) {
			if(isDone()) {
				handleReSetResult();
				return;
			}
			this.resultException = exceptionResult;
			status = ResultStatus.RESULT_SET;
			completionLatch.countDown();
		}
	}
	
	public boolean cancel() {
		synchronized (lock) {
			if(isDone()) {
				return false;
			} else {
				status = ResultStatus.CANCELLED;
				completionLatch.countDown();
				return true;
			}
		}
	}
	
	protected void handleReSetResult() {
		throw assertFail();
	}
	
	public void awaitCompletion() throws InterruptedException {
		completionLatch.await();
	}
	
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		boolean success = completionLatch.await(timeout, unit);
		if(!success) {
			throw new TimeoutException();
		}
	}
	
	@Override
	public DATA awaitResult() 
			throws EXC, OperationCancellation, InterruptedException {
		awaitCompletion();
		return getResult_afterCompletion();
	}
	
	@Override
	public DATA awaitResult(long timeout, TimeUnit unit) 
			throws EXC, OperationCancellation, InterruptedException, TimeoutException {
		awaitCompletion(timeout, unit);
		return getResult_afterCompletion();
	}
	
	protected DATA getResult_afterCompletion() throws EXC, OperationCancellation {
		if(isCancelled()) {
			throw new OperationCancellation();
		}
		throwIfExceptionResult();
		return resultValue;
	}
	
	@SuppressWarnings("unchecked")
	protected void throwIfExceptionResult() throws EXC {
		if(resultException instanceof RuntimeException) {
			throw (RuntimeException) resultException;
		}
		if(resultException != null) {
			throw (EXC) resultException;
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return cancel();
	}
	
	@Override
	public DATA get() throws InterruptedException, ExecutionException {
		try {
			return awaitResult();
		} catch(Throwable e) {
			throw toExecutionException(e);
		}
	}
	
	@Override
	public DATA get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			return awaitResult(timeout, unit);
		} catch(Throwable e) {
			throw toExecutionException(e);
		}
	}
	
	public ExecutionException toExecutionException(Throwable e) throws ExecutionException {
		// Don't throw java.util.concurrent.CancellationException because it is a RuntimeException
		return new ExecutionException(e);
	}
	
	/* -----------------  ----------------- */
	
	public static class LatchFuture extends ResultFuture<Object, RuntimeException> {
		
		public void setCompleted() {
			setResult(null);
		}
		
		@Override
		protected void handleReSetResult() {
			// Do nothing - this is allowed because the possible value is always null anyways
		}
		
	}
	
}