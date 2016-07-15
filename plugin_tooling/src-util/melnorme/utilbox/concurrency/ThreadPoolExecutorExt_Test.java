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
import java.util.concurrent.Future;

import org.junit.Test;

import melnorme.lang.utils.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.ExecutorTaskAgent_Test.Tests_ExecutorTaskAgent;
import melnorme.utilbox.concurrency.Futures_Tests.InvalidRunnable;
import melnorme.utilbox.concurrency.Futures_Tests.InterruptibleEndlessRunnable;
import melnorme.utilbox.tests.CommonTest;

public class ThreadPoolExecutorExt_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() {
		testShutdownAndCancelAll();
		testShutdownAndCancelAll2();
	}
	
	protected void testShutdownAndCancelAll() {
		
		ThreadPoolExecutorExt executor = new Tests_ExecutorTaskAgent(getClass().getSimpleName());
		
		LatchRunnable2 executingRunnable = new InterruptibleEndlessRunnable();
		executor.execute(executingRunnable); // Initial task
		
		Future<?> futureTask = executor.submit(new InvalidRunnable());
		
		MonitorRunnableFuture<Void> monitorFutureTask = monitorRunnableFuture(new InvalidRunnable());
		executor.submitTask(monitorFutureTask);
		
		RunnableFuture2<Object> runnableFuture = new RunnableFuture2<>(() -> {
			throw assertFail();
		});
		executor.submitTask(runnableFuture);
		
		
		executingRunnable.awaitEntry();
		executor.shutdownNowAndCancelAll();
		
		
		// Test that tasks were cancelled by the executor
		assertTrue(futureTask.isCancelled());
		verifyThrows(() -> futureTask.get(), CancellationException.class);
		assertTrue(monitorFutureTask.isCancelled());
		verifyThrows(() -> monitorFutureTask.awaitResult2(), OperationCancellation.class);
	}
		
	protected void testShutdownAndCancelAll2() {
		
		ThreadPoolExecutorExt executor = new Tests_ExecutorTaskAgent(getClass().getSimpleName());
		
		InterruptibleEndlessRunnable latchRunnable = new InterruptibleEndlessRunnable();
		
		// Initial task
		MonitorRunnableFuture<Void> monitorFutureTask = monitorRunnableFuture(latchRunnable);
		executor.submitTask(monitorFutureTask);
		
		latchRunnable.awaitEntry();
		executor.shutdownNowAndCancelAll();
		
		
		// Test that tasks were cancelled by the executor
		verifyThrows(() -> monitorFutureTask.awaitResult2(), null);
		
		
		// Ensure executing runnable was interrupted and continued
		latchRunnable.awaitExit();
		assertTrue(latchRunnable.interrupted);
	}
	
	public static MonitorRunnableFuture<Void> monitorRunnableFuture(Runnable runnable) {
		assertNotNull(runnable);
		return new MonitorRunnableFuture<Void>() {
			@Override
			protected Void invokeToResult() {
				runnable.run();
				return null;
			}
		};
	}
	
}