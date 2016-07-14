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
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;
import melnorme.utilbox.core.fntypes.SupplierExt;

/**
 * A few minor additions to {@link IBasicExecutor}
 * 
 */
public interface ICommonExecutor extends IBasicExecutor {
	
	/** @return the name of this executor. Used mainly for debugging purposes, such as thread naming. */
	String getName();
	
	/* -----------------  ----------------- */
	
	/** Submit a {@link IRunnableFuture2} for execution. */
	void submitRunnable(IRunnableFuture2<?> futureTask);
	
	/** Same as {@link ExecutorService#submit(Callable)}, but using {@link SupplierExt} instead, 
	 * which has with a more strict exception throwing API. */
	<RET> Future2<RET> submitSupplier(SupplierExt<RET> callable);
	
	/** Similar to {@link #submitX(CallableX)}, but using an {@link OperationCallable}. */
	<RET> Future2<OperationResult<RET>> submitOp(OperationCallable<RET> opCallable);
	
	
	default <RET> BasicFuture<RET> submitR(Runnable runnable) {
		return submitSupplier(() -> {
			runnable.run();
			return null;
		});
	}
	
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