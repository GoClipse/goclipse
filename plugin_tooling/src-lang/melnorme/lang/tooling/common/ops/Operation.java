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

/**
 * Simpler and more common form of {@link ResultOperation}, which has no return value. 
 */
public interface Operation extends ResultOperation<Void> {
	
	@Override
	default Void callOp(IOperationMonitor om) throws CommonException, OperationCancellation {
		execute(om);
		return null;
	}
	
	/** Execute this operation. */
	void execute(IOperationMonitor om) throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	public static Operation NULL_COMMON_OPERATION = (pm) -> { };
	
	/* -----------------  ----------------- */
	
	@Override
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