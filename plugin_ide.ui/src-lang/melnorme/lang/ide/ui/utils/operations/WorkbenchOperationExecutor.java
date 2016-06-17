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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class WorkbenchOperationExecutor {
	
	protected final boolean allowBackgroundAlready;
	protected final boolean executeInUIOnly;
	
	public WorkbenchOperationExecutor() {
		this(false);
	}
	
	public WorkbenchOperationExecutor(boolean executeInUIOnly) {
		super();
		this.executeInUIOnly = executeInUIOnly;
		this.allowBackgroundAlready = !executeInUIOnly;
	}
	
	protected final void runRunnableWithProgress(IRunnableWithProgress progressRunnable)
			throws InvocationTargetException, InterruptedException {
		
		if(allowBackgroundAlready && Display.getCurrent() == null) {
			assertTrue(executeInUIOnly == false);
			// Perform computation directly in this thread, but cancellation won't be possible.
			progressRunnable.run(new NullProgressMonitor());
		} else {
			
			assertTrue(Display.getCurrent() != null);
			
			doRunRunnableWithProgress(progressRunnable);
		}
	}
	
	protected void doRunRunnableWithProgress(IRunnableWithProgress progressRunnable)
			throws InvocationTargetException, InterruptedException {
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		progressService.run(!executeInUIOnly, true, progressRunnable);
	}
	
	public void execute(CommonOperation coreOperation) throws CommonException, OperationCancellation {
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
	
	public <R> R callInBackground(Function<IOperationMonitor, R> op) {
		
		AtomicReference<R> resultHolder = new AtomicReference<>();
		
		CommonOperation opWrapper = new CommonOperation() {
			@Override
			public void execute(IOperationMonitor om) {
				R result = op.apply(om);
				resultHolder.set(result);
			}
		};
		try {
			execute(opWrapper);
		} catch(CommonException | OperationCancellation e) {
			throw assertFail();
		}
		
		return resultHolder.get();
	}
	
	public <R> R invokeInBackground(ResultOperation<R> op) throws CommonException, OperationCancellation {
		
		AtomicReference<R> resultHolder = new AtomicReference<>();
		
		execute(new CommonOperation() {
			@Override
			public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
				R result = op.executeOp(om);
				resultHolder.set(result);
			}
		});
		
		return resultHolder.get();
	}
	
	/* -----------------  ----------------- */
	
	public static class ProgressMonitorDialogOpRunner extends WorkbenchOperationExecutor {
		
		protected final ProgressMonitorDialog progressMonitorDialog;
		
		public ProgressMonitorDialogOpRunner(Shell shell) {
			super();
			progressMonitorDialog = assertNotNull(new ProgressMonitorDialog(shell));
		}
		
		@Override
		protected void doRunRunnableWithProgress(IRunnableWithProgress progressRunnable)
				throws InvocationTargetException, InterruptedException {
			progressMonitorDialog.run(!executeInUIOnly, true, progressRunnable);
		}
	}
	
	
}