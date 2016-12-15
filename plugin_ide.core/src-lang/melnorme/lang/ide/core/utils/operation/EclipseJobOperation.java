/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import melnorme.lang.ide.core.operations.IStatusMessageHandler;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.status.StatusLevel;

/**
 * Run an {@link Operation} under an Eclipse Job.
 * @see EclipseJobFuture
 */
public class EclipseJobOperation extends Job {
	
	protected final IStatusMessageHandler msgHandler;
	protected final Operation operation;
	
	public EclipseJobOperation(String name, IStatusMessageHandler msgHandler, Operation operation) {
		super(name);
		this.msgHandler = assertNotNull(msgHandler);
		this.operation = assertNotNull(operation);
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IOperationMonitor om = EclipseUtils.om(monitor);
		try {
			operation.execute(om);
		} catch(OperationCancellation e) {
			return Status.CANCEL_STATUS;
		} catch(CommonException e) {
			msgHandler.notifyMessage(StatusLevel.ERROR, "Error on " + getName(), e.getMultiLineRender());
			return Status.OK_STATUS;
		}
		return Status.OK_STATUS;
	}
	
}