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

import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;

public abstract class MonitorRunnableFuture<RET> 
	extends MonitorTaskFuture<RET>
	implements IRunnableFuture2<RET>
{
	
	public MonitorRunnableFuture() {
		super();
	}
	
	public MonitorRunnableFuture(CancelMonitor cancelMonitor) {
		super(cancelMonitor);
	}
	
	public MonitorRunnableFuture(ICancelMonitor cancelMonitor) {
		super(cancelMonitor);
	}
	
	@Override
	public void run() {
		runFuture();
	}
	
}