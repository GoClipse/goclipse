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

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An implementation of {@link ITaskAgent} based on an {@link ExecutorService}.
 */
public class ExecutorTaskAgent extends ThreadPoolExecutorExt implements ExecutorService, ITaskAgent {
	
	public static ITaskAgent createExecutorAgent(String name, UncaughtExceptionHandler ueHandler) {
		return new ExecutorTaskAgent(name, ueHandler);
	}
	
	public ExecutorTaskAgent(String name, UncaughtExceptionHandler ueHandler) {
		super(1, 1, new LinkedBlockingQueue<Runnable>(), name, ueHandler);
	}
	
	@Override
	public List<Runnable> shutdownNow() {
		List<Runnable> remaining = super.shutdownNow();
		for (Runnable runnable : remaining) {
			if(runnable instanceof FutureTask<?>) {
				FutureTask<?> futureTask = (FutureTask<?>) runnable;
				futureTask.cancel(true);
			}
		}
		return remaining;
	}
	
	@Override
	public void waitForPendingTasks() {
		try {
			awaitPendingTasks();
		} catch (InterruptedException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		} 
	}
	
	public void awaitPendingTasks() throws InterruptedException {
		Future<?> waiter = submit(new Runnable() {
			@Override
			public void run() {
			}
		});
		try {
			waiter.get();
		} catch (ExecutionException e) {
			throw assertFail();
		} catch (CancellationException e) {
			// The only way this task was cancelled should be if shutdownNow was used.
			// In that case, await termination
			assertTrue(isShutdown());
			awaitTermination();
		}
	}
	
}