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

import java.util.concurrent.Executor;

/**
 * A {@link Runnable} future that can complete by executing it's {@link #run()} method.
 * 
 */
public interface IRunnableFuture2<RET> extends Runnable, ICancellableTask, Future2<RET> {
	
	/** 
	 * Execute this future. Should have no effect if Future is cancelled.
	 */
	@Override
	abstract void run();
	
	@Override
	abstract boolean tryCancel();
	
	/* -----------------  ----------------- */
	
	default IRunnableFuture2<RET> submitTo(Executor executor) {
		executor.execute(this);
		return this;
	}
	
}