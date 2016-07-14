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

import org.junit.After;
import org.junit.Test;

import melnorme.lang.utils.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.ExecutorTaskAgent_Test.Tests_ExecutorTaskAgent;
import melnorme.utilbox.concurrency.Futures_Tests.InterruptibleEndlessRunnable;
import melnorme.utilbox.concurrency.Futures_Tests.InvalidRunnable;
import melnorme.utilbox.tests.CommonTest;

abstract class AbstractFutureTest extends CommonTest {
	
	protected Tests_ExecutorTaskAgent executor;
	
	protected void init_Executor() {
		teardown();
		executor = new Tests_ExecutorTaskAgent(getClass().getSimpleName());
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
			testCancellation();
		}
	}
	
	protected IRunnableFuture2<?> runnableFuture;
	
	protected IRunnableFuture2<?> getFuture() {
		return runnableFuture;
	}
	
	protected abstract IRunnableFuture2<?> init_Future(Runnable latchRunnable);
	
	protected void testCancellation() {
		test_cancellation_before_running();
		
		test_cancellation_of_running_task();
		
		test_cancellation_of_waiting_task();
	}
	
	protected void test_cancellation_of_running_task() {
		init_Executor();
		
		LatchRunnable2 latchRunnable = submitRunnableFuture();
		latchRunnable.awaitEntry();
		
		cancelFuture();
		testExpectInterruptionOfRunningTask(latchRunnable);
	}
	
	protected void test_cancellation_of_waiting_task() {
		init_Executor();
		executor.submit(new InterruptibleEndlessRunnable());
		
		submitRunnableFuture();
		
		cancelFuture();
	}
	
	protected void test_cancellation_before_running() {
		init_Executor();
		
		runnableFuture = init_Future(new InvalidRunnable());
		cancelFuture();
		assertTrue(runnableFuture.isCancelled());
		runnableFuture.run();
	}
	
	protected LatchRunnable2 submitRunnableFuture() {
		LatchRunnable2 latchRunnable = new InterruptibleEndlessRunnable();
		runnableFuture = init_Future(latchRunnable);
		assertTrue(getFuture().isDone() == false);
		executor.submitRunnable(getFuture());
		return latchRunnable;
	}
	
	protected void cancelFuture() {
		getFuture().tryCancel();
		verifyThrows(() -> getFuture().awaitResult2(), OperationCancellation.class);
	}
	
	protected void testExpectInterruptionOfRunningTask(LatchRunnable2 latchRunnable) {
		latchRunnable.awaitExit();
	}
	
}

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
	
	public static class MonitorFuture_Test extends AbstractFutureTest {
		
		public MonitorRunnableFuture monitorFuture;
		
		@Override
		protected IRunnableFuture2<?> init_Future(Runnable latchRunnable) {
			monitorFuture = new MonitorRunnableFuture() {
				@Override
				protected void runTask() {
					latchRunnable.run();
				}
				
			};
			return monitorFuture;
		}
		
	}
	
	public static class RunnableFuture2_Test extends AbstractFutureTest {
		
		public RunnableFuture2<Object> runnableFuture;
		
		@Override
		protected IRunnableFuture2<?> init_Future(Runnable latchRunnable) {
			return runnableFuture = new RunnableFuture2<>(() -> {
				latchRunnable.run();
				return null;
			});
		}
		
		@Override
		protected void cancelFuture() {
			getFuture().tryCancel();
//			verifyThrows(() -> getFuture().awaitResult2(), OperationCancellation.class);
		}
		
		@Override
		protected void testExpectInterruptionOfRunningTask(LatchRunnable2 latchRunnable) {
//			super.testExpectInterruptedOfRunningTask(latchRunnable);
		}
		
		@Override
		protected void test_cancellation_before_running() {
//			super.testCancellationBeforeRunning();
		}
		
	}
	
}