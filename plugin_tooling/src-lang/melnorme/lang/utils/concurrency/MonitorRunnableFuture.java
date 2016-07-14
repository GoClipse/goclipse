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
package melnorme.lang.utils.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.AbstractRunnableFuture2;
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.concurrency.IRunnableFuture2;

/**
 * A {@link IRunnableFuture2} with a cancellation monitor
 */
public abstract class MonitorRunnableFuture 
	extends AbstractRunnableFuture2<Void> 
{
	
	public static MonitorRunnableFuture fromRunnable(Runnable runnable) {
		assertNotNull(runnable);
		return new MonitorRunnableFuture() {
			@Override
			protected void runTask() {
				runnable.run();
			}
		};
	}
	
	/* -----------------  ----------------- */
	
	protected final CancelMonitor cancelMonitor;
	
	public MonitorRunnableFuture() {
		this(new CancelMonitor());
	}
	
	public MonitorRunnableFuture(CancelMonitor cancelMonitor) {
		this.cancelMonitor = assertNotNull(cancelMonitor);
	}
	
	@Override
	protected void handleCancellation() {
		super.handleCancellation();
		cancelMonitor.cancel();
	}
	
	@Override
	protected final Void invokeToResult() {
		runTask();
		return null;
	}
	
	protected abstract void runTask();
	
}