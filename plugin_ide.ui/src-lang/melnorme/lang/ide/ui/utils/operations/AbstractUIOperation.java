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
	
	/**
	 * Run this UI operation. There are two phases/tasks to it:
	 * The first one is some work being done in a background thread - {@link #doBackgroundComputation(IProgressMonitor)}.
	 * The second one, {@link #handleComputationResult()} is executed after the background works completes, 
	 * and always runs in the UI thread.
	 */
	@Override
	protected void doOperation() throws CommonException, OperationCancellation {
		startBackgroundOperation();
		handleComputationResult();
	}
	
	protected void startBackgroundOperation() throws CommonException, OperationCancellation {
		if(!isBackgroundComputationNecessary()) {
			return;
		}
		new ProgressServiceExecutor(this::runBackgroundComputation).execute();
	}
	
	protected boolean isBackgroundComputationNecessary() throws CommonException {
		return true;
	}
	
	protected void runBackgroundComputation(IProgressMonitor pm) throws CommonException, OperationCancellation {
		pm.setTaskName(getTaskName());
		
		doBackgroundComputation(pm);
	}
	
	/** @return the task name for the progress dialog. This method must be thread-safe. */
	protected String getTaskName() {
		return MessageFormat.format(MSG_EXECUTING_OPERATION, operationName);
	}
	
	/* -----------------  ----------------- */
	
	/** Perform the long running computation. Runs in a background thread. */
	protected abstract void doBackgroundComputation(IProgressMonitor pm) 
			throws CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CommonException {
		// Default: do nothing
	}
	
}