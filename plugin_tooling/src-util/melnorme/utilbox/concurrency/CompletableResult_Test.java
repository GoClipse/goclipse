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

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.tests.CommonTest;

public class CompletableResult_Test extends CommonTest {
	
	@Test
	public void testResultFuture() throws Exception { testResultFuture$(); }
	public void testResultFuture$() throws Exception {
		{
			CompletableResult<String> completableResult = new CompletableResult<>();
			
			Executors.newSingleThreadExecutor().execute(() -> {
				MiscUtil.sleepUnchecked(100);
				completableResult.setResult(null);
			});
			// Note: there is no 100% sure way to ensure that awaitResult is entered before setResult is called
			completableResult.awaitResult();
		}
		
		{
			CompletableResult<String> resultFuture = new CompletableResult<>();
			AtomicReference<Boolean> threw = new AtomicReference<>(false);
			
			LatchRunnable2 latchRunnable = new LatchRunnable2(2, 1) {
				@Override
				protected void doRun() {
					try {
						resultFuture.setResult(null);
					} catch(Exception e) {
						threw.set(true);	
					}
				}
			};
			
			Executors.newSingleThreadExecutor().execute(latchRunnable);
			resultFuture.setCancelledResult();
			latchRunnable.entryLatch.countDown();
			latchRunnable.exitLatch.await(); // Ensure doRun() is executed
			assertTrue(threw.get() == false);
		}
		
	}
	
}