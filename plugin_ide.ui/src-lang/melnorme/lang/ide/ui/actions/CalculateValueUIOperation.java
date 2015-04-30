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
package melnorme.lang.ide.ui.actions;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class CalculateValueUIOperation<VALUE> extends AbstractUIOperation {
	
	protected volatile VALUE resultValue;
	
	public CalculateValueUIOperation(String operationName) {
		super(operationName);
	}
	
	protected VALUE getResultValue() {
		return resultValue;
	}
	
	public VALUE executeAndGetHandledResult() {
		executeAndHandleResult();
		return getResultValue();
	}
	
	public VALUE executeAndGetValidatedResult() throws CoreException {
		executeOperation();
		return getResultValue();
	}
	
	@Override
	protected void performLongRunningComputation(IProgressMonitor monitor) throws CoreException, CommonException,
			OperationCancellation {
		resultValue = calculateValue(monitor);
	}
	
	protected abstract VALUE calculateValue(IProgressMonitor monitor) throws CoreException, CommonException,
		OperationCancellation;
	
	@Override
	protected void validateComputationResult(boolean isCanceled) throws CoreException {
		if(!isCanceled && resultValue == null) {
			handleNonCanceledNullResult();
		}
	}
	
	protected void handleNonCanceledNullResult() throws CoreException {
	}
	
}