/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;

import java.util.function.Function;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.JobExecutor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.lang.utils.concurrency.JobFuture;
import melnorme.utilbox.core.fntypes.OperationResult;

public class EclipseJobExecutor implements JobExecutor {
	
	@Override
	public JobFuture<OperationResult<Void>> startOp(
			String operationName, boolean schedule, Operation operation) {
		return new EclipseJobFuture<>(operationName, operation, schedule);
	}
	
	@Override
	public <RET> JobFuture<OperationResult<RET>> startOpFunction(String operationName, 
			boolean schedule, Function<IOperationMonitor, OperationResult<RET>> operation) {
		return new EclipseJobFuture<>(operationName, operation, schedule);
	}
	
}