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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Interface for a blocking/asynchrounous supplier.
 * There is no guarantee, with this interface alone, that the supplier is the same for multiple calls
 * of awaitResult
 *
 * @param <RESULT>
 */
public interface AsyncSupplier<RESULT> {
	
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
	
	/* -----------------  ----------------- */
	
	default RESULT awaitResult(ICancelMonitor cm) throws OperationCancellation {
		
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