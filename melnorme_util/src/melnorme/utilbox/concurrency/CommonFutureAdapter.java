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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CommonResult;

/**
 * 
 * Adapt a {@link Future} with the {@link Future2} interface
 * 
 */
public class CommonFutureAdapter<VALUE> extends AbstractFutureAdapter<VALUE, CommonResult<VALUE>> {
	
	protected final String defaultErrorMessage; 
	
	public CommonFutureAdapter(Future<VALUE> future, String defaultErrorMessage) {
		super(future);
		this.defaultErrorMessage = assertNotNull(defaultErrorMessage);
	}
	
	@Override
	protected CommonResult<VALUE> createResult(VALUE resultValue) {
		return new CommonResult<VALUE>(resultValue);
	}
	
	@Override
	protected CommonResult<VALUE> createExceptionResult(ExecutionException e) {
		CommonException resultException = new CommonException(defaultErrorMessage, e.getCause());
		return new CommonResult<VALUE>(null, resultException);
	}
	
}