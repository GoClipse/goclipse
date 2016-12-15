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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.CompositeCancelMonitor;

/**
 * A {@link AbstractTaskFuture2} with a cancellation monitor.
 */
public abstract class MonitorTaskFuture<RET> 
	extends AbstractTaskFuture2<RET> 
{
	
	/** The cancel monitor for this Future */
	private final CancelMonitor cancelMonitor;
	
	public MonitorTaskFuture() {
		this(new CancelMonitor());
	}
	
	public MonitorTaskFuture(ICancelMonitor cancelMonitor) {
		this(new CompositeCancelMonitor(cancelMonitor));
	}
	
	public MonitorTaskFuture(CancelMonitor cancelMonitor) {
		this.cancelMonitor = assertNotNull(cancelMonitor);
	}
	
	public CancelMonitor getCancelMonitor() {
		updateCancellationFromMonitor();
		return cancelMonitor;
	}
	
	protected void updateCancellationFromMonitor() {
		boolean monitorCancelled = cancelMonitor.isCancelled();
		if(monitorCancelled && super.isCancelled() == false) {
			tryCancel();
		}
	}
	
	@Override
	public boolean isTerminated() {
		updateCancellationFromMonitor();
		return super.isTerminated();
	}
	
	@Override
	public boolean isCancelled() {
		updateCancellationFromMonitor();
		return super.isCancelled();
	}
	
	@Override
	protected void handleCancellation() {
		cancelMonitor.cancel();
		super.handleCancellation();
	}
	
}