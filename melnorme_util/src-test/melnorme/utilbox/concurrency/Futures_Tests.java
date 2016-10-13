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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Test;

import melnorme.utilbox.concurrency.ExecutorTaskAgent_Test.Tests_ExecutorTaskAgent;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.core.fntypes.SupplierExt;
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
				executor = null;
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
		
		protected abstract FUTURE initFuture(SupplierExt<Object> callable);
		
		public void test_result() throws Exception {
			
			// Test await result
			FUTURE future = submitToExecutor(() -> "result");
			checkResult(future, "result");
			assertTrue(future.tryCancel() == false);
			assertTrue(future.isCompletedSuccessfully());
			checkResult(future, "result");
			
			// Test RuntimeException handling
			test_result_forRuntimeException();
		}
		
		protected void test_result_forRuntimeException() {
			verifyThrows(() -> {
				submitAndAwaitResult(() -> { 
					throw new RuntimeException("xxx2");
				});
			}, RuntimeException.class, "xxx2");
		}
		
		protected void checkResult(FUTURE future, Object result) 
				throws OperationCancellation, InterruptedException, TimeoutException {
			assertEquals(result, future.awaitResult());
			
			Thread.currentThread().interrupt();
			assertEquals(result, future.getResult_forSuccessfulyCompleted());
			Thread.currentThread().interrupt();
			assertEquals(result, future.getResult_forTerminated());
			Thread.currentThread().interrupt();
			assertEquals(result, future.awaitResult());
			Thread.currentThread().interrupt();
			assertEquals(result, future.awaitResult(1000, TimeUnit.DAYS));
			assertTrue(Thread.interrupted());
			assertTrue(Thread.currentThread().isInterrupted() == false);
		}
		
		protected FUTURE submitToExecutor(SupplierExt<Object> callable) {
			FUTURE future = initFuture(callable);
			submitToExecutor(future, ForkJoinPool.commonPool());
			return future;
		}
		
		protected void submitToExecutor(FUTURE future, ExecutorService executor) {
			assertTrue(future.canExecute());
			executor.execute(future);
		}
		
		protected <EXC extends Exception> FUTURE submitAndAwaitResult(SupplierExt<Object> callable)
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
		protected MonitorRunnableFuture<Object> initFuture(SupplierExt<Object> callable) {
			return new MonitorRunnableFuture<Object>() {
				@Override
				protected Object internalInvoke() {
					return callable.call();
				}
			};
		}
		
	}
	
	public static class RunnableFuture2_Test extends AbstractFutureTest<IRunnableFuture2<?>> {
		
		@Override
		protected IRunnableFuture2<Object> initFuture(SupplierExt<Object> callable) {
			return IRunnableFuture2.toFuture(callable);
		}
		
		@Override
		public void test_result() throws Exception {
			super.test_result();
			

			IRunnableFuture2<Result<Object, RuntimeException>> future = IRunnableFuture2.toResultFuture(() -> { 
				throw new RuntimeException("xxx2");
			});
			
			submitToExecutor(future, ForkJoinPool.commonPool());
			Result<Object, RuntimeException> result = future.awaitResult();
			
			verifyThrows(() -> {
				result.get();
			}, RuntimeException.class, "xxx2");

		}
		
	}
	
	public static class Future2Adapter_Test extends AbstractFutureTest<RunnableFutureAdapter<Object>> {
		
		@Override
		protected RunnableFutureAdapter<Object> initFuture(SupplierExt<Object> callable) {
			return new RunnableFutureAdapter<>(new FutureTask<>(callable));
		}
		
		@Override
		protected void checkResult(RunnableFutureAdapter<Object> future, Object result)
				throws OperationCancellation, InterruptedException, TimeoutException {
			super.checkResult(future, new Result<>(result));
		}
		
		@Override
		protected void test_result_forRuntimeException() {
			verifyThrows(() -> {
				submitAndAwaitResult(() -> { 
					throw new RuntimeException("xxx2");
				});
			}, null);
		}
		
		@Test
		public void test_errors() throws Exception { test_errors$(); }
		public void test_errors$() throws Exception {
			
			CompletableFuture<String> completableFuture = new CompletableFuture<>();
			FutureAdapter<String> future2Adapter = new FutureAdapter<>(completableFuture);
			
			assertTrue(future2Adapter.isCompletedSuccessfully() == false);
			completableFuture.completeExceptionally(new RuntimeException("XXX"));
			assertTrue(future2Adapter.isTerminated() == true);
			assertTrue(future2Adapter.isCompletedSuccessfully() == true);
			Result<String, Throwable> awaitResult = future2Adapter.awaitResult();
			verifyThrows(awaitResult::get, RuntimeException.class, "XXX");
		}
		
	}
	
}