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

import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.ide.core.utils.operation.EclipseJobExecutor;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.process.ExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessHelper_Test.TestsExternalProcessHelper;

public class LanguageServerHandler_Test extends CommonCoreTest {
	
	
	protected ExecutorService executor;
	
	protected ExecutorService getExecutor() {
		if(executor != null) {
			executor.shutdownNow();
		}
		executor = Executors.newSingleThreadExecutor();
		return executor;
	}
	
	@Test
	public void test() throws Exception {
		for(int i = 0; i < 10; i++) {
			test$();
		}
	}
	
	public void test$() throws Exception {
		
		CountDownLatch doCreatelatch = new CountDownLatch(1);
		
		LanguageServerHandler<LanguageServerInstance> lsHandler = new LanguageServerHandler<LanguageServerInstance>(
				new EclipseJobExecutor(), LangCore.getToolManager()
		) {
			
			@Override
			public Path getServerPath() throws CommonException {
				return MiscUtil.createValidPath("LanguageServerHandler_Test/blah");
			}
			
			@Override
			protected LanguageServerInstance doCreateServerInstance(IOperationMonitor om)
					throws CommonException, OperationCancellation {
				try {
					doCreatelatch.countDown();
					
					new CountDownLatch(1).await(); // await forever until interrupted
				} catch(InterruptedException e) {
					ExternalProcessHelper eph = new TestsExternalProcessHelper(true, false, new CancelMonitor());
					return new LanguageServerInstance(getServerPath(), eph) {
						@Override
						protected String getLanguageServerName() {
							return "Mock";
						}
					}; // This is the exit path the test expects
				} 
				throw assertFail();
			}
		};
		
		try {
			IRunnableFuture2<Result<Object, RuntimeException>> future = IRunnableFuture2.toResultFuture(() -> {
				try {
					Thread.currentThread().setName(LanguageServerHandler_Test.class.getSimpleName());
					lsHandler.getReadyServerInstance();
				} catch(CommonException e) {
					assertFail();
				} catch(OperationCancellation e) {
					return null;
				}
				throw assertFail();
			});
			getExecutor().execute(future);
			
			doCreatelatch.await();
			
			lsHandler.stopServerInstance();
			future.awaitResult().get(); // Test that getReadyServerInstance is cancelled due to stopServerInstance
		} finally {
			lsHandler.close();
		}
	}
	
}