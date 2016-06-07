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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.common.ops.ICommonOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class RunnableWithProgressOperationAdapter {
	
	protected final ICommonOperation coreOperation;
	
	public RunnableWithProgressOperationAdapter(ICommonOperation coreOperation) {
		this.coreOperation = assertNotNull(coreOperation);
	}
	
	public void execute() throws CommonException, OperationCancellation {
		try {
			IRunnableWithProgress runnableWithProgress = new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					try {
						coreOperation.execute(EclipseUtils.om(monitor));
					} catch(CommonException | OperationCancellation e) {
						// wrap exception
						throw new InvocationTargetException(e);
					}
				}
			};
			runRunnableWithProgress(runnableWithProgress);
		} catch (InvocationTargetException ite) {
			try {
				throw ite.getCause();
			} catch(CommonException | OperationCancellation original) {
				throw original; // rethrow as original exception
			} catch(Throwable e) {
				// This should not happen either, unless doRun threw a RuntimeException
				throw new CommonException(LangCoreMessages.LangCore_internalError, e);
			} 
		} catch (InterruptedException e) {
			// This should not happen
			throw new CommonException(LangCoreMessages.LangCore_internalError, e);
		}
	}
	
	protected abstract void runRunnableWithProgress(IRunnableWithProgress progressRunnable)
			throws InvocationTargetException, InterruptedException;
	
	/* -----------------  ----------------- */
	
	public static class ProgressMonitorDialogOpRunner extends RunnableWithProgressOperationAdapter {
		
		protected final ProgressMonitorDialog progressMonitorDialog;
		protected boolean fork = true;
		
		public ProgressMonitorDialogOpRunner(Shell shell, ICommonOperation coreOperation) {
			super(coreOperation);
			progressMonitorDialog = assertNotNull(new ProgressMonitorDialog(shell));
		}
		
		@Override
		protected void runRunnableWithProgress(IRunnableWithProgress progressRunnable)
				throws InvocationTargetException, InterruptedException {
			progressMonitorDialog.run(fork, true, progressRunnable);
		}
	}
	
	public static class WorkbenchProgressServiceOpRunner extends RunnableWithProgressOperationAdapter {
		public WorkbenchProgressServiceOpRunner(ICommonOperation coreOperation) {
			super(coreOperation);
		}
		
		@Override
		protected void runRunnableWithProgress(IRunnableWithProgress progressRunnable)
				throws InvocationTargetException, InterruptedException {
			IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
			progressService.busyCursorWhile(progressRunnable);
		}
	}
	
}