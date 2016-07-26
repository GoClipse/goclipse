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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.tests.CommonTest;

public class CompletableResult_Test extends CommonTest {
	
	@Test
	public void testResultFuture() throws Exception { testResultFuture$(); }
	public void testResultFuture$() throws Exception {
		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
		owned.add(() -> newSingleThreadExecutor.shutdown());
		
		{
			CompletableResult<String> completableResult = new CompletableResult<>();
			
			newSingleThreadExecutor.execute(() -> {
				MiscUtil.sleepUnchecked(100);
				completableResult.setResult(null);
			});
			// Note: there is no 100% sure way to ensure that awaitResult is entered before setResult is called
			completableResult.awaitResult();
		}
		
		{
			CompletableResult<String> resultFuture = new CompletableResult<>();
			AtomicReference<Boolean> threw = new AtomicReference<>(false);
			
			CountDownLatch setResultLatch = new CountDownLatch(1);
			LatchRunnable2 latchRunnable = new LatchRunnable2() {
				@Override
				protected void doRun() {
					try {
						LatchRunnable2.awaitLatch(setResultLatch);
						resultFuture.setResult(null);
					} catch(Exception e) {
						threw.set(true);	
					}
				}
			};
			
			newSingleThreadExecutor.execute(latchRunnable);
			resultFuture.setCancelledResult();
			setResultLatch.countDown();
			
			latchRunnable.awaitExit(); // Ensure doRun() is executed
			assertTrue(threw.get() == false);
		}
		
	}
	
}