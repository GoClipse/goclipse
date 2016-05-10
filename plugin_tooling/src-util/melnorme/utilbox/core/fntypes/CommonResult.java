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

public class CommonResult<DATA> {
	
	protected volatile DATA resultValue = null;
	/** Note: resultException is either a EXC, or a RuntimeException. */
	protected volatile Throwable resultException;
	
	public CommonResult(DATA resultValue) {
		this.resultValue = resultValue;
		this.resultException = null;
	}
	
	public CommonResult(@SuppressWarnings("unused") Void dummy, CommonException resultException) {
		this.resultException = resultException;
	}
	
	public CommonResult(@SuppressWarnings("unused") Void dummy, OperationCancellation resultException) {
		this.resultException = resultException;
	}
	
	protected DATA get() throws CommonException, OperationCancellation {
		throwIfExceptionResult();
		return resultValue;
	}
	
	protected void throwIfExceptionResult() throws CommonException, OperationCancellation  {
		if(resultException instanceof OperationCancellation) {
			throw (OperationCancellation) resultException;
		}
		if(resultException instanceof CommonException) {
			throw (CommonException) resultException;
		}
		if(resultException instanceof RuntimeException) {
			throw (RuntimeException) resultException;
		}
		assertFail();
	}
	
}