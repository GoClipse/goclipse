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
public class ResultFuture<DATA> implements Future<DATA> {
	
	protected final CountDownLatch completionLatch = new CountDownLatch(1);
	protected final Object lock = new Object();
	
    protected volatile ResultStatus status = ResultStatus.INITIAL;
	protected volatile DATA resultValue;
	
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
			}
			this.resultValue = resultValue;
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
	
	public DATA awaitResult() 
			throws OperationCancellation, InterruptedException {
		awaitCompletion();
		return getResult();
	}
	
	public DATA awaitResult(long timeout, TimeUnit unit) 
			throws OperationCancellation, InterruptedException, TimeoutException {
		awaitCompletion(timeout, unit);
		return getResult();
	}
	
	protected DATA getResult() throws OperationCancellation {
		if(isCancelled()) {
			throw new OperationCancellation();
		}
		return resultValue;
	}
	
	public void awaitSuccess() throws OperationCancellation, InterruptedException {
		awaitResult();
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
		} catch(OperationCancellation e) {
			throw toExecutionException(e);
		}
	}
	
	@Override
	public DATA get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
			return awaitResult(timeout, unit);
		} catch(OperationCancellation e) {
			throw toExecutionException(e);
		}
	}
	
	public ExecutionException toExecutionException(OperationCancellation e) throws ExecutionException {
		// Don't throw java.util.concurrent.CancellationException because it is a RuntimeException
		return new ExecutionException(e);
	}
	
	/* -----------------  ----------------- */
	
	public static class LatchFuture extends ResultFuture<Object> {
		
		public void setCompleted() {
			setResult(null);
		}
		
	}
	
}