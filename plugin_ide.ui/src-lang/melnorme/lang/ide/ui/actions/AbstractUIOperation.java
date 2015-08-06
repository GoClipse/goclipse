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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class AbstractUIOperation {
	
	protected static final String MSG_EXECUTING_OPERATION 
		= "{0}";
	protected static final String MSG_ERROR_EXECUTING_OPERATION 
		= "Error executing `{0}`.";
	protected static final String MSG_INTERNAL_ERROR_EXECUTING_OPERATION 
		= "Internal error executing `{0}`.";
	
	
	protected final String operationName;
	
	public AbstractUIOperation(String operationName) {
		this.operationName = assertNotNull(operationName);
	}
	
	public String getOperationName() {
		return operationName;
	}
	
	public void executeAndHandle() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			try {
				execute();
			} catch(CoreException ce) {
				throw LangCore.createCommonException(ce);
			}
		} catch(CommonException ce) {
			UIOperationsStatusHandler.handleOperationStatus2(
				MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName), ce);
		} catch(RuntimeException re) {
			UIOperationsStatusHandler.handleInternalError(
				MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName), re);
		}
	}
	
	public final void execute() throws CoreException, CommonException {
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
		if(Display.getCurrent() == null) {
			// Perform computation directly in this thread, cancellation won't be possible.
			computationRunnable.doRun_toCoreException(new NullProgressMonitor());
			return;
		}
		computationRunnable.runUnderWorkbenchProgressService();
	}
	
	protected final OperationRunnableWithProgress computationRunnable = new OperationRunnableWithProgress() {
		@Override
		public void doRun(IProgressMonitor monitor) throws CoreException, OperationCancellation, CommonException {
			monitor.setTaskName(getTaskName());
			
			doBackgroundComputation(monitor);
		}
	};
	
	/** @return the task name for the progress dialog. This method must be thread-safe. */
	protected String getTaskName() {
		return MessageFormat.format(MSG_EXECUTING_OPERATION, operationName);
	}
	
	/** Perform the long running computation. Runs in a background thread. */
	protected abstract void doBackgroundComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation;
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
		// Default: do nothing
	}
	
}