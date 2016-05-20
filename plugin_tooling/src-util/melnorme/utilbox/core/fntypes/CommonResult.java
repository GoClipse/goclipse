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

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class CommonResult<DATA> extends Result<DATA, Exception> {
	
	public CommonResult(DATA resultValue) {
		super(resultValue);
	}
	
	public CommonResult(Void dummy, CommonException resultException) {
		super(dummy, resultException);
	}
	
	public CommonResult(Void dummy, OperationCancellation resultException) {
		super(dummy, resultException);
	}
	
	@Override
	public DATA get() throws CommonException, OperationCancellation {
		throwIfExceptionResult();
		return resultValue;
	}
	
	@Override
	protected void throwIfExceptionResult() throws CommonException, OperationCancellation  {
		try {
			super.throwIfExceptionResult();
		} catch(OperationCancellation | CommonException | RuntimeException e) {
			throw e;
		} catch(Exception e) {
			assertFail();
		}
	}
	
}