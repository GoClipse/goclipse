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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.IProgressService;

import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/**
 * An UI operation that optionally performs some work in a background thread, 
 * with the UI waiting using the {@link IProgressService}
 */
public abstract class AbstractUIOperation extends BasicUIOperation {
	
	public boolean runInAsynchronousJob = false;
	
	public AbstractUIOperation(String operationName) {
		super(operationName);
	}
	
	public AbstractUIOperation(String operationName, boolean runInAsynchronousJob) {
		super(operationName);
		this.runInAsynchronousJob = runInAsynchronousJob;
	}
	
	public void executeAndHandleAsynchronouslyInJob() {
		runInAsynchronousJob = true;
		super.executeAndHandle();
	}
	
	/**
	 * Run this UI operation. There are two phases/tasks to it:
	 * The first one is some work being done in a background thread - {@link #doBackgroundComputation(IProgressMonitor)}.
	 * The second one, {@link #handleComputationResult()} is executed after the background works completes, 
	 * and always runs in the UI thread.
	 */
	@Override
	protected void doOperation() throws CoreException, CommonException, OperationCancellation {
		if(!isBackgroundComputationNecessary()) {
			// No need for background computation
			handleComputationResult();
			return;
		}
		runInBackgroundExecutor();
	}
	
	protected boolean isBackgroundComputationNecessary() throws CoreException, CommonException, OperationCancellation {
		return true;
	}
	
	protected void runInBackgroundExecutor() throws CoreException, CommonException, OperationCancellation {
		if(runInAsynchronousJob) {
			runAsynchronouslyInBackgroundJob();
		} else {
			runInProgressServiceExecutor();
		}
	}
	
	protected void runInProgressServiceExecutor() throws CoreException, CommonException, OperationCancellation {
		new ProgressServiceExecutor(this::runBackgroundComputation).execute();
		handleComputationResult();
	}
	
	protected void runBackgroundComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation {
		monitor.setTaskName(getTaskName());
		
		doBackgroundComputation(monitor);
	}
	
	/** @return the task name for the progress dialog. This method must be thread-safe. */
	protected String getTaskName() {
		return MessageFormat.format(MSG_EXECUTING_OPERATION, operationName);
	}
	
	/* -----------------  ----------------- */
	
	protected void runAsynchronouslyInBackgroundJob() {
		Display display = Display.getCurrent();
		
		new Job(getOperationName()) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					runBackgroundComputation(monitor);
					
					display.asyncExec(() -> handleComputationResult_handled());
					
				} catch(CoreException ce) {
					display.asyncExec(() -> handleError(ce));
				} catch(CommonException ce) {
					display.asyncExec(() -> handleError(ce));
				} catch(OperationCancellation e) {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
				
			}
			
		}
		.schedule();
	}
	
	/** Perform the long running computation. Runs in a background thread. */
	protected abstract void doBackgroundComputation(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation;
	
	/* -----------------  ----------------- */
	
	/** Handle long running computation result. This runs in UI thread. */
	protected void handleComputationResult() throws CoreException, CommonException, OperationCancellation {
		// Default: do nothing
	}
	
	protected void handleComputationResult_handled()  {
		try {
			handleComputationResult();
		} catch(CoreException ce) {
			handleError(ce);
		} catch(CommonException ce) {
			handleError(ce);
		} catch(OperationCancellation e) {
			return;
		}
	}
	
}