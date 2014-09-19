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
	
	public void executeHandled() {
		assertTrue(Display.getCurrent() != null);
		
		try {
			executeOperation();
		} catch (CoreException ce) {
			UIOperationExceptionHandler.handle(ce, operationName, 
				MessageFormat.format(MSG_ERROR_EXECUTING_OPERATION, operationName));
		} catch (RuntimeException re) {
			UIOperationExceptionHandler.doHandleException(operationName, 
				MessageFormat.format(MSG_INTERNAL_ERROR_EXECUTING_OPERATION, operationName), re);
		}
	}
	
	public void executeOperation() throws CoreException {
		try {
			performLongRunningComputation();
		} catch (InterruptedException e) {
			return;
		}
	}
	
	protected void performLongRunningComputation() throws InterruptedException, CoreException {
		if(Display.getCurrent() == null) {
			// Perform computation directly in this thread.
			performLongRunningComputation_do();
			return;
		}
		IProgressService ps = PlatformUI.getWorkbench().getProgressService();
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InterruptedException {
					monitor.setTaskName(MessageFormat.format(MSG_EXECUTING_OPERATION, operationName));
					
					// TODO: need to performLongRunningOp in executor, so that we can check monitor.
					performLongRunningComputation_do();
					if(monitor.isCanceled()) {
						throw new InterruptedException();
					}
				}
			});
		} catch (InvocationTargetException e) {
			throw new CoreException(LangUIPlugin.createErrorStatus(
				LangCoreMessages.LangCore_error, e.getTargetException()));
		}
	}
	
	protected void performLongRunningComputation_do() {
		
	}
	
}