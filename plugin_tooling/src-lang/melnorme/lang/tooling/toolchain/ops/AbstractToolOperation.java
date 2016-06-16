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
package melnorme.lang.tooling.toolchain.ops;


import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public interface AbstractToolOperation<RESULT> {
	
	public abstract RESULT executeToolOperation(IOperationMonitor om) 
			throws CommonException, OperationCancellation, OperationSoftFailure;
	
	default ResultOperation<ToolResponse<RESULT>> toResultOperation() {
		return (om) -> {
			try {
				return new ToolResponse<>(executeToolOperation(om));
			} catch(OperationSoftFailure e) {
				return ToolResponse.newError(e.getMessage());
			}
		};
	}
}