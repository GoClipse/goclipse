/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.core.fntypes.Result;

/**
 * Variant of {@link Future} with a safer and more precise API, with regards to exception throwing:
 * 
 * - Does not throw {@link ExecutionException}. If you want an Exception as a possible result, use {@link Result}.
 * - If cancellation occurs, this throws a checked exception instead of an unchecked exception. 
 *
 * @param <RESULT> The result type returned by the result methods.
 * @param <EXCEPTION> The exception thrown by the result methods. 
 * It should not be {@link InterruptedException} or {@link OperationCancellation} !
 * 
 * @see Future2
 * 
 */
public interface BasicFuture<RESULT> {
	
    /** See {@link Future#isCancelled()} */
    boolean isCancelled();
    
    /** See {@link Future#isDone()} */
    boolean isDone();
    
    /** @return true if completed with a result, without being cancelled. */
	default boolean isCompletedWithResult() {
		return isDone() && !isCancelled();
	}
	
	
    /* -----------------  ----------------- */
    
	RESULT awaitResult() 
			throws OperationCancellation, InterruptedException;
	
	RESULT awaitResult(long timeout, TimeUnit unit) 
			throws OperationCancellation, InterruptedException, TimeoutException;
	
	/** Same as {@link #awaitResult()}, 
	 * but throw InterruptedException as an OperationCancellation. */
	default RESULT awaitResult2() throws OperationCancellation {
		try {
			return awaitResult();
		} catch(InterruptedException e) {
			throw new OperationCancellation();
		}
	}
	
}