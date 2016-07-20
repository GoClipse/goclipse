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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Test;

import melnorme.lang.utils.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.ExecutorTaskAgent_Test.Tests_ExecutorTaskAgent;
import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.tests.CommonTest;

public abstract class Futures_Tests extends CommonTest {
	
	/** A runnable that is never meant to be run. */
	public static class InvalidRunnable extends LatchRunnable2 {
		
		public InvalidRunnable() {
			super();
		}
		
		@Override
		protected void doRun() {
			System.exit(1);
		}
	}
	
	public static class InterruptibleEndlessRunnable extends LatchRunnable2 {
		
		protected volatile boolean interrupted = false;
		
		public InterruptibleEndlessRunnable() {
			super();
		}
		
		@Override
		protected void doRun() {
			try {
				new CountDownLatch(1).await();
			} catch(InterruptedException e) {
				interrupted = true;
			}
		}
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class AbstractFutureTest<FUTURE extends IRunnableFuture2<?>> extends CommonTest {
		
		protected Tests_ExecutorTaskAgent executor;
		
		protected void init_Executor() {
			teardown();
			executor = new Tests_ExecutorTaskAgent(getClass().getSimpleName());
			future = null;
		}
		
		@After
		public void teardown() {
			if(executor != null) {
				executor.shutdownNow();
			}
		}
		
		@Test
		public void test() throws Exception { test$(); }
		public void test$() throws Exception {
			for(int i = 0; i < 10; i++) {
				test_result();
				
				testCancellation();
			}
		}
		
		protected FUTURE future;
		
		protected FUTURE getFuture() {
			return future;
		}
		
		protected FUTURE initFuture_fromRunnable(Runnable runnable) {
			FUTURE future = initFuture(() -> {
				runnable.run();
				return null;
			});
			assertTrue(future.isTerminated() == false);
			return future;
		}
		
		protected abstract FUTURE initFuture(CallableX<Object, RuntimeException> callable);
		
		public void test_result() throws Exception {
			
			// Test await result
			FUTURE future = submitToExecutor(() -> "result");
			assertEquals("result", future.awaitResult());
			
			Thread.currentThread().interrupt();
			assertEquals("result", future.getResult_forSuccessfulyCompleted());
			Thread.currentThread().interrupt();
			assertEquals("result", future.getResult_forTerminated());
			Thread.currentThread().interrupt();
			assertEquals("result", future.awaitResult());
			Thread.currentThread().interrupt();
			assertEquals("result", future.awaitResult(1000, TimeUnit.DAYS));
			assertTrue(Thread.interrupted());
			assertTrue(Thread.currentThread().isInterrupted() == false);
			
			
			// Test RuntimeException handling
			verifyThrows(() -> {
				submitAndAwaitResult(() -> { 
					throw new RuntimeException("xxx2");
				});
			}, RuntimeException.class, "xxx2");
			
		}
		
		protected FUTURE submitToExecutor(CallableX<Object, RuntimeException> callable) {
			FUTURE future = initFuture(callable);
			submitToExecutor(future, ForkJoinPool.commonPool());
			return future;
		}
		
		protected void submitToExecutor(FUTURE future, ExecutorService executor) {
			if(executor instanceof ThreadPoolExecutorExt) {
				ThreadPoolExecutorExt executorExt = (ThreadPoolExecutorExt) executor;
				executorExt.submitTask(future);
			}
			executor.execute(future);
		}
		
		protected <EXC extends Exception> FUTURE submitAndAwaitResult(CallableX<Object, RuntimeException> callable)
				throws OperationCancellation, InterruptedException, EXC {
			FUTURE future = submitToExecutor(callable);
			future.awaitResult();
			return future;
		}
		
		protected void doSubmitFuture(FUTURE future) {
			submitToExecutor(future, executor);
		}
		
		/* -----------------  ----------------- */
		
		protected void testCancellation() {
			test_cancellation_before_running();
			
			test_cancellation_of_running_task();
			
			test_cancellation_of_waiting_task();
		}
		
		protected void test_cancellation_before_running() {
			init_Executor();
			
			FUTURE future = initFuture_fromRunnable(new InvalidRunnable());
			cancelFuture(future);
			assertTrue(future.isCancelled());
			runFuture(future);
		}
		
		protected void runFuture(FUTURE future) {
			future.run();
		}
		
		protected void test_cancellation_of_running_task() {
			init_Executor();
			
			LatchRunnable2 latchRunnable = new InterruptibleEndlessRunnable();
			submitRunnableFuture(latchRunnable);
			latchRunnable.awaitEntry();
			
			cancelFuture();
			testExpectInterruptionOfRunningTask(latchRunnable);
		}
		
		protected void test_cancellation_of_waiting_task() {
			init_Executor();
			executor.submit(new InterruptibleEndlessRunnable());
			
			submitRunnableFuture(new InterruptibleEndlessRunnable());
			
			cancelFuture();
		}
		
		protected void submitRunnableFuture(LatchRunnable2 latchRunnable) {
			future = initFuture_fromRunnable(latchRunnable);
			doSubmitFuture(future);
		}
		
		protected void cancelFuture() {
			cancelFuture(getFuture());
		}
		
		public void cancelFuture(Future2<?> future2) {
			future2.tryCancel();
			verifyThrows(() -> future2.awaitResult2(), OperationCancellation.class);
		}
		
		protected void testExpectInterruptionOfRunningTask(LatchRunnable2 latchRunnable) {
			latchRunnable.awaitExit();
		}
		
	}
	
	
	/* -----------------  ----------------- */
	
	public static class MonitorFuture_Test extends AbstractFutureTest<MonitorRunnableFuture<Object>> {
		
		@Override
		protected MonitorRunnableFuture<Object> initFuture(CallableX<Object, RuntimeException> callable) {
			return new MonitorRunnableFuture<Object>() {
				@Override
				protected Object internalInvoke() {
					return callable.call();
				}
			};
		}
		
	}
	
	public static class RunnableFuture2_Test extends AbstractFutureTest<IRunnableFuture2<Object>> {
		
		@Override
		protected IRunnableFuture2<Object> initFuture(CallableX<Object, RuntimeException> callable) {
			return IRunnableFuture2.toFuture(callable);
		}
		
	}
	
}