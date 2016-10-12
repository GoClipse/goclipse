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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

public interface Future2<RESULT> extends BasicFuture<RESULT> {
	
	/** 
	 * Attempts to cancel execution of this task. This attempt will fail if the task has already completed, 
	 * has already been cancelled, or could not be cancelled for some other reason.
	 * 
	 * If successful, and this task has not started when cancel is called, this task should never run.
	 */
	boolean tryCancel();
	
	/**
	 * @return the result of this future if it is done, throw a cancellation exception othewise.
	 * As a consequence this method is non-blocking. 
	 */
	default RESULT cancelOrGetResult() throws OperationCancellation {
		tryCancel();
		assertTrue(isTerminated());
		return getResult_forTerminated();
	}
	
}