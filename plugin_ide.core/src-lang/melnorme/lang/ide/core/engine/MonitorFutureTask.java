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
package melnorme.lang.ide.core.engine;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import melnorme.utilbox.concurrency.FutureTaskX;

/**
 * Wrap a {@link FutureTaskX} around a cancel monitor
 */
public abstract class MonitorFutureTask<EXC extends Exception> {

	protected final IProgressMonitor cancelMonitor = new NullProgressMonitor();
	protected final FutureTaskX<?, EXC> asFutureTask;
	
	public MonitorFutureTask() {
		this.asFutureTask = new FutureTaskX<Object, EXC>(this::runTask) {
			@Override
			public void before_cancel(boolean mayInterruptIfRunning) {
				cancelMonitor.setCanceled(true);
			}
		};
	}
	
	public final void cancel() {
		asFutureTask.cancel(true);
	}
	
	protected boolean isCancelled() {
		return cancelMonitor.isCanceled();
	}
	
	protected abstract void runTask() throws EXC;
	
}