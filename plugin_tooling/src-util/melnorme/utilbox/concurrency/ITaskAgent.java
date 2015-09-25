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

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;


/**
 * An {@link ITaskAgent} is a concurrency computational entity, 
 * consisting of a single worker thread running in the background,
 * whose purpose is to execute tasks submitted to it in sequence.
 *   
 * It works in a way similar to an event loop, or an Actor in the Actor concurrency model.  
 */
public interface ITaskAgent extends ICommonAgent {
	
	/** 
	 * Wait for all tasks that have been submitted so far to complete.
	 */
	void waitForPendingTasks();
	
	/** 
	 * @return the total number of tasks that have been submitted for execution (including possibly rejected tasks). 
	 */
	long getSubmittedTaskCount();
	
	/**
	 * Indefinitely wait for the executor to terminate.
	 * @throws InterruptedException if interrupted.
	 */
	void awaitTermination() throws InterruptedException;
	
	/* ----------------------------------- */
	
	/**
	 * Submits a value-returning task for execution and returns a
	 * Future representing the pending results of the task. The
	 * Future's <tt>get</tt> method will return the task's result upon
	 * successful completion.
	 *
	 * <p>
	 * If you would like to immediately block waiting
	 * for a task, you can use constructions of the form
	 * <tt>result = exec.submit(aCallable).get();</tt>
	 *
	 * @param task the task to submit
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException if the task cannot be
	 *         scheduled for execution
	 * @throws NullPointerException if the task is null
	 */
	<T> Future<T> submit(Callable<T> task);
	
	/**
	 * Submits a Runnable task for execution and returns a Future
	 * representing that task. The Future's <tt>get</tt> method will
	 * return <tt>null</tt> upon <em>successful</em> completion.
	 *
	 * @param task the task to submit
	 * @return a Future representing pending completion of the task
	 * @throws RejectedExecutionException if the task cannot be
	 *         scheduled for execution
	 * @throws NullPointerException if the task is null
	 */
	Future<?> submit(Runnable task);
	
}