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

/**
 * Variant of {@link Future} with a safer and more precise API, with regards to exception throwing:
 * 
 * - The exception type for the result is parameterized, so the generic {@link ExecutionException} is not used.
 * - It throws a checked exception instead of an unchecked exception if cancellation occurs. 
 *
 * @param <RESULT> The result type returned by the result methods.
 * @param <EXCEPTION> The exception thrown by the result methods. 
 * It should not be {@link InterruptedException} or {@link OperationCancellation} !
 * 
 */
public interface FutureX<RESULT, EXCEPTION extends Throwable> {
	
	/** See {@link Future#cancel(boolean)} */
    boolean cancel();
    
    /** See {@link Future#isCancelled()} */
    boolean isCancelled();
    
    /** See {@link Future#isDone()} */
    boolean isDone();
	
    /* -----------------  ----------------- */
    
	RESULT awaitResult() 
			throws EXCEPTION, OperationCancellation, InterruptedException;
	
	RESULT awaitResult(long timeout, TimeUnit unit) 
			throws EXCEPTION, OperationCancellation, InterruptedException, TimeoutException;
	
	/** Same as {@link #awaitResult()}, 
	 * but throw InterruptedException as an OperationCancellation. */
	default RESULT getResult() 
			throws EXCEPTION, OperationCancellation {
		try {
			return awaitResult();
		} catch(InterruptedException e) {
			throw new OperationCancellation();
		}
	}
	
	/** Same as {@link #awaitResult(long, TimeUnit)}, 
	 * but throw InterruptedException as an OperationCancellation. */
	default RESULT getResult(long timeout, TimeUnit unit) 
			throws EXCEPTION, OperationCancellation, TimeoutException {
		try {
			return awaitResult(timeout, unit);
		} catch(InterruptedException e) {
			throw new OperationCancellation();
		}
	}
	
}