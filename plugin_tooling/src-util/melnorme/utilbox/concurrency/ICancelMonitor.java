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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.concurrent.CountDownLatch;

public interface ICancelMonitor {
	
	public boolean isCancelled();
	
	default void checkCancellation() throws OperationCancellation {
		checkCancelation(this);
	}
	
	/* -----------------  ----------------- */
	
	public static void checkCancelation(ICancelMonitor cm) throws OperationCancellation {
		if(cm.isCancelled()) {
			throw new OperationCancellation();
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class NullCancelMonitor implements ICancelMonitor {
		
		@Override
		public boolean isCancelled() {
			return false;
		}
		
	}
	
	public static class CancelMonitor implements ICancelMonitor {
		
		protected volatile boolean isCancelled = false;
		
		@Override
		public boolean isCancelled() {
			return isCancelled;
		}
		
		public void cancel() {
			isCancelled = true;
		}
		
	}
	
	public static class CompositeCancelMonitor extends CancelMonitor {
		
		protected final ICancelMonitor parentCancelMonitor;
		
		public CompositeCancelMonitor(ICancelMonitor parentCancelMonitor) {
			this.parentCancelMonitor = assertNotNull(parentCancelMonitor);
		}
		
		@Override
		public boolean isCancelled() {
			return super.isCancelled() || parentCancelMonitor.isCancelled();
		}
	}
	
	public static class CancelMonitorWithLatch extends CancelMonitor {
		
		protected final CountDownLatch cancelLatch = new CountDownLatch(1);
		
		public CountDownLatch getCancelLatch() {
			return cancelLatch;
		}
		
		@Override
		public void cancel() {
			super.cancel();
			cancelLatch.countDown();
		}
		
	}
	
}