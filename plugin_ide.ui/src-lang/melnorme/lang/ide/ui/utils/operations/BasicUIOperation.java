/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.utils.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class BasicUIOperation {
	
	protected static final String MSG_EXECUTING_OPERATION 
		= "{0}";
	protected static final String MSG_ERROR_EXECUTING_OPERATION 
		= "Error executing `{0}`.";
	protected static final String MSG_INTERNAL_ERROR_EXECUTING_OPERATION 
		= "Internal error executing `{0}`.";
	
	
	protected final String operationName;
	
	public BasicUIOperation(String operationName) {
		this.operationName = assertNotNull(operationName);
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	public void executeAndHandle() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			try {
				execute2();
			} catch(CoreException ce) {
				throw LangCore.createCommonException(ce);
			}
		} catch(CommonException ce) {
			handleStatus(ce);
		} catch(RuntimeException re) {
			handleInternalError(re);
		}
	}
	
	protected void handleStatus(CommonException ce) {
		UIOperationsStatusHandler.handleStatus(
			MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName), ce);
	}
	
	protected void handleInternalError(RuntimeException re) {
		UIOperationsStatusHandler.handleInternalError(
			MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName), re);
	}
	
	protected final void execute2() throws CoreException, CommonException {
		try {
			prepareOperation();
			
			performBackgroundComputation();
			
			handleComputationResult();
		} catch (OperationCancellation e) {
			handleOperationCancellation();
		}
	}
	
	protected void handleOperationCancellation() throws CoreException, CommonException {
	}
	
	protected void prepareOperation() throws CoreException, CommonException, OperationCancellation {
	}
	
	protected void performBackgroundComputation() throws OperationCancellation, CoreException {
	}
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
		// Default: do nothing
	}
	
}