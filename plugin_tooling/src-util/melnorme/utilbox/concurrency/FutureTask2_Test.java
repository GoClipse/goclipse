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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.tests.CommonTest;

public class FutureTask2_Test extends CommonTest {
	
	public static class NeverendingCallable implements CallableX<Object, RuntimeException> {
		
		protected final CountDownLatch entryLatch = new CountDownLatch(1);
		
		@Override
		public Object call() throws RuntimeException { 
			try {
				entryLatch.countDown();
				
				new CountDownLatch(1).await();
				throw assertFail();
			} catch(InterruptedException e) {
				return "";
			}
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		
		// Test await result
		assertEquals("result", submitAndAwaitResult(() -> "result"));
		
		// Test Exception handling
		verifyThrows(() -> {
			submitAndAwaitResult(() -> { 
				throw new CommonException("xxx1");
			});
		}, CommonException.class, "xxx1");
		
		// Test RuntimeException handling
		verifyThrows(() -> {
			submitAndAwaitResult(() -> { 
				throw new RuntimeException("xxx2");
			});
		}, RuntimeException.class, "xxx2");
		
		testCancellation$();
		
	}
	
	protected void testCancellation$() throws InterruptedException, OperationCancellation {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		{
			NeverendingCallable neverending = new NeverendingCallable();
			FutureX<?, ?> future = new FutureTaskX<>(neverending).submitTo(executor);
			neverending.entryLatch.await();
			
			// Test cancellation through Future API
			future.cancel(true);
			assertTrue(future.isCancelled());
			verifyThrows(() -> future.awaitResult(), OperationCancellation.class);
		}
		
		{
			new FutureTaskX<>(new NeverendingCallable()).submitTo(executor);
			
			FutureX<?, ?> future = new FutureTaskX<>(new NeverendingCallable()).submitTo(executor);
			
			// Test cancellation through Future API - when task has not started yet
			future.cancel(true);
			assertTrue(future.isCancelled());
			verifyThrows(() -> future.awaitResult(), OperationCancellation.class);
		}
		
		executor.shutdownNow();
	}
	
	protected <EXC extends Exception> Object submitAndAwaitResult(CallableX<Object, EXC> callable)
			throws OperationCancellation, InterruptedException, EXC {
		FutureX<Object, EXC> future = new FutureTaskX<>(callable).submitTo(ForkJoinPool.commonPool());
		return future.awaitResult();
	}
	
}