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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

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
	
	@Override
	default DATA getResult_forTerminated() {
		assertTrue(!isCancelled()); 
		return getResult_forSuccessfulyCompleted();
	}
	
	// Doesn't throw OperationCancellation 
	@Override
	default DATA awaitResult() throws InterruptedException {
		awaitTermination();
		return getResult_forTerminated();
	}
	
	// Doesn't throw OperationCancellation
	@Override
	default DATA awaitResult(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		awaitTermination(timeout, unit);
		return getResult_forTerminated();
	}
	
	@Override
	default DATA awaitResult2() {
		try {
			return BasicFuture.super.awaitResult2();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
}