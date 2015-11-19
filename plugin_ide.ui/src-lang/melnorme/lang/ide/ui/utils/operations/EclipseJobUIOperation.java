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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.utils.operation.CoreOperationRunnable;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;

// TODO need to refactor this so that can be used with any AbstractUIOperation
public abstract class EclipseJobUIOperation extends AbstractUIOperation {
	
	public EclipseJobUIOperation(String operationName) {
		super(operationName);
	}
	
	@Override
	protected void performBackgroundComputation() throws OperationCancellation, CoreException {
		
		new Job(getOperationName()) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					((CoreOperationRunnable) (pm) -> doBackgroundComputation(pm)).coreAdaptedRun(monitor);
				} catch(CoreException ce) {
					Display.getDefault().asyncExec(
						() -> UIOperationsStatusHandler.handleOperationStatus(getOperationName(), ce));
				} catch(OperationCancellation e) {
					return Status.CANCEL_STATUS;
				}
				
				return Status.OK_STATUS;
			}
		}
		.schedule();
	}
	
}