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

import java.util.concurrent.RunnableFuture;

import melnorme.utilbox.core.fntypes.CallableX;

public class ResultFutureTask<RET, EXC extends Exception> extends ResultFuture<RET, EXC> 
	implements RunnableFuture<RET> {
	
	protected final CallableX<RET, EXC> callable;
	
	public ResultFutureTask(CallableX<RET, EXC> callable) {
		this.callable = callable;
	}
	
	@Override
	public final void run() {
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
	
}