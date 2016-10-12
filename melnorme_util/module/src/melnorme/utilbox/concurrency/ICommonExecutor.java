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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import melnorme.utilbox.core.fntypes.CallableX;

/**
 * A few minor additions to {@link IBasicExecutor}
 * 
 */
public interface ICommonExecutor extends IBasicExecutor {
	
	/** @return the name of this executor. Used mainly for debugging purposes, such as thread naming. */
	String getName();
	
	/* -----------------  ----------------- */
	
	/** Submit a {@link ICancellableTask} for execution. */
	void submitTask(ICancellableTask runnableTask);
	
	
	/** Submit a {@link IRunnableFuture2} for execution. @return the given future. */
	default <RET> Future2<RET> submitFuture(IRunnableFuture2<RET> runnableFuture) {
		submitTask(runnableFuture);
		return runnableFuture;
	}
	
	/** Same as {@link ExecutorService#submit(Runnable)}, but returning a {@link BasicFuture}. */
	default <RET> BasicFuture<RET> submitBasicRunnable(Runnable runnable) {
		// need to use anon-class instead of lambda, javac errors on lambda expresion
		return submitBasicCallable(new CallableX<RET, RuntimeException>() {
			@Override
			public RET call() throws RuntimeException {
				runnable.run();
				return null;
			}
		});
	}
	
	/** Same as {@link ExecutorService#submit(Callable)}, 
	 * but using {@link CallableX} instead, and returning a {@link Future2}. */
	default <RET> Future2<RET> submitBasicCallable(CallableX<RET, RuntimeException> callable) {
		return submitFuture(IRunnableFuture2.toFuture(callable));
	}
	
	/* -----------------  ----------------- */
	
	/** 
	 * @return the total number of tasks that have been submitted for execution (including possibly rejected tasks).
	 * This is intented to be used by tests code only, it shouldn't be meaningful otherwise. 
	 */
	long getSubmittedTaskCount();
	
	/* -----------------  ----------------- */
	
	/**
	 * Do {@link #shutdownNow()} and cancel pending tasks.
	 */
	List<Runnable> shutdownNowAndCancelAll();
	
}