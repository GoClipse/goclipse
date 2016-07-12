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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.core.Assert.AssertFailedException;

public abstract class AbstractRunnableFuture<RET> extends AbstractFuture2<RET> implements Runnable {
	
	protected final CompletableResult<RET> completableResult = new CompletableResult<>();
	
	public AbstractRunnableFuture() {
		super();
	}
	
	@Override
	public void run() {
		completableResult.setResult(invokeToResult());
	}
	
	protected abstract RET invokeToResult();
	
	@Override
	public boolean tryCancel() throws AssertFailedException {
		// Hum, perhaps we should be more lenient and just return false?
		throw assertFail();
	}
	
	@Override
	public boolean isCancelled() {
		return completableResult.isCancelled();
	}
	
	@Override
	public boolean isDone() {
		return completableResult.isDone();
	}
	
	@Override
	public RET awaitResult() throws InterruptedException {
		try {
			return completableResult.awaitResult();
		} catch(OperationCancellation e) {
			throw assertFail();
		}
	}
	
	@Override
	public RET awaitResult(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
		try {
			return completableResult.awaitResult(timeout, unit);
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
	
}