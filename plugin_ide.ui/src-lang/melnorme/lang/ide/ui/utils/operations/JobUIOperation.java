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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class JobUIOperation extends BasicUIOperation {
	
	protected final Operation operation;
	
	public JobUIOperation(String operationName, Operation operation) {
		super(operationName);
		this.operation = assertNotNull(operation);
	}
	
	@Override
	public void execute() {
		
		Display display = Display.getCurrent();
		
		Operation backgroundOp = Operation.namedOperation(operationName, this::doBackgroundComputation);
		
		new Job(operationName) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					backgroundOp.execute(EclipseUtils.om(monitor));
					
					display.asyncExec(() -> asynchronous_handleComputationResult());
					
				} catch(CommonException ce) {
					display.asyncExec(() -> handleError(ce));
				} catch(OperationCancellation e) {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
				
			}
			
		}.schedule();
	}
	
	/* -----------------  ----------------- */
	
	protected void doBackgroundComputation(IOperationMonitor om) 
			throws CommonException, OperationCancellation {
		operation.execute(om);
	}
	
	protected void asynchronous_handleComputationResult()  {
	}
	
}