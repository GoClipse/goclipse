/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import melnorme.utilbox.tests.CommonTestExt;

public class ExecutorTaskAgent_Test extends CommonTestExt {
	
	@Test
	public void testBasic() throws Exception {
		runMultipleTimes(10, 100, () -> testBasic$());
	}
	public void testBasic$() throws Exception {
		ExecutorTaskAgent agent = new Tests_ExecutorTaskAgent("testShutdownNow");
		LatchRunnable firstTask = new LatchRunnable(true);
		LatchRunnable secondTask = new LatchRunnable(true);
		
		agent.submit(firstTask);
		assertTrue(agent.getSubmittedTaskCount() == 1);
		Future<?> secondTaskFuture = agent.submit(secondTask);
		assertTrue(agent.getSubmittedTaskCount() == 2);
		
		firstTask.awaitTaskEntry();
		assertTrue(secondTaskFuture.isCancelled() == false);
		
		List<Runnable> cancelledTasks = agent.shutdownNowAndCancelAll();
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
		ExecutorTaskAgent agent = new Tests_ExecutorTaskAgent("testShutdownNow_Interrupt");
		LatchRunnable firstTask = new LatchRunnable(false);
		agent.submit(firstTask);
		
		Future<?> future = agent.submit(new LatchRunnable(true));
		
		firstTask.awaitTaskEntry();
		
		assertTrue(future.isDone() == false);
		List<Runnable> cancelledTasks = agent.shutdownNowAndCancelAll();
		assertTrue(cancelledTasks.size() == 1);
		assertTrue(future.isCancelled());
		
		agent.awaitTermination();
	}
	
	public static class Tests_ExecutorTaskAgent extends ExecutorTaskAgent {
		
		protected final LinkedBlockingQueue<Throwable> uncaughtExceptions;
		
		public Tests_ExecutorTaskAgent(String name) {
			this(new LinkedBlockingQueue<>(), name);
		}
		
		protected Tests_ExecutorTaskAgent(LinkedBlockingQueue<Throwable> uncaughtExceptions, String name) {
			super("TestsExecutor." + name, (throwable) -> {
				if(throwable != null) {
					uncaughtExceptions.add(throwable);
				}			
			});
			this.uncaughtExceptions = uncaughtExceptions;
		}
		
		@Override
		public void awaitTermination() throws InterruptedException {
			super.awaitTermination();
		}
		
	}
	
	@Test
	public void testExceptionHandling() throws Exception {
		runMultipleTimes(10, 100, () -> testExceptionHandling$());
	}
	
	public void testExceptionHandling$() throws Exception {
		
		Tests_ExecutorTaskAgent agent = new Tests_ExecutorTaskAgent("testExceptionHandling");
		Future<?> future;
		
		future = agent.submit(new LatchRunnable(false));
		future.cancel(true);
		
		try {
			future.get();
			assertFail();
		} catch (CancellationException ce) {
			// ok
		}
		agent.awaitPendingTasks();
		assertTrue(agent.uncaughtExceptions.size() == 0);
		
		
		Runnable npeRunnable = () -> {
			throw new RuntimeException("npeRunnable"); // a RuntimeException, representing an internal error
		};
		Callable<String> normalTask = () -> {
			throw new IOException("Some expected exception");
		};
		
		checkExceptionHandling(agent.uncaughtExceptions, agent, 
			agent.submit(npeRunnable), RuntimeException.class, false);
		
		checkExceptionHandling(agent.uncaughtExceptions, agent, 
			agent.submit(normalTask), IOException.class, true);
		
		checkExceptionHandling(agent.uncaughtExceptions, agent, 
			agent.submit(() -> { throw new RuntimeException("---"); }), RuntimeException.class, false);
		
		agent.execute(npeRunnable);
		
		agent.shutdown();
		agent.awaitTermination();
		
		while(true) {
			if(agent.uncaughtExceptions.size() == 1) {
				break;
			}
			Thread.sleep(20);
		}
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