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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Like a {@link Future}, but cannot be cancelled, 
 * and guaranteed not to throw {@link ExecutionException}s.  
 */
public interface NonCancellableFuture<DATA> extends BasicFuture<DATA> {
	
	@Override
	default boolean isCancelled() {
		return false;
	}
	
	// Doesn't throw OperationCancellation 
	@Override
	public abstract DATA awaitResult() throws InterruptedException;
	
	// Doesn't throw OperationCancellation
	@Override
	public abstract DATA awaitResult(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException;
	
	
	@Override
	default DATA awaitResult2() {
		try {
			return BasicFuture.super.awaitResult2();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	/* -----------------  ----------------- */
	
	default DATA awaitData(ICancelMonitor cm) throws OperationCancellation {
		
		while(true) {
			cm.checkCancellation();
			
			try {
				return this.awaitResult(100, TimeUnit.MILLISECONDS);
			} catch(InterruptedException e) {
				throw new OperationCancellation();
			} catch(TimeoutException e) {
				continue;
			}
		}
	}
	
}