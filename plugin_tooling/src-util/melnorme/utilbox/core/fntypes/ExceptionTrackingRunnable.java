/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.core.fntypes;


/**
 * A helper {@link Runnable} that tracks an exception thrown during its run method.
 * Also stores the result of the run method, which can be retrieve after it has finished.
 * Retrieving the result will rethrow the original exception, if any was thrown.
 * The result can be accessed by other threads.
 */
/* FIXME: review*/
public abstract class ExceptionTrackingRunnable<R, E extends Throwable> implements Runnable {
	
	protected volatile Throwable throwable;
	protected volatile R result;
	
	@Override
	public final void run() {
		try {
			result = doRun();
		} catch (Throwable throwable) {
			this.throwable = throwable;
		}
	}
	
	@SuppressWarnings("unchecked")
	public final R getResult() throws E {
		if(throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		}
		if(throwable != null) {
			throw (E) throwable;
		}
		return result;
	}
	
	protected abstract R doRun() throws E;

}