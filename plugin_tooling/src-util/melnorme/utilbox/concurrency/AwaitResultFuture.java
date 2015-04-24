/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Basic {@link Future} that terminates 
 * when {@link #setResult(Object)} or {@link #cancel(boolean)} are called.
 */
public class AwaitResultFuture<V> implements Future<V> {
	
	protected final CountDownLatch latch = new CountDownLatch(1);
	
    protected volatile boolean isDone = false;
    protected volatile boolean cancelled = false;
	protected volatile V resultValue;
	
	@Override
	public boolean isDone() {
		return isDone;
	}
	
	@Override
	public V get() throws InterruptedException {
		latch.await();
		return resultValue;
	}
	
	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException {
		latch.await(timeout, unit);
		return resultValue;
	}
	
	public void setResult(V resultValue) {
		synchronized (this) {
			if(isDone) {
				return;
			}
			this.resultValue = resultValue;
			isDone = true;
			latch.countDown();
		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		synchronized (this) {
			if(isDone) {
				return false;
			}
			cancelled = true;
			isDone = true;
			latch.countDown();
			return true;
		}
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
}