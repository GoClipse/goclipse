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
package melnorme.lang.ide.ui.utils.operations;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class CalculateValueUIOperation<RESULT> extends AbstractUIOperation {
	
	protected volatile RESULT result;
	
	public CalculateValueUIOperation(String operationName) {
		super(operationName);
	}
	
	public RESULT getResultValue() {
		return result;
	}
	
	public RESULT executeAndGetHandledResult() {
		executeAndHandle();
		return getResultValue();
	}
	
	@Override
	protected final void doBackgroundComputation(IOperationMonitor om) 
			throws CommonException, OperationCancellation {
		result = doBackgroundValueComputation(om);
	}
	
	protected abstract RESULT doBackgroundValueComputation(IOperationMonitor om) 
			throws CommonException, OperationCancellation;
	
}