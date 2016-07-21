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

/**
 * A progress monitor whose only job is to act as a cancellation monitor, using a timeout.
 */
public class TimeoutCancelMonitor implements ICancelMonitor {
	
	protected int timeoutMillis;
	protected long startTimeMillis = -1;
	
	public TimeoutCancelMonitor(int timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
		this.startTimeMillis = System.currentTimeMillis();
	}
	
	public int getTimeoutMillis() {
		return timeoutMillis;
	}
	
	@Override
	public boolean isCancelled() {
		return System.currentTimeMillis() - startTimeMillis > timeoutMillis;
	}
	
}