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

import melnorme.utilbox.core.fntypes.Callable2;
import melnorme.utilbox.core.fntypes.Result;

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
	
	public static <RET> IRunnableFuture2<RET> toFuture(Callable2<RET, RuntimeException> callable) {
		return new RunnableFuture2<>(callable);
	}
	
	public static <RET, EXC extends Throwable> IRunnableFuture2<Result<RET, EXC>> toResultFuture(
			Callable2<RET, EXC> callable) {
		return toFuture(callable::callToResult);
	}
	
}