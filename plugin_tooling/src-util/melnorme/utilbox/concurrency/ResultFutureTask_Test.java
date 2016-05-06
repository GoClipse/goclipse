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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.tests.CommonTest;

public class ResultFutureTask_Test extends CommonTest {
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() throws Exception {
		{
			ResultFutureTask<Object, CommonException> resultFutureTask = new ResultFutureTask<>(() -> { 
				throw new CommonException("xxx1");
			});
			ForkJoinPool.commonPool().execute(resultFutureTask);
			
			try {
				resultFutureTask.awaitResult();
				assertFail();
			} catch(CommonException e) {
				assertEquals(e.getMessage(), "xxx1");
				// continue;
			}
		}
		
		{
			ResultFutureTask<Object, RuntimeException> resultFutureTask = new ResultFutureTask<>(() -> { 
				throw new RuntimeException("xxx2");
			});
			ForkJoinPool.commonPool().execute(resultFutureTask);
			
			verifyThrows(() -> resultFutureTask.awaitResult(), RuntimeException.class, "xxx2");
		}
		
		{
			ResultFutureTask<Object, RuntimeException> resultFutureTask = new ResultFutureTask<>(() -> { 
				return "result";
			});
			
			ForkJoinPool.commonPool().execute(resultFutureTask);
			
			assertEquals(resultFutureTask.awaitResult(), "result");
		}

	}
	
}