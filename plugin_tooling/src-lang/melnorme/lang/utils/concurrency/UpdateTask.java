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
package melnorme.lang.utils.concurrency;

import melnorme.utilbox.concurrency.ICancelMonitor;

public abstract class UpdateTask implements Runnable {

	protected volatile boolean cancelled = false;
	
	public UpdateTask() {
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	protected final ICancelMonitor cm = new ICancelMonitor() {
		@Override
		public boolean isCanceled() {
			return cancelled;
		}
	};
	
	@Override
	public void run() {
		if(cancelled) {
			return;
		}
		
		doRun();
	}
	
	public abstract void doRun();
	
}