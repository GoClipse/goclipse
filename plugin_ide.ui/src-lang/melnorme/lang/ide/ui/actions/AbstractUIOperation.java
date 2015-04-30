/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.text.MessageFormat;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;


public abstract class AbstractUIOperation {
	
	protected static final String MSG_EXECUTING_OPERATION 
		= "Executing {0}.";
	protected static final String MSG_ERROR_EXECUTING_OPERATION 
		= "Error executing {0}.";
	protected static final String MSG_INTERNAL_ERROR_EXECUTING_OPERATION 
		= "Internal error executing {0}.";
	
	
	protected final String operationName;
	
	public AbstractUIOperation(String operationName) {
		this.operationName = assertNotNull(operationName);
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	public final void executeAndHandleResult() {
		executeAndHandle();
	}
	
	protected void executeAndHandle() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			executeOperation();
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handleOperationStatus(
				MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName), ce);
		} catch (RuntimeException re) {
			UIOperationExceptionHandler.handleError(
				MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName), re);
		}
	}
	
	public final void executeOperation() throws CoreException {
		prepareOperation();
		
		try {
			performLongRunningComputation();
			
			validateComputationResult(false);
			
			handleComputationResult();
		} catch (OperationCancellation e) {
			validateComputationResult(true);
		}
	}
	
	protected void prepareOperation() throws CoreException {
	}
	
	protected void performLongRunningComputation() throws OperationCancellation, CoreException {
		if(Display.getCurrent() == null) {
			performLongRunningComputation_inCurrentThread();
			return;
		}
		performLongRunningComputation_usingProgressService();
	}
	
	protected void performLongRunningComputation_inCurrentThread() throws CoreException, OperationCancellation {
		// Perform computation directly in this thread, cancellation won't be possible.
		performLongRunningComputation_toCoreException(new NullProgressMonitor());
	}
	
	protected void performLongRunningComputation_usingProgressService() throws OperationCancellation, CoreException {
		IProgressService ps = PlatformUI.getWorkbench().getProgressService();
		computationRunnable.runUnderProgressService(ps);
	}
	
	protected final OperationRunnableWithProgress computationRunnable = new OperationRunnableWithProgress() {
		@Override
		public void doRun(IProgressMonitor monitor) throws CoreException, OperationCancellation {
			monitor.setTaskName(getTaskName());
			
			performLongRunningComputation_toCoreException(monitor);
		}
	};
	
	/** @return the task name for the progress dialog. This method must be thread-safe. */
	protected String getTaskName() {
		return MessageFormat.format(MSG_EXECUTING_OPERATION, operationName);
	}
	
	protected final void performLongRunningComputation_toCoreException(IProgressMonitor monitor) 
			throws CoreException, OperationCancellation {
		try {
			performLongRunningComputation(monitor);
		} catch(CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	/** Perform the long running computation. Runs in a background thread. */
	protected abstract void performLongRunningComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation;
	
	@SuppressWarnings("unused")
	protected void validateComputationResult(boolean isCanceled) throws CoreException {
	}
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CoreException {
		// Default: do nothing
	}
	
}