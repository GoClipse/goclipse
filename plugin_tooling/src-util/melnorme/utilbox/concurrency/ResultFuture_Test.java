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

import java.util.concurrent.Executors;

import org.junit.Test;

import melnorme.utilbox.misc.MiscUtil;
import melnorme.utilbox.tests.CommonTest;

public class ResultFuture_Test extends CommonTest {
	
	@Test
	public void testResultFuture() throws Exception { testResultFuture$(); }
	public void testResultFuture$() throws Exception {
		
		ResultFuture<String> resultFuture = new ResultFuture<>();
		
		Executors.newSingleThreadExecutor().execute(() -> {
			MiscUtil.sleepUnchecked(100);
			resultFuture.setResult(null);
		});
		// No 100% sure to ensure that getResult is entered before setResult is called
		resultFuture.getResult();
	}
	
}