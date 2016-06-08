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
package melnorme.lang.tooling.common.ops;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface ICommonOperation extends CommonOperation<Void> {
	
	@Override
	default Void executeOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		execute(om);
		return null;
	}
	
	void execute(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	public static ICommonOperation NULL_COMMON_OPERATION = (pm) -> { };
	
	/* -----------------  ----------------- */
	
	/* FIXME: review uses of this, remove*/
	public static abstract class ResultCommonOperation<T> implements ICommonOperation {
		
		protected T result;
		
		@Override
		public final void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
			result = call(om);
		}
		
		protected abstract T call(IOperationMonitor om) throws CommonException, OperationCancellation ;
		
		public T getResult() {
			return result;
		}
		
	}
	
}