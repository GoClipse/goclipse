/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;

import java.util.concurrent.CountDownLatch;

public interface ICancelMonitor {
	
	public boolean isCanceled();
	
	default void checkCancellation() throws OperationCancellation {
		checkCancelation(this);
	}
	
	/* -----------------  ----------------- */
	
	public static void checkCancelation(ICancelMonitor cm) throws OperationCancellation {
		if(cm.isCanceled()) {
			throw new OperationCancellation();
		}
	}
	
	/* -----------------  ----------------- */
	
	public class NullCancelMonitor implements ICancelMonitor {
		
		@Override
		public boolean isCanceled() {
			return false;
		}
		
	}
	
	public class CancelMonitor implements ICancelMonitor {
		
		protected volatile boolean isCancelled = false;
		protected final CountDownLatch cancelLatch = new CountDownLatch(1);
		
		@Override
		public boolean isCanceled() {
			return isCancelled;
		}
		
		public CountDownLatch getCancelLatch() {
			return cancelLatch;
		}
		
		public void cancel() {
			isCancelled = true;
			cancelLatch.countDown();
		}
		
	}
	
}