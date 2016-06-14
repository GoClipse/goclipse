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

import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface ResultOperation<RESULT> {
	
	public abstract RESULT executeOp(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	public default ResultOperation<RESULT> namedOperation(String taskName) {
		return namedOperation(taskName, this);
	}
	
	public static <R> ResultOperation<R> namedOperation(String taskName, ResultOperation<R> subOp) {
		return new ResultOperation<R>() {
			@Override
			public R executeOp(IOperationMonitor om) throws CommonException, OperationCancellation {
				try(IOperationSubMonitor subMonitor = om.enterSubTask(taskName)) {
					return subOp.executeOp(subMonitor);
				}
			}
		};
	}
	
}