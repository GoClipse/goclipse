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

import melnorme.utilbox.core.fntypes.CallableX;
import melnorme.utilbox.core.fntypes.OperationResult;
import melnorme.utilbox.core.fntypes.OperationCallable;

/**
 * A few minor additions to {@link IBasicExecutor}
 * 
 */
public interface ICommonExecutor extends IBasicExecutor {
	
	/** @return the name of this executor. Used mainly for debugging purposes, such as thread naming. */
	String getName();
	
	/* -----------------  ----------------- */
	
	/** Same as {@link ExecutorService#submit(Callable)}, but with a more strict exception throwing API. */
	<RET, EXC extends Exception> FutureX<RET, EXC> submitX(CallableX<RET, EXC> callable);
	
	/** Similar to {@link #submitX(CallableX)}, but using an {@link OperationCallable}. */
	<RET> CommonFuture<RET> submitOp(OperationCallable<RET> opCallable);
	
	/** Alias interface */
	public static interface CommonFuture<RET> extends FutureX<OperationResult<RET>, RuntimeException> {
		
	}
	
	default <RET> FutureX<RET, RuntimeException> submitR(Runnable runnable) {
		return submitX(() -> {
			runnable.run();
			return null;
		});
	}
	
	/** Submit a {@link FutureTaskX} for execution. */
	void submitTask(FutureTaskX<?, RuntimeException> futureTask);
	
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