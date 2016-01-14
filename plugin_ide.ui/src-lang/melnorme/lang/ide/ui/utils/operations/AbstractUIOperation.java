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

import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.progress.IProgressService;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/**
 * An UI operation that optionally performs some work in a background thread, 
 * with the UI waiting using the {@link IProgressService}
 */
public abstract class AbstractUIOperation extends BasicUIOperation {
	
	public AbstractUIOperation(String operationName) {
		super(operationName);
	}
	
	@Override
	protected final void doOperation() throws CoreException, CommonException, OperationCancellation {
		performBackgroundComputation();
		handleComputationResult();
	}
	
	protected void performBackgroundComputation() throws CoreException, OperationCancellation {
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
	
	/* -----------------  ----------------- */
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
		// Default: do nothing
	}
	
}