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

import java.util.function.Function;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.RunnableFuture2;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;

public interface Operation extends Function<IOperationMonitor, OperationResult<Void>> {
	
	/** Execute this operation. */
	void execute(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/** Execute this operation to a {@link OperationResult} object. */
	@Override
	default OperationResult<Void> apply(IOperationMonitor om) {
		try {
			execute(om);
			return new OperationResult<>(null);
		} catch(CommonException e) {
			return new OperationResult<>(null, e);
		} catch(OperationCancellation e) {
			return new OperationResult<>(null, e);
		}
	}
	
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