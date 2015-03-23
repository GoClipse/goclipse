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

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;
import melnorme.utilbox.concurrency.OperationCancellation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
			
			performOperation_handleResult();
		} catch (OperationCancellation e) {
			return;
		}
	}
	
	protected void prepareOperation() throws CoreException {
	}
	
	protected final void performLongRunningComputation() throws OperationCancellation, CoreException {
		if(Display.getCurrent() == null) {
			performLongRunningComputation_inCurrentThread();
			return;
		}
		performLongRunningComputation_usingProgressDialog();
	}
	
	protected void performLongRunningComputation_inCurrentThread() throws CoreException, OperationCancellation {
		// Perform computation directly in this thread, cancellation won't be possible.
		NullProgressMonitor monitor = new NullProgressMonitor();
		performLongRunningComputation_do(monitor);
	}
	
	protected void performLongRunningComputation_usingProgressDialog() throws OperationCancellation, CoreException {
		IProgressService ps = PlatformUI.getWorkbench().getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException, InvocationTargetException {
					monitor.setTaskName(MessageFormat.format(MSG_EXECUTING_OPERATION, operationName));
					
					try {
						performLongRunningComputation_inWorkerThread(monitor);
					} catch (OperationCancellation e) {
						throw new InterruptedException(); // Send through as an InterruptedException
					}
				}
			});
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if(cause instanceof CoreException) {
				throw (CoreException) cause;
			}
			if(cause instanceof OperationCancellation) {
				throw (OperationCancellation) cause;
			}
			
			throw LangUIPlugin.createCoreException(LangCoreMessages.LangCore_internalError, cause);
		} catch (InterruptedException e) {
			throw new OperationCancellation();
		}
	}
	
	protected void performLongRunningComputation_inWorkerThread(IProgressMonitor monitor) 
			throws OperationCancellation, InvocationTargetException {
		try {
			performLongRunningComputation_do(monitor);
		} catch (CoreException ce) {
			if(monitor.isCanceled()) {
				throw new OperationCancellation();
			}
			if(ce.getCause() instanceof CancellationException) {
				// In principle this should not happen, because monitor.isCanceled() would be true.
				// But in case some operation code used other monitor or some other means to cancel... 
				throw (CancellationException) ce.getCause();
			}
			throw new InvocationTargetException(ce);
		}
	}
	
	protected abstract void performLongRunningComputation_do(IProgressMonitor monitor) 
			throws CoreException, OperationCancellation;
	
	protected abstract void performOperation_handleResult() throws CoreException;
	
}