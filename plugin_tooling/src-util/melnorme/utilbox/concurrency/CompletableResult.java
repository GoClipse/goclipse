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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A future meant to be completed by an explicit {@link #setResult()} call. 
 * Similar to {@link CompletableFuture} but with a safer and simplified API, particularly:
 * 
 * - Uses the {@link BasicFuture} interface which has a safer and more precise API than {@link Future} 
 * with regards to exception throwing.
 * - By default, completing the Future ({@link #setResult()}) can only be attempted once, 
 * it is illegal for multiple {@link #setResult()} calls to be attempted.
 *
 */
public class CompletableResult<DATA> 
	implements BasicFuture<DATA>
{
	
	protected final CountDownLatch completionLatch = new CountDownLatch(1);
	protected final Object lock = new Object();
	
    protected volatile ResultStatus status = ResultStatus.NOT_COMPLETED;
	protected volatile DATA result;
	
	public static enum ResultStatus { NOT_COMPLETED, RESULT_SET, CANCELLED }
	
	public CompletableResult() {
		super();
	}
	
	@Override
	public boolean isDone() {
		return status != ResultStatus.NOT_COMPLETED;
	}
	
	@Override
	public boolean isCancelled() {
		return status == ResultStatus.CANCELLED;
	}
	
	public CountDownLatch getCompletionLatch() {
		return completionLatch;
	}
	
	public void setResult(DATA result) {
		synchronized (lock) {
			if(isDone()) {
				if(isCancelled()) {
					return;
				}
				handleReSetResult();
				return;
			}
			this.result = result;
			status = ResultStatus.RESULT_SET;
			completionLatch.countDown();
		}
	}
	
	/** 
	 * Set the result of this {@link CompletableResult} as cancelled.
	 * Has no effect if a result has already been set.
	 * 
	 * Note: if there is still a task or background task calculating a result, it is the caller's responsibility
	 * to terminate that task, this {@link CompletableResult} knows nothing about it.
	 */
	public boolean setCancelledResult() {
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
			throws OperationCancellation, InterruptedException {
		awaitCompletion();
		return getResult_afterCompletion();
	}
	
	@Override
	public DATA awaitResult(long timeout, TimeUnit unit) 
			throws OperationCancellation, InterruptedException, TimeoutException {
		awaitCompletion(timeout, unit);
		return getResult_afterCompletion();
	}
	
	protected DATA getResult_afterCompletion() throws OperationCancellation {
		if(isCancelled()) {
			throw new OperationCancellation();
		}
		return result;
	}
	
	/* -----------------  ----------------- */
	
	public static class CompletableLatch extends CompletableResult<Object> {
		
		public void setCompleted() {
			setResult(null);
		}
		
		@Override
		protected void handleReSetResult() {
			// Do nothing - this is allowed because the possible value is always null anyways
		}
		
	}
	
}