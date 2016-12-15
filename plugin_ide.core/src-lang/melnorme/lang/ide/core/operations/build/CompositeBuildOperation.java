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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.Operation;
import melnorme.lang.tooling.common.ops.OperationFuture2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class CompositeBuildOperation implements Operation {
	
	protected final Indexable<Operation> operations;
	protected final OperationFuture2<Void> opFuture;
	
	public CompositeBuildOperation(Indexable<Operation> operations) {
		this.operations = assertNotNull(operations);
		this.opFuture = OperationFuture2.fromOperation(this::innerExecute); 
	}
	
	@Override
	public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
		assertTrue(opFuture.canExecute());
		opFuture.callOp(om);
	}
	
	public void innerExecute(IOperationMonitor monitor) throws CommonException, OperationCancellation {
		if(monitor.isCancelled()) {
			return;
		}
		for (Operation subOperation : operations) {
			subOperation.execute(monitor);
		}
	}
	
}