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

import melnorme.utilbox.core.fntypes.Callable2;

/**
 * A simple future, completable by means of executing the {@link #run()} method.
 * Cannot be cancelled.
 * 
 */
public class RunnableFuture2<RET> extends AbstractRunnableFuture2<RET> {
	
	protected final Callable2<RET, RuntimeException> callable;
	
	public RunnableFuture2(Callable2<RET, RuntimeException> callable) {
		this.callable = assertNotNull(callable);
	}
	
	@Override
	protected RET internalInvoke() {
		return callable.invoke();
	}
	
}