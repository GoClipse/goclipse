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

import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationResult;

/**
 * An operation which return a RET object upon successfull completion.
 *
 * @param <RET>
 */
public interface ResultOperation<RET> extends Function<IOperationMonitor, OperationResult<RET>> {
	
	public abstract RET callOp(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/** Execute this operation to a {@link OperationResult} object. */
	@Override
	default OperationResult<RET> apply(IOperationMonitor om) {
		try {
			return new OperationResult<>(callOp(om));
		} catch(CommonException e) {
			return new OperationResult<>(null, e);
		} catch(OperationCancellation e) {
			return new OperationResult<>(null, e);
		}
	}
	
	default OperationResult<RET> executeToResult(IOperationMonitor om) {
		return apply(om);
	}
	
	/* -----------------  ----------------- */
	
	default IRunnableFuture2<OperationResult<RET>> toRunnableFuture(IOperationMonitor om) {
		return OperationFuture.fromResultOperation(om, ResultOperation.this);
	}
	
	/* -----------------  ----------------- */
	
	public default ResultOperation<RET> namedOperation(String taskName) {
		return namedOperation(taskName, this);
	}
	
	public static <R> ResultOperation<R> namedOperation(String taskName, ResultOperation<R> subOp) {
		return new ResultOperation<R>() {
			@Override
			public R callOp(IOperationMonitor om) throws CommonException, OperationCancellation {
				try(IOperationSubMonitor subMonitor = om.enterSubTask(taskName)) {
					return subOp.callOp(subMonitor);
				}
			}
		};
	}
	
}