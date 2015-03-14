/*******************************************************************************
 * Copyright (c) 2013, 2013 Bruno Medeiros and other Contributors.
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import dtool.tests.DToolTests;

public class ExecutorTaskAgent_Test {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		int times = DToolTests.TESTS_LITE_MODE ? 10 : 100;
		for (int i = 0; i < times; i++) {
			testBasic();
		}
	}
	public void testBasic() throws Exception {
		ExecutorTaskAgent agent = new ExecutorTaskAgent("testShutdownNow");
		LatchRunnable firstTask = new LatchRunnable();
		LatchRunnable secondTask = new LatchRunnable();
		
		agent.submit(firstTask);
		assertTrue(agent.getSubmittedTaskCount() == 1);
		Future<?> secondTaskFuture = agent.submit(secondTask);
		assertTrue(agent.getSubmittedTaskCount() == 2);
		
		firstTask.awaitTaskEntry();
		assertTrue(secondTaskFuture.isCancelled() == false);
		
		List<Runnable> cancelledTasks = agent.shutdownNow();
		assertTrue(cancelledTasks.size() == 1);
		
		assertTrue(secondTaskFuture.isCancelled() == true);
		assertTrue(agent.isShutdown());
		Thread.sleep(1);
		assertTrue(agent.isTerminating() == true);
		assertTrue(agent.isTerminated() == false);
		firstTask.releaseAll();
		agent.awaitTermination();
		assertTrue(agent.isShutdown());
		assertTrue(agent.isTerminating() == false);
		assertTrue(agent.isTerminated());
		
		testShutdownNow_Interrupt();
	}
	
	// test that shutdownNow interrupts current task.
	public void testShutdownNow_Interrupt() throws InterruptedException {
		ExecutorTaskAgent agent = new ExecutorTaskAgent("testShutdownNow_Interrupt");
		LatchRunnable firstTask = new LatchRunnable(false);
		agent.submit(firstTask);
		
		agent.submit(new LatchRunnable());
		
		firstTask.awaitTaskEntry();
		
		List<Runnable> cancelledTasks = agent.shutdownNow();
		assertTrue(cancelledTasks.size() == 1);
		
		agent.awaitTermination();
	}
	
	
	@Test
	public void testExceptionHandling() throws Exception { testExceptionHandling$(); }
	public void testExceptionHandling$() throws Exception {
		final LinkedBlockingQueue<Throwable> unexpectedExceptions = new LinkedBlockingQueue<>();
		
		ExecutorTaskAgent agent = new ExecutorTaskAgent("testExceptionHandling") {
			@Override
			protected void handleUnexpectedException(Throwable throwable) {
				if(throwable != null) {
					unexpectedExceptions.add(throwable);
				}
			}
		};
		Future<?> future;
		
		Runnable npeRunnable = new Runnable() {
			@Override
			public void run() {
				throw new RuntimeException(); // a RuntimeException, representing an internal error
			}
		};
		Callable<String> normalTask = new Callable<String>() {
			@Override
			public String call() throws Exception {
				throw new IOException("Some expected exception");
			}
		};
		
		
		future = agent.submit(npeRunnable);
		
		try {
			future.cancel(true);
			future.get();
		} catch (CancellationException ce) {
			// ok
		}
		agent.awaitPendingTasks();
		assertTrue(unexpectedExceptions.size() == 0);
		
		
		checkExceptionHandling(unexpectedExceptions, agent, 
			agent.submit(npeRunnable), RuntimeException.class, false);
		
		checkExceptionHandling(unexpectedExceptions, agent, 
			agent.submit(normalTask), IOException.class, true);
	}
	
	protected void checkExceptionHandling(final LinkedBlockingQueue<Throwable> unexpectedExceptions, 
			ExecutorTaskAgent agent, Future<?> future, Class<? extends Exception> expectedKlass, boolean isExpected) 
					throws InterruptedException {
		
		try {
			future.get();
		} catch (ExecutionException ce) {
			assertTrue(expectedKlass.isInstance(ce.getCause()));
			// ok
		}
		agent.awaitPendingTasks();
		
		if(expectedKlass == null || isExpected) {
			assertTrue(unexpectedExceptions.size() == 0);
			return;
		} else {
			assertTrue(unexpectedExceptions.size() == 1);
			Throwable removed = unexpectedExceptions.remove();
			assertTrue(expectedKlass.isInstance(removed));
		}
	}
	
}