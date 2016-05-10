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

import melnorme.utilbox.core.fntypes.CallableX;

/**
 * A runnable ResultFuture.
 * 
 * This object is not a {@link Runnable}, nor is the run method public,  
 * in order to prevent it being submited to {@link ExecutorService#submit(Runnable)},
 * which would result in a Future with non-working cancellation.
 * 
 * Instead, {@link #submitTo(Executor)} should be used instead.
 * 
 */
public class ResultFutureTask<RET, EXC extends Exception> extends ResultFuture<RET, EXC> {
	
	protected final CallableX<RET, EXC> callable;
	
	public ResultFutureTask(CallableX<RET, EXC> callable) {
		this.callable = callable;
	}
	
	protected final void run() {
		try {
			RET resultValue = doRun();
			setResult(resultValue);
		} catch(RuntimeException e) {
			setRuntimeExceptionResult(e);
		} catch(Exception e) {
			@SuppressWarnings("unchecked")
			EXC exc = (EXC) e;
			setExceptionResult(exc);
		}
	}
	
	protected RET doRun() throws EXC {
		return callable.call();
	}
	
	public ResultFuture<RET, EXC> submitTo(Executor executor) {
		executor.execute(this::run);
		return this;
	}
	
}