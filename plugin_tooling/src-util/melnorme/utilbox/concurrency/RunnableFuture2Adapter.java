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
package melnorme.utilbox.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.RunnableFuture;

import melnorme.utilbox.core.fntypes.Result;

/**
 * Adapt a {@link RunnableFuture} with the {@link Future2} interface
 */
public class RunnableFuture2Adapter<VALUE> extends Future2Adapter<VALUE> 
	implements IRunnableFuture2<Result<VALUE, Throwable>> 
{
	
	protected final RunnableFuture<VALUE> future;
	protected volatile boolean hasStarted;
	
	public RunnableFuture2Adapter(RunnableFuture<VALUE> result) {
		super(result);
		this.future = assertNotNull(result);
	}
	
	@Override
	public void run() {
		assertTrue(hasStarted == false);
		hasStarted = true;
		future.run();
	}
	
	@Override
	public boolean canExecute() {
		return !hasStarted;
	}
	
}