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

import melnorme.utilbox.core.CommonException;

public class CResult<DATA> extends Result<DATA, CommonException> {
	
	public CResult(DATA resultValue) {
		super(resultValue);
	}
	
	public CResult(DATA resultValue, CommonException resultException) {
		super(resultValue, resultException);
	}
	
	@Override
	protected void throwIfExceptionResult() throws CommonException  {
		try {
			super.throwIfExceptionResult();
		} catch(CommonException | RuntimeException e) {
			throw e;
		} catch(Exception e) {
			assertFail();
		}
	}
	
}