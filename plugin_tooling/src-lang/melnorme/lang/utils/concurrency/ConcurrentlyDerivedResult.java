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
package melnorme.lang.utils.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.fntypes.Result;

/**
 * Similar to ConcurrentlyDerivedData, but with a {@link Result}.
 * 
 * Note {@link #_invariant_()}
 */
public class ConcurrentlyDerivedResult<VALUE, EXC extends Exception, SELF> 
	extends ConcurrentlyDerivedData<Result<VALUE, EXC>, SELF> {
	
	public ConcurrentlyDerivedResult() {
		super();
		internalSetData(new Result<>(null));
		_invariant_();
	}
	
	public void _invariant_() {
		assertNotNull(getStoredData());
	}
	
	@Override
	public void internalSetData(Result<VALUE, EXC> newData) {
		super.internalSetData(assertNotNull(newData));
	}
	
	/* -----------------  ----------------- */
	
	public void setUpdateTask(ResultUpdateTask newUpdateTask) {
		super.setUpdateTask(newUpdateTask);
	}
	
	@Override
	public void setUpdateTask(DataUpdateTask<Result<VALUE, EXC>> newUpdateTask) {
		assertTrue(newUpdateTask instanceof ConcurrentlyDerivedResult.ResultUpdateTask);
		super.setUpdateTask(newUpdateTask);
	}
	
	public abstract class ResultUpdateTask extends DataUpdateTask<Result<VALUE, EXC>> {
		
		public ResultUpdateTask(String taskDisplayName) {
			super(ConcurrentlyDerivedResult.this, taskDisplayName);
		}
		
		@Override
		protected Result<VALUE, EXC> createNewData() throws OperationCancellation {
			try {
				return new Result<>(doCreateNewData());
			} catch(RuntimeException e) {
				// This behavior is illegal, but try to handle it as gracefully as possible
				throw e; 
			} catch(Exception e) {
				// We can't catch EXC directly because it's a type parameter
				@SuppressWarnings("unchecked")
				EXC exc = (EXC) e;
				return new Result<>(null, exc);
			}
		}
		
		protected abstract VALUE doCreateNewData() throws EXC, OperationCancellation;
		
	}
	
	public VALUE awaitUpdatedResultValue(IOperationMonitor om) throws OperationCancellation, EXC {
		return awaitUpdatedData(om).get();
	}
	
}