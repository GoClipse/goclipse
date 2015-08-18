/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.project_model;

import org.junit.Test;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.utilbox.concurrency.LatchRunnable;

public class BundleModelManagerTest extends CommonCoreTest {
	
	@Test
	public void testShutdown() throws Exception { testShutdown$(); }
	public void testShutdown$() throws Exception {
		BundleModelManager<?> bundleMgr = LangCore_Actual.createBundleModelManager();
		bundleMgr.startManager();
		
		final LatchRunnable latchRunnable = new LatchRunnable();
		bundleMgr.getModelAgent().submit(latchRunnable);
		
		latchRunnable.awaitTaskEntry();
		
		// Test that shutdown happens successfully even with pending task, and no log entries are made.
		bundleMgr.shutdownManager(); 
	}
	
}