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
package melnorme.lang.tooling.common.ops;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.RunnableFuture2;
import melnorme.utilbox.core.fntypes.OperationCallable;
import melnorme.utilbox.core.fntypes.OperationResult;

public abstract class MonitorOperationCallable<RET> implements OperationCallable<RET> {
	
	protected final IOperationMonitor om;
	
	public MonitorOperationCallable(IOperationMonitor om) {
		this.om = assertNotNull(om);
	}
	
	public IRunnableFuture2<OperationResult<RET>> toRunnableFuture() {
		/* FIXME: review interaction with OperationMonitor */
		return RunnableFuture2.toFuture(this::callToResult);
	}
	
}