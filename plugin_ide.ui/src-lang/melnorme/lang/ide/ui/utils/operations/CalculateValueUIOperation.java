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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.lang.tooling.data.Severity;
import melnorme.lang.tooling.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor;
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
	
	public RESULT executeAndGetValidatedResult() throws CoreException, CommonException {
		assertTrue(Display.getCurrent() != null);
		
		execute0();
		return getResultValue();
	}
	
	@Override
	protected final void doBackgroundComputation(IOperationMonitor om) 
			throws CommonException, OperationCancellation {
		result = doBackgroundValueComputation(om);
	}
	
	protected abstract RESULT doBackgroundValueComputation(IOperationMonitor monitor) 
			throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	protected void dialogInfo(String message) {
		UIOperationsStatusHandler.displayStatusMessage(operationName, Severity.INFO, message);  
	}
	
	protected void dialogError(String message) {
		UIOperationsStatusHandler.displayStatusMessage(operationName, Severity.ERROR, message);  
	}
	
	public static ICancelMonitor cm(IProgressMonitor pm) {
		return EclipseUtils.cm(pm);
	}
	
}