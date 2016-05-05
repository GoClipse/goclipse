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
import java.util.concurrent.TimeUnit;

/**
 * A few minor additions to {@link IBasicExecutor}
 * 
 */
public interface ICommonExecutor extends IBasicExecutor {
	
	/* -----------------  Additions  ----------------- */
	
	/** @return the name of this executor. Used mainly for debugging purposes, such as thread naming. */
	String getName();
	
	/** 
	 * @return the total number of tasks that have been submitted for execution (including possibly rejected tasks).
	 * This is intented to be used by tests code only, it shouldn't be meaningful otherwise. 
	 */
	long getSubmittedTaskCount();
	
	/**
	 * Do {@link #shutdownNow()} and cancel pending tasks.
	 */
	List<Runnable> shutdownNowAndCancelAll();
	
	/**
	 * Indefinitely wait for the executor to terminate.
	 * @throws InterruptedException if interrupted.
	 */
	default void awaitTermination() throws InterruptedException {
		while(!awaitTermination(1000, TimeUnit.SECONDS)) {
		}
	}
	
}