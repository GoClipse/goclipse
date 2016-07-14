/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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
import static melnorme.utilbox.misc.MiscUtil.isUncheckedException;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;
import melnorme.utilbox.core.fntypes.SupplierExt;

/**
 * An extension to {@link ThreadPoolExecutor}: 
 *  - Safer handling of uncaught exceptions, they must be handled by given UncaughtExceptionHandler.
 *  - Has a few minor utils.
 */
public class ThreadPoolExecutorExt extends ThreadPoolExecutor implements ExecutorService, ICommonExecutor {
	
	public static interface UncaughtExceptionHandler extends Consumer<Throwable> { }
	
	protected final String name;
	protected final UncaughtExceptionHandler uncaughtExceptionHandler;
	
	public ThreadPoolExecutorExt(String name, UncaughtExceptionHandler ueHandler) {
		this(1, 1, new LinkedBlockingQueue<Runnable>(), name, ueHandler);
	}
	
	public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize,
			BlockingQueue<Runnable> workQueue, String name, UncaughtExceptionHandler ueHandler) {
		this(corePoolSize, maximumPoolSize, 60L, TimeUnit.SECONDS, workQueue, name, ueHandler);
	}
	
	public ThreadPoolExecutorExt(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, String name, UncaughtExceptionHandler ueHandler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, 
			new ExecutorThreadFactory(name, ueHandler));
		this.name = assertNotNull(name);
		this.uncaughtExceptionHandler = assertNotNull(ueHandler);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	protected final AtomicInteger executeCount = new AtomicInteger(0);
	
	@Override
	public long getSubmittedTaskCount() {
		return executeCount.get();
	}
	
	@Override
	public void execute(Runnable command) {
		executeCount.incrementAndGet();
		super.execute(command);
	}
	
	@Override
	public void submitRunnable(IRunnableFuture2<?> futureTask) {
		this.execute(futureTask);
	}
	
	public static <FT extends FutureTaskX<?>> FT submitTo(Executor executor, FT futureTask2) {
		executor.execute(futureTask2);
		return futureTask2;
	}
	
	@Override
	public List<Runnable> shutdownNowAndCancelAll() {
		List<Runnable> remaining = super.shutdownNow();
		for (Runnable runnable : remaining) {
			if(runnable instanceof FutureTask<?>) {
				FutureTask<?> futureTask = (FutureTask<?>) runnable;
				futureTask.cancel(true);
			} else if(runnable instanceof Future2<?>) {
				Future2<?> futureTask = (Future2<?>) runnable;
				futureTask.tryCancel();
			}
		}
		return remaining;
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public <RET> Future2<RET> submitSupplier(SupplierExt<RET> callable) {
		return submitTo(this, new FutureTaskX<>(callable));
	}
	
	@Override
	public <RET> Future2<OperationResult<RET>> submitOp(OperationCallable<RET> opCallable) {
		return submitTo(this, new CommonResultFutureTask<>(opCallable.toResultSupplier()));
	}
	
	// This adapter is only needed because CommonFuture is not a true alias.
	protected class CommonResultFutureTask<RET> extends FutureTaskX<OperationResult<RET>> 
		implements Future2<OperationResult<RET>> {
		
		public CommonResultFutureTask(CallableX<OperationResult<RET>, RuntimeException> callable) {
			super(callable);
		}
	}
	
	/* -----------------  Uncaught exception handling  ----------------- */
	
	public static class ExecutorThreadFactory extends NamingThreadFactory {
		
		protected final UncaughtExceptionHandler ueHandler;
		
		public ExecutorThreadFactory(String poolName, UncaughtExceptionHandler ueHandler) {
			super(poolName);
			this.ueHandler = ueHandler;
		}
		
		@Override
		public Thread newThread(Runnable runable) {
			Thread newThread = super.newThread(runable);
			newThread.setUncaughtExceptionHandler((thread, throwable) -> ueHandler.accept(throwable));
			return newThread;
		}
		
	}
	
	@Override
	protected void afterExecute(final Runnable runnable, final Throwable throwable) {
		
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
				Throwable futureThrowable = ee.getCause();
				if(!isUncheckedException(futureThrowable)) {
					// for Future's, we only consider it to be unexpected if it's an unchecked exception
					// otherwise it can be expected the task client code know how to handle to exception
					return;
				} else {
					handleUnexpectedException(futureThrowable);
				}
			}
		}
		
		// We don't handle the uncaught throwables here. 
		// Instead the thread uncaught exception handler handles them.
	}
	
	protected final void handleUnexpectedException(Throwable throwable) {
		// Handle the uncaught exception.
		// Usually this will log the exception, so that the user is noticed an internal error occurred.
		uncaughtExceptionHandler.accept(throwable);
	}
	
}