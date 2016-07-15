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
package melnorme.lang.tests;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import melnorme.lang.tooling.common.ops.IOperationMonitor.BasicOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.NullOperationMonitor;
import melnorme.lang.tooling.common.ops.OperationFuture.BiDelegatingOperationMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;

public class OperationMonitor_Test extends CommonToolingTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		CancelMonitor topCancelMonitor = new CancelMonitor();
		BasicOperationMonitor om = new NullOperationMonitor(topCancelMonitor);
		BiDelegatingOperationMonitor biDelegatingOM = new BiDelegatingOperationMonitor(om);
		
		assertTrue(biDelegatingOM.isCanceled() == false);
		
		try(IOperationSubMonitor subMonitor = biDelegatingOM.enterSubTask("subTask")) {
			assertTrue(subMonitor.isCanceled() == false);
			
			topCancelMonitor.cancel();
			assertTrue(biDelegatingOM.isCanceled() == true);
			assertTrue(subMonitor.isCanceled() == true);
		}
	} 
	
}