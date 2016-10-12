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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * IBasicExecutor is a simplification of {@link ExecutorService},  
 * it doesn't have the invokeAll* methods. 
 *
 */
public interface IBasicExecutor  {
	
	/** Same as {@link ExecutorService}{@link #shutdown()}. */
    void shutdown();
    
	/** Same as {@link ExecutorService}{@link #shutdownNow()}. */
    List<Runnable> shutdownNow();
    
    /** Same as {@link ExecutorService}{@link #isShutdown()}. */
    boolean isShutdown();
    
    /** Same as {@link ExecutorService}{@link #isTerminated()}. */
    boolean isTerminated();
    
    /** Same as {@link ExecutorService}{@link #awaitTermination(long, TimeUnit)}. */
    boolean awaitTermination(long timeout, TimeUnit unit)
        throws InterruptedException;
    
	/**
	 * Indefinitely wait for the executor to terminate.
	 * @throws InterruptedException if interrupted.
	 */
	default void awaitTermination() throws InterruptedException {
		while(!awaitTermination(1000, TimeUnit.SECONDS)) {
		}
	}	
}