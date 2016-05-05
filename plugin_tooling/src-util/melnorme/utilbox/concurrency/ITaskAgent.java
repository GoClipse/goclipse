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

/**
 * An {@link ITaskAgent} is a concurrency computational entity, 
 * consisting of a single worker thread running in the background,
 * whose purpose is to execute tasks submitted to it in sequence.
 *   
 * It works in a similar way to an event loop, or an Actor in the Actor concurrency model.  
 */
public interface ITaskAgent extends ICommonExecutor {
	
	/** 
	 * Waits for all tasks that have been submitted so far to complete.
	 * It will not wait for tasks submitted after it begins waiting.
	 */
	void waitForPendingTasks() throws InterruptedException;
	
}