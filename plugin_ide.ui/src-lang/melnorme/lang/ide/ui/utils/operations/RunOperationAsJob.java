/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
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

import melnorme.lang.ide.core.utils.operation.CommonProgressRunnable;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class RunOperationAsJob extends Job {
	
	protected final CommonProgressRunnable opRunnable;
	protected final String operationName;
	
	public RunOperationAsJob(String operationName, CommonProgressRunnable opRunnable) {
		super(operationName);
		this.opRunnable = assertNotNull(opRunnable);
		this.operationName = assertNotNull(operationName);
		setUser(true);
	}
	
	@Override
	protected IStatus run(IProgressMonitor pm) {
		try {
			opRunnable.run(pm);
		} catch(CommonException ce) {
			Display.getDefault().asyncExec(
				() -> UIOperationsStatusHandler.handleOperationStatus(getName(), ce));
		} catch(OperationCancellation oc) {
			return Status.CANCEL_STATUS;
		}
		
		return Status.OK_STATUS;
	}
	
}