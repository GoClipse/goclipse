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
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.ide.core.utils.operation.EclipseJobExecutor;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class LanguageServerHandler_Test extends CommonCoreTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		LanguageServerHandler<LanguageServerInstance> lsHandler = new LanguageServerHandler<LanguageServerInstance>(
				new EclipseJobExecutor(), LangCore.getToolManager()
		) {

			@Override
			protected LanguageServerInstance doCreateServerInstance(IOperationMonitor om)
					throws CommonException, OperationCancellation {
				try {
					new CountDownLatch(1).await(); // await forever until interrupted
				} catch(InterruptedException e) {
					return null;
				} 
				throw assertFail();
			}
		};
		
		try {
			Thread thread = new Thread(() -> {
				try {
					Thread.currentThread().setName(LanguageServerHandler_Test.class.getSimpleName());
					lsHandler.getReadyServerInstance();
				} catch(CommonException e) {
					assertFail();
				} catch(OperationCancellation e) {
					return;
				}
				assertFail();
			});
			thread.start();
			
			lsHandler.stopServerInstance();
			thread.join(); // Test that getReadyServerInstance is cancelled
		} finally {
			lsHandler.close();
		}
	}
	
}
