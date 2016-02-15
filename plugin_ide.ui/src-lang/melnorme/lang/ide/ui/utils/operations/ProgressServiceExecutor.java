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
package melnorme.lang.ide.ui.utils.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.operations.IToolOperation;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class ProgressServiceExecutor {
	
	protected final IToolOperation coreOperation;
	protected final IProgressService progressService;
	
	public ProgressServiceExecutor(IToolOperation coreOperation) {
		this(coreOperation, PlatformUI.getWorkbench().getProgressService());
	}
	
	public ProgressServiceExecutor(IToolOperation coreOperation, IProgressService progressService) {
		this.coreOperation = assertNotNull(coreOperation);
		this.progressService = assertNotNull(progressService);
	}
	
	public void execute() throws CoreException, CommonException, OperationCancellation {
		runUnder(progressService);
	}
	
	public void runUnder(IProgressService ps) throws CoreException, CommonException, OperationCancellation {
		try {
			ps.busyCursorWhile(new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						coreOperation.execute_adapted(monitor);
					} catch(CoreException | CommonException | OperationCancellation e) {
						throw new InvocationTargetException(e);
					}
				}
			});
		} catch (InvocationTargetException ite) {
			try {
				throw ite.getCause();
			} catch(CoreException | CommonException | OperationCancellation original) {
				throw original; // rethrow as original exception
			} catch(Throwable e) {
				// This should not happen either, unless doRun threw a RuntimeException
				throw LangUIPlugin.createCoreException(LangCoreMessages.LangCore_internalError, e);
			} 
		} catch (InterruptedException e) {
			// This should not happen
			throw LangUIPlugin.createCoreException(LangCoreMessages.LangCore_internalError, e);
		}
		
	}
	
}