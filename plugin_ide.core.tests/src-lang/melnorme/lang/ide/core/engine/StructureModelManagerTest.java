package melnorme.lang.ide.core.engine;
/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/


import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;

import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.engine.StructureModelManager.SourceModelRegistration;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.core.Assert.AssertFailedException;
import melnorme.utilbox.misc.Location;

import org.eclipse.jface.text.Document;
import org.junit.Ignore;
import org.junit.Test;

public class StructureModelManagerTest extends CommonCoreTest {
	
	protected EngineClient engineClient;
	
	public int createUpdateTaskCount;
	public int createDisposeTaskCount;
	
	public int createUpdateTaskCount_EXPECTED;
	public int createDisposeTaskCount_EXPECTED;
	
	protected IStructureModelListener structureListener = new IStructureModelListener() {
		@Override
		public void structureChanged(StructureInfo lockedStructureInfo) {
		}
	};
	
	protected EngineClient initializeTestsEngineClient() {
		createUpdateTaskCount = createUpdateTaskCount_EXPECTED = 0;
		createDisposeTaskCount = createDisposeTaskCount_EXPECTED = 0;
		return new InstrumentedEngineClient();
	}
	
	public class InstrumentedEngineClient extends EngineClient {
		
		public CountDownLatch updateLatch = new CountDownLatch(0);
		public CountDownLatch disposeLatch = new CountDownLatch(0);
		
		@Override
		protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source,
				Location fileLocation) {
			createUpdateTaskCount++;
			return new StructureUpdateTask(structureInfo) {
				
				@Override
				protected SourceFileStructure createSourceFileStructure() {
					awaitUnchecked(updateLatch);
					return null;
				}
			};
		}
		
		@Override
		protected StructureUpdateTask createDisposeTask(StructureInfo structureInfo, Location fileLocation) {
			createDisposeTaskCount++;
			return new StructureUpdateTask(structureInfo) {
				@Override
				protected SourceFileStructure createSourceFileStructure() {
					awaitUnchecked(disposeLatch);
					return null;
				}
			};
		}
		
	}
	
	public void checkTaskDelta(int updateTaskCount, int disposeTaskCount) {
		createUpdateTaskCount_EXPECTED += updateTaskCount;
		createDisposeTaskCount_EXPECTED += disposeTaskCount;
		checkCounts();
	}
	
	public void checkCounts() {
		assertTrue(createUpdateTaskCount == createUpdateTaskCount_EXPECTED);
		assertTrue(createDisposeTaskCount == createDisposeTaskCount_EXPECTED);
	}
	
	/* -----------------  ----------------- */
	
	@Test
	public void testWorkflows() throws Exception { testWorkflows$(); }
	public void testWorkflows$() throws Exception {
		engineClient = initializeTestsEngineClient();
		
		assertTrue(engineClient.getStoredStructureInfo("Blah") == null);
		
		Object key = "Key1";
		
		testBasicFlow(key, new Document(), true);
		
		// Test again, new document
		testBasicFlow(key, new Document(), true);
		
		
		/* -----------------  ----------------- */
		
		testMultipleConnects(key, new Document());
	}
	
	protected SourceModelRegistration testConnectStructureUpdates(Object key, Document doc, boolean initialConnect) {
		if(initialConnect) {
			StructureInfo storedStructureInfo = engineClient.getStoredStructureInfo(key);
			assertTrue(storedStructureInfo == null || !storedStructureInfo.hasConnectedListeners());
		}
		
		SourceModelRegistration registration = engineClient.connectStructureUpdates3(key, doc, structureListener);
		checkTaskDelta(initialConnect ? 1 : 0, 0);
		
		StructureInfo structureInfo = registration.structureInfo;
		assertTrue(structureInfo.hasConnectedListeners());
		assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
		
		return registration;
	}
	
	protected void testDisconnectUpdates(Object key, SourceModelRegistration registration, boolean isWorkingCopy) {
		StructureInfo structureInfo = registration.structureInfo;
		
		registration.dispose();
		verifyThrows(() -> registration.dispose(), AssertFailedException.class);
		checkTaskDelta(0, 1);
		
		assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
		assertTrue(structureInfo.hasConnectedListeners() == isWorkingCopy);
	}
	
	protected void testBasicFlow(Object key, Document doc, boolean initialConnect) {
		checkCounts();
		if(initialConnect) {
			doc.set("");
			checkCounts();
		}
		
		SourceModelRegistration reconcileRegistration = testConnectStructureUpdates(key, doc, initialConnect);
		
		doc.set("");
		checkTaskDelta(1, 0);
		
		doc.set("");
		checkTaskDelta(1, 0);
		
		testDisconnectUpdates(key, reconcileRegistration, initialConnect ? false : true);
		
		if(initialConnect) {
			doc.set("");
			checkCounts();
		}
	}
	
	protected void testMultipleConnects(Object key, Document doc) {
		engineClient = initializeTestsEngineClient();
		
		SourceModelRegistration registration = testConnectStructureUpdates(key, doc, true);
		StructureInfo structureInfo = registration.structureInfo;
		
		checkCounts();
		SourceModelRegistration registration2 = engineClient.connectStructureUpdates3(key, doc, structureListener);
		assertTrue(registration2.structureInfo == structureInfo);
		// Test no extra updates
		checkCounts();
		
		testBasicFlow(key, doc, false);
		
		SourceModelRegistration registration_unmanaged = 
				engineClient.connectStructureUpdates3(key, new Document(), structureListener);
		assertTrue(structureInfo != registration_unmanaged.structureInfo);

		checkTaskDelta(1, 0);
		
		testBasicFlow(key, doc, false);
		
		testDisconnectUpdates(key, registration, true);
		testDisconnectUpdates(key, registration2, false);
		
		testBasicFlow(key, doc, true);
	}
	
	/* -----------------  ----------------- */
	
	public static void awaitUnchecked(CountDownLatch latch) {
		try {
			latch.await();
		} catch(InterruptedException e) {
			throw melnorme.utilbox.core.ExceptionAdapter.unchecked(e);
		}
	}
	
	@Test
	public void testTasks() throws Exception { testTasks$(); }
	public void testTasks$() throws Exception {
		// run test with a real client.
		
		new StructureModelManager_ActualTest().testWorkflows$();
	}
	
	@Ignore
	protected final class StructureModelManager_ActualTest extends StructureModelManagerTest {
		@Override
		protected EngineClient initializeTestsEngineClient() {
			return LangCore_Actual.createEngineClient();
		}
		
		@Override
		public void checkCounts() {
			// Do nothing.
		}
	}
	
}