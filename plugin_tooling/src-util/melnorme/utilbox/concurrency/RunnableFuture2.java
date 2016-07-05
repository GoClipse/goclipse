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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

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
public class RunnableFuture2<RET> extends AbstractRunnableFuture<RET> {
	
	protected final Callable2<RET, RuntimeException> callable;
	
	public RunnableFuture2(Callable2<RET, RuntimeException> callable) {
		this.callable = assertNotNull(callable);
	}
	
	@Override
	protected RET invokeToResult() {
		return callable.invoke();
	}
	
	/* -----------------  ----------------- */
	
	
	public static <RET, EXC extends Throwable> ResultRunnableFuture<RET, EXC> toResultRunnableFuture(
			Callable2<RET, EXC> callable) {
		return new ResultRunnableFuture<RET, EXC>(callable);
	}
	
	public static class ResultRunnableFuture<RET, EXC extends Throwable> extends RunnableFuture2<Result<RET, EXC>> {
		
		public ResultRunnableFuture(Callable2<RET, EXC> callable) {
			super(callable::callToResult);
		}
		
	}
	
}