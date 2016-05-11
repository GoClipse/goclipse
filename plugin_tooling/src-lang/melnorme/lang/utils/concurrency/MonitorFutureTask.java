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

import melnorme.utilbox.concurrency.FutureTaskX;
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;

/**
 * Wrap a {@link FutureTaskX} around a cancel monitor
 */
public abstract class MonitorFutureTask<EXC extends Exception> {
	
	protected final CancelMonitor cancelMonitor = new CancelMonitor();
	protected final FutureTaskX<?, EXC> asFutureTask;
	
	public MonitorFutureTask() {
		this.asFutureTask = new FutureTaskX<Object, EXC>(this::runTask) {
			@Override
			public void before_cancel(boolean mayInterruptIfRunning) {
				cancelMonitor.cancel();
			}
		};
	}
	
	public final void cancel() {
		asFutureTask.cancel(true);
	}
	
	public boolean isCancelled() {
		return cancelMonitor.isCanceled();
	}
	
	public FutureTaskX<?, EXC> asFutureTask() {
		return asFutureTask;
	}
	
	protected abstract void runTask() throws EXC;
	
}