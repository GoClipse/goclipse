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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.ExecutorService;

import melnorme.utilbox.concurrency.Futures_Tests.AbstractFutureTest;
import melnorme.utilbox.core.fntypes.SupplierExt;

public class EclipseJobFuture_Test extends AbstractFutureTest<EclipseJobFuture<?>> {
	
	@Override
	protected EclipseJobFuture<?> initFuture(SupplierExt<Object> callable) {
		return new EclipseJobFuture<>("Test", (pm) -> callable.call(), false);
	}
	
	@Override
	protected void submitToExecutor(EclipseJobFuture<?> future, ExecutorService executor) {
		assertTrue(future.isStarted() == false);
		future.start();
	}
	
	@Override
	protected void runFuture(EclipseJobFuture<?> future) {
		future.runFuture();
	}
	
}