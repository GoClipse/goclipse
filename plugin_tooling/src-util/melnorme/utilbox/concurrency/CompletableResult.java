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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.core.fntypes.CallableX;

/**
 * A future meant to be completed by an explicit {@link #setResult()} call. 
 * Similar to {@link CompletableFuture} but with a safer and simplified API, particularly:
 * 
 * - Uses the {@link BasicFuture} interface which has a safer and more precise API than {@link Future} 
 * with regards to exception throwing.
 * - By default, completing the Future ({@link #setResult()}) can only be attempted once, 
 * it is illegal for multiple {@link #setResult()} calls to be attempted.
 * - This future can also be completed with a RuntimeException, which will be thrown whenever 
 * a client tries to obtain the result. Note that using RuntimeExceptions is discouraged. 
 * The main use case for this functionality is to handle exceptions representing bugs (assertion failures, etc.)
 * in a way that is easier to debug the failure: by threwing them back closer to the point of origin of the bug, 
 * as opposed to getting silently swallowed/ignored by an executor worker thread. 
 *
 */
public class CompletableResult<DATA> 
	implements BasicFuture<DATA>
{
	
	protected final CountDownLatch completionLatch = new CountDownLatch(1);
	protected final Object lock = new Object();
	
    protected volatile ResultStatus status = ResultStatus.NOT_TERMINATED;
	protected volatile DATA result;
	protected volatile RuntimeException resultRuntimeException;
	
	public static enum ResultStatus { NOT_TERMINATED, RESULT_SET, CANCELLED }
	
	public CompletableResult() {
		super();
	}
	
	@Override
	public boolean isTerminated() {
		return status != ResultStatus.NOT_TERMINATED;
	}
	
	public boolean isCompleted() {
		return isTerminated();
	}
	
	@Override
	public boolean isCancelled() {
		return status == ResultStatus.CANCELLED;
	}
	
	public CountDownLatch getCompletionLatch() {
		return completionLatch;
	}
	
	public void setResultFromCallable(CallableX<DATA, RuntimeException> resultCallable) {
		try {
			DATA result = resultCallable.invoke();
			setResult(result);
		} catch(RuntimeException re) {
			doSetResult(null, re);
		}
	}
	
	/**
	 * Complete this future with given result.
	 * NOTE: clients who obtain a result from a functional object like a {@link Callable} or similar, 
	 * should use {@link #setResultFromCallable(CallableX)} instead, in order to preserve RuntimeExceptions  
	 */
	public void setResult(DATA result) {
		doSetResult(result, null);
	}
	
	protected void doSetResult(DATA result, RuntimeException re) {
		assertTrue(re == null || result == null); // Only one possible result
		
		synchronized (lock) {
			if(isTerminated()) {
				if(isCancelled()) {
					return;
				}
				handleReSetResult();
				return;
			}
			this.result = result;
			this.resultRuntimeException = re;
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
			if(isTerminated()) {
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
		if(isCompleted()) {
			return; // Early check so that InterruptedException is not thrown
		}
		completionLatch.await();
	}
	
	public void awaitCompletion(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		if(isCompleted()) {
			return; // Early check so that InterruptedException is not thrown
		}
		boolean success = completionLatch.await(timeout, unit);
		if(!success) {
			throw new TimeoutException();
		}
	}
	
	@Override
	public DATA awaitResult() 
			throws OperationCancellation, InterruptedException {
		awaitCompletion();
		return getResult_forTerminated();
	}
	
	@Override
	public DATA awaitResult(long timeout, TimeUnit unit) 
			throws OperationCancellation, InterruptedException, TimeoutException {
		awaitCompletion(timeout, unit);
		return getResult_forTerminated();
	}
	
	@Override
	public DATA getResult_forSuccessfulyCompleted() {
		assertTrue(isCompletedSuccessfully());
		if(resultRuntimeException != null) {
			throw resultRuntimeException;
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