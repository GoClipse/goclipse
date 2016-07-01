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

import org.eclipse.ui.progress.IProgressService;

import melnorme.lang.tooling.common.ops.Operation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
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
	 * The first one is some work being done in a background thread - {@link #doBackgroundComputation(IOperationMonitor)}.
	 * The second one, {@link #handleComputationResult()} is executed after the background works completes, 
	 * and always runs in the UI thread.
	 */
	@Override
	public void execute() throws CommonException, OperationCancellation {
		executeBackgroundOperation();
	}
	
	protected void executeBackgroundOperation() throws CommonException, OperationCancellation {
		getBackgroundOperationExecutor().execute(getBackgroundOperation());
	}
	
	protected WorkbenchOperationExecutor getBackgroundOperationExecutor() {
		return new WorkbenchOperationExecutor();
	}
	
	protected Operation getBackgroundOperation() {
		return Operation.namedOperation(getOperationName(), this::doBackgroundComputation);
	}
	
	/** @return the task name for the progress dialog. This method must be thread-safe. */
	@Override
	public String getOperationName() {
		return super.getOperationName();
	}
	
	/* -----------------  ----------------- */
	
	/** Perform the long running computation. Runs in a background thread. */
	protected abstract void doBackgroundComputation(IOperationMonitor om) 
			throws CommonException, OperationCancellation;
	
}