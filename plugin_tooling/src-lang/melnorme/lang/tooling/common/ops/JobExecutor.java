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

import melnorme.lang.utils.concurrency.JobFuture;
import melnorme.utilbox.core.fntypes.OperationResult;


public interface JobExecutor {
	
	<RET> JobFuture<OperationResult<RET>> startOpFunction(
			String operationName, boolean schedule, Function<IOperationMonitor, OperationResult<RET>> operation)
	;
	
	default JobFuture<OperationResult<Void>> startOp(
			String operationName, boolean schedule, Operation operation) 
	{
		return startOpFunction(operationName, schedule, operation);
	}
	
	default <RET> JobFuture<OperationResult<RET>> startResultOp(
			String operationName, boolean schedule, ResultOperation<RET> resultOperation)
	{
		return startOpFunction(operationName, schedule, resultOperation);
	}
	
}