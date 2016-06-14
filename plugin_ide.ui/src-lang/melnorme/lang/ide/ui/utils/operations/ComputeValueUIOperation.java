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
package melnorme.lang.ide.ui.utils.operations;


import melnorme.lang.ide.ui.utils.operations.CalculateValueUIOperation;
import melnorme.lang.ide.ui.utils.operations.WorkbenchBackgroundExecutor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.OperationCallable;

/**
 * A special UI operation that can run both in the UI thread and outside it,
 * and it might have additional capabilities if running on UI thread.
 * 
 * @see CalculateValueUIOperation 
 */
public abstract class ComputeValueUIOperation<RESULT> implements OperationCallable<RESULT> {
	
	public ComputeValueUIOperation() {
		super();
	}
	
	protected <R> R invokeInBackground(ResultOperation<R> godefOp)
			throws CommonException, OperationCancellation {
		return new WorkbenchBackgroundExecutor().invokeInBackground(godefOp);
	}

}