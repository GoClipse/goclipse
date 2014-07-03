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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.misc.MiscUtil.isUncheckedException;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of {@link ITaskAgent} based on an {@link ExecutorService}.
 */
public class ExecutorTaskAgent extends ThreadPoolExecutor implements ExecutorService, ITaskAgent {
	
	public static ITaskAgent createExecutorAgent(String name) {
		return new ExecutorTaskAgent(name);
	}
	
	protected final String name;
	
	public ExecutorTaskAgent(String name) {
		super(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), new NameAgentThreadFactory(name));
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/** Modified from DefaultThreadFactory to allow setting an unique thread name */
	protected static class NameAgentThreadFactory implements ThreadFactory {
		private final ThreadGroup group;
		private final String name;
		
		NameAgentThreadFactory(String name) {
			this.name = name;
			SecurityManager s = System.getSecurityManager();
			this.group  = (s != null) ? s.getThreadGroup() :
				Thread.currentThread().getThreadGroup();
		}
		
		@Override
		public Thread newThread(Runnable r) {
			
			Thread t = new Thread(group, r, name, 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}
	
	@Override
	protected void beforeExecute(Thread thread, Runnable runnable) {
		super.beforeExecute(thread, runnable);
	}
	
	@Override
	protected void afterExecute(Runnable runnable, Throwable throwable) {
		super.afterExecute(runnable, throwable);
		
		if (throwable == null && runnable instanceof Future) {
			Future<?> future = (Future<?>) runnable;
			assertTrue(future.isDone());
			try {
				future.get();
			} catch (InterruptedException ie) {
				// This should not happen because the future is done, get() should return succesfully
				throw assertFail();
			} catch (CancellationException ce) {
				assertTrue(future.isCancelled());
				return;
			} catch (ExecutionException ee) {
				throwable = ee.getCause();
				if(!isUncheckedException(throwable)) {
					// for Future's, we only consider it to be unexpected if it's an unchecked exception
					// otherwise it can be expected the task client code know how to handle to exception
					return;
				}
			}
		}
		
		if(throwable != null) {
			handleUnexpectedException(throwable);
		}
	}
	
	@SuppressWarnings("unused")
	protected void handleUnexpectedException(Throwable throwable) {
	}
	
	@Override
	public long getSubmittedTaskCount() {
		return getTaskCount();
	}
	
	/** @return true if there is any task being executed, or queued to be executed.
	 * WARNING: This method has a concurrency bug and may return false
	 * even if the current thread has submited a task that has not yet completed.
	 * Use only for test code, in a tentative way only. */
	public boolean guess_hasPendingTasks() {
		return getQueue().size() > 0 || getActiveCount() > 0;
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
	
	@Override
	public void awaitTermination() throws InterruptedException {
		while(!awaitTermination(1000, TimeUnit.SECONDS)) {
			
		}
	}
	
}