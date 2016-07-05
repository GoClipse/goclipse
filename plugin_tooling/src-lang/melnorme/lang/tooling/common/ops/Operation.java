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
import melnorme.utilbox.concurrency.RunnableFuture2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationResult;
import melnorme.utilbox.core.fntypes.OperationCallable;

public interface Operation {
	
	void execute(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	default OperationCallable<Void> toOperationCallable(IOperationMonitor om) {
		return new OperationCallable<Void>() {
			@Override
			public Void call() throws CommonException, OperationCancellation {
				execute(om);
				return null;
			}
		};
	}
	
	default RunnableFuture2<OperationResult<Void>> toRunnableFuture(IOperationMonitor om) {
		return toOperationCallable(om).toRunnableFuture();
	}
	
	default OperationResult<Void> executeToResult(IOperationMonitor om) {
		return toOperationCallable(om).callToResult();
	}
	
	public static Operation NULL_COMMON_OPERATION = (pm) -> { };
	
	/* -----------------  ----------------- */
	
	default Operation namedOperation(String taskName) {
		return namedOperation(taskName, this);
	}
	
	public static Operation namedOperation(String taskName, Operation subOp) {
		return new Operation() {
			@Override
			public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
				om.runSubTask(taskName, subOp);
			}
		};
	}
	
}