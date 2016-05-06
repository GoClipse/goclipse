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

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Same as {@link Future} but with more specific paremeterization of the thrown exceptions.
 *
 * @param <RESULT>
 * @param <EXCEPTION>
 */
public interface FutureX<RESULT, EXCEPTION extends Throwable> {
	
	/** See {@link Future#cancel(boolean)} */
    boolean cancel(boolean mayInterruptIfRunning);
    
    /** See {@link Future#isCancelled()} */
    boolean isCancelled();
    
    /** See {@link Future#isDone()} */
    boolean isDone();
	
    /* -----------------  ----------------- */
    
	RESULT awaitResult() 
			throws EXCEPTION, OperationCancellation, InterruptedException;
	
	RESULT awaitResult(long timeout, TimeUnit unit) 
			throws EXCEPTION, OperationCancellation, InterruptedException, TimeoutException;
	
}