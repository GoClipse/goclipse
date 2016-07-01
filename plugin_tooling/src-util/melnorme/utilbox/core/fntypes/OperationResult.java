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

public class OperationResult<DATA> extends Result<DATA, Exception> {
	
	public OperationResult(DATA resultValue) {
		super(resultValue);
	}
	
	public OperationResult(DATA resultValue, CommonException resultException) {
		super(resultValue, resultException);
	}
	
	public OperationResult(DATA resultValue, OperationCancellation resultException) {
		super(resultValue, resultException);
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