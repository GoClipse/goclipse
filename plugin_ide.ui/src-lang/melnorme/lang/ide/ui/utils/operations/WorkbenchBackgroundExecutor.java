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
package melnorme.lang.ide.ui.utils.operations;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.ui.utils.operations.RunnableWithProgressOperationAdapter.WorkbenchProgressServiceOpRunner;
import melnorme.lang.tooling.common.ops.CommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.NullOperationMonitor;
import melnorme.lang.tooling.common.ops.ResultOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class WorkbenchBackgroundExecutor {
	
	public <R> R invokeInBackground(ResultOperation<R> op) throws CommonException, OperationCancellation {
		
		AtomicReference<R> resultHolder = new AtomicReference<>();
		
		runInBackground(new CommonOperation() {
			@Override
			public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
				R result = op.executeOp(om);
				resultHolder.set(result);
			}
		});
		
		return resultHolder.get();
	}
	
	public void runInBackground(CommonOperation op) throws CommonException, OperationCancellation {
		if(Display.getCurrent() == null) {
			// Perform computation directly in this thread, but cancellation won't be possible.
			op.execute(new NullOperationMonitor());
		} else {
			new WorkbenchProgressServiceOpRunner(op).execute();
		}
	}
	
}