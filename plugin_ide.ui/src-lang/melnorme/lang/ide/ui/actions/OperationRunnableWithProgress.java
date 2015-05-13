/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.actions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.lang.reflect.InvocationTargetException;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.concurrency.OperationCancellation;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

public abstract class OperationRunnableWithProgress implements IRunnableWithProgress {
	
	protected volatile Exception exceptionResult;
	
	public void runUnderWorkbenchProgressService() throws OperationCancellation, CoreException {
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		runUnderProgressService(progressService);
	}
	
	public void runUnderProgressService(IProgressService ps) throws OperationCancellation, CoreException {
		try {
			ps.busyCursorWhile(this);
		} catch (InterruptedException e) {
			// This should not happen
			throw LangUIPlugin.createCoreException(LangCoreMessages.LangCore_internalError, e);
		} catch (InvocationTargetException e) {
			// This should not happen either, unless doRun threw a RuntimeException
			throw LangUIPlugin.createCoreException(LangCoreMessages.LangCore_internalError, e);
		}
		
		rethrowResultException();
	}
	
	public void rethrowResultException() throws CoreException, OperationCancellation {
		if(exceptionResult != null) {
			try {
				throw exceptionResult;
			} catch(CoreException | OperationCancellation e) {
				throw e; // rethrow
			} catch(Exception e) {
				throw assertFail();
			} 
		}
	}
	
	@Override
	public final void run(IProgressMonitor monitor) {
		try {
			doRun(monitor);
		} catch(OperationCancellation oc) {
			exceptionResult = new OperationCancellation();
		} catch(CoreException ce) {
			if(monitor.isCanceled()) {
				exceptionResult = new OperationCancellation();
			} else {
				exceptionResult = ce;
			}
		} catch(OperationCanceledException oce) {
			exceptionResult = new OperationCancellation();
		}
	}
	
	protected abstract void doRun(IProgressMonitor monitor) throws CoreException, OperationCancellation;
	
	
}