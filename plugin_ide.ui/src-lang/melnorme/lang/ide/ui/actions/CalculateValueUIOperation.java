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
	
	public RESULT executeAndGetValidatedResult() throws CoreException, CommonException {
		execute();
		return getResultValue();
	}
	
	@Override
	protected final void doBackgroundComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation {
		result = doBackgroundValueComputation(monitor);
	}
	
	protected abstract RESULT doBackgroundValueComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation;
	
}