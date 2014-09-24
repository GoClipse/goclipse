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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.utils.UIOperationExceptionHandler;

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
		= "Internal Error executing {0}.";
	
	
	protected final String operationName;
	
	public AbstractUIOperation(String operationName) {
		this.operationName = operationName;
	}
	
	public void executeAndHandle() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			executeOperation();
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handle(ce, operationName, 
				MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName));
		} catch (RuntimeException re) {
			UIOperationExceptionHandler.handleError(true, operationName, 
				MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName), re);
		}
	}
	
	public final void executeOperation() throws CoreException {
		prepareOperation();
		
		try {
			performLongRunningComputation();
			
			performOperation_handleResult();
		} catch (InterruptedException e) {
			return;
		}
	}
	
	protected void prepareOperation() throws CoreException {
	}
	
	protected final void performLongRunningComputation() throws InterruptedException, CoreException {
		if(Display.getCurrent() == null) {
			performLongRunningComputation_inCurrentThread();
			return;
		}
		performLongRunningComputation_inUIThread();
	}
	
	protected void performLongRunningComputation_inCurrentThread() throws CoreException {
		// Perform computation directly in this thread, cancellation won't be possible.
		NullProgressMonitor monitor = new NullProgressMonitor();
		performLongRunningComputation_do(monitor);
	}
	
	protected void performLongRunningComputation_inUIThread() throws InterruptedException, CoreException {
		IProgressService ps = PlatformUI.getWorkbench().getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException, InvocationTargetException {
					monitor.setTaskName(MessageFormat.format(MSG_EXECUTING_OPERATION, operationName));
					
					try {
						performLongRunningComputation_do(monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
					if(monitor.isCanceled()) {
						throw new InterruptedException();
					}
				}
			});
		} catch (InvocationTargetException e) {
			if(e.getCause() instanceof CoreException) {
				throw (CoreException) e.getCause();
			}
			throw new CoreException(LangUIPlugin.createErrorStatus(
				LangCoreMessages.LangCore_error, e.getTargetException()));
		}
	}
	
	protected abstract void performLongRunningComputation_do(IProgressMonitor monitor) throws CoreException;
	
	protected abstract void performOperation_handleResult() throws CoreException;
	
}