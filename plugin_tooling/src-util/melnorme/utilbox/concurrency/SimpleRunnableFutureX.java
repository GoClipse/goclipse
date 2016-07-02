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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.ResultFuture.NonNullResultFuture;
import melnorme.utilbox.core.fntypes.Callable2;
import melnorme.utilbox.core.fntypes.Result;

/**
 * A simple future, completable by means of executing the {@link #run()} method.
 * Cannot be cancelled.
 * 
 * @param <RESULT> The result type returned by the result methods.
 * @param <EXCEPTION> The exception thrown by the result methods. 
 * 
 */
public class SimpleRunnableFutureX<RET, EXC extends Throwable> extends AbstractFutureX<Result<RET, EXC>> 
	implements Runnable, FutureX<Result<RET, EXC>, RuntimeException> {
	
	protected final ResultFuture<Result<RET, EXC>> resultFuture = new NonNullResultFuture<>();
	protected final Callable2<RET, EXC> callable;
	
	public SimpleRunnableFutureX(Callable2<RET, EXC> callable) {
		this.callable = assertNotNull(callable);
	}
	
	@Override
	public void run() {
		Result<RET, EXC> result = callable.callToResult();
		if(result.isException()) {
			assertTrue(!(result.getResultException() instanceof OperationCancellation)); 
		}
		resultFuture.setResult(result);
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
	public Result<RET, EXC> awaitResult() throws InterruptedException {
		try {
			return resultFuture.awaitResult();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public Result<RET, EXC> awaitResult(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		try {
			return resultFuture.awaitResult(timeout, unit);
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public Result<RET, EXC> getResult() {
		try {
			return super.getResult();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public Result<RET, EXC> getResult(long timeout, TimeUnit unit) throws TimeoutException {
		try {
			return super.getResult(timeout, unit);
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	/* -----------------  ----------------- */
	
	public static class SimpleRunnableFuture<RET> extends SimpleRunnableFutureX<RET, RuntimeException> {
		
		public SimpleRunnableFuture(Callable2<RET, RuntimeException> callable) {
			super(callable);
		}
		
	}
	
}