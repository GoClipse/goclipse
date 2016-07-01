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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFutureX<RET, EXC extends Throwable> implements FutureX<RET, EXC> {
	
	public AbstractFutureX() {
		super();
	}
	
	public Future<RET> asStdFuture() {
		return asFuture;
	}
	
	protected final Future<RET> asFuture = new Future<RET>() {
		
		@Override
		public boolean cancel(boolean mayInterruptIfRunning) {
			return AbstractFutureX.this.cancel();
		}
		
		@Override
		public boolean isCancelled() {
			return AbstractFutureX.this.isCancelled();
		}
		
		@Override
		public boolean isDone() {
			return AbstractFutureX.this.isDone();
		}
		
		@Override
		public RET get() throws InterruptedException, ExecutionException {
			try {
				return awaitResult();
			} catch(Throwable e) {
				// Don't throw java.util.concurrent.CancellationException because it is a RuntimeException
				throw toExecutionException(e);
			}
		}
		@Override
		public RET get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
			try {
				return awaitResult(timeout, unit);
			} catch(Throwable e) {
				// Don't throw java.util.concurrent.CancellationException because it is a RuntimeException
				throw toExecutionException(e);
			}
		}
		
		public ExecutionException toExecutionException(Throwable e) throws ExecutionException {
			return new ExecutionException(e);
		}
		
	};
	
}