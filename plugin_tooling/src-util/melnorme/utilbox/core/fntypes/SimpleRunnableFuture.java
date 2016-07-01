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
package melnorme.utilbox.core.fntypes;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.AbstractFutureX;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.ResultFuture;
import melnorme.utilbox.concurrency.RunnableFutureX;

/**
 * A simple ResultFuture, completable by means of executing the {@link #run()} method.
 * Cannot be cancelled.
 */
public class SimpleRunnableFuture<RET> extends AbstractFutureX<RET, RuntimeException> 
	implements RunnableFutureX<RET, RuntimeException> {
	
	protected final ResultFuture<RET, RuntimeException> resultFuture = new ResultFuture<>();
	protected final CallableX<RET, RuntimeException> callable;
	
	public SimpleRunnableFuture(CallableX<RET, RuntimeException> callable) {
		this.callable = assertNotNull(callable);
	}
	
	@Override
	public void run() {
		resultFuture.setResult(callable.call());
	}
	
	@Override
	public boolean cancel() {
		throw assertFail();
	}
	
	@Override
	public boolean isCancelled() {
		return resultFuture.isCancelled();
	}
	
	@Override
	public boolean isDone() {
		return resultFuture.isDone();
	}
	
	@Override
	public RET awaitResult() throws RuntimeException, InterruptedException {
		try {
			return resultFuture.awaitResult();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public RET awaitResult(long timeout, TimeUnit unit)
			throws RuntimeException, InterruptedException, TimeoutException {
		try {
			return resultFuture.awaitResult(timeout, unit);
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public RET getResult() {
		try {
			return super.getResult();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public RET getResult(long timeout, TimeUnit unit) throws RuntimeException, TimeoutException {
		try {
			return super.getResult(timeout, unit);
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
}