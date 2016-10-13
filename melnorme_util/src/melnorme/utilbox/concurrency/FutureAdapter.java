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

import melnorme.utilbox.core.fntypes.Result;

public class FutureAdapter<VALUE> extends AbstractFutureAdapter<VALUE, Result<VALUE, Throwable>> {
	
	public FutureAdapter(Future<VALUE> future) {
		super(future);
	}
	
	@Override
	protected Result<VALUE, Throwable> createResult(VALUE resultValue) {
		return Result.fromValue(resultValue);
	}
	
	@Override
	protected Result<VALUE, Throwable> createExceptionResult(ExecutionException e) {
		return Result.fromException(e.getCause());
	}
	
}