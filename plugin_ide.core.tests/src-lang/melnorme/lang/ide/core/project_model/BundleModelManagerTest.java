package melnorme.lang.ide.core.project_model;

import org.junit.Test;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.utilbox.concurrency.LatchRunnable;

public class BundleModelManagerTest extends CommonCoreTest {
	
	@Test
	public void testShutdown() throws Exception { testShutdown$(); }
	public void testShutdown$() throws Exception {
		BundleModelManager<?, ?> bundleMgr = LangCore_Actual.createBundleModelManager(); 
		
		final LatchRunnable latchRunnable = new LatchRunnable();
		bundleMgr.getModelAgent().submit(latchRunnable);
		
		latchRunnable.awaitTaskEntry();
		
		// Test that shutdown happens successfully even with pending task, and no log entries are made.
		bundleMgr.shutdownManager(); 
	}
	
}