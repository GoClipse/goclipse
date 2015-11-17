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


import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import melnorme.lang.ide.core.engine.StructureModelManager.SourceModelRegistration;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.tooling.ast.ParserError;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.Assert.AssertFailedException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.StrictDisposable;

public class StructureModelManagerTest extends CommonCoreTest {
	
	static {
		assertTrue(StructureModelManager_WithActualTest.class != null); // Ensure class exists
	}
	
	/* -----------------  ----------------- */
	
	protected EngineClient engineClient;
	protected InstrumentedEngineClient engineClient2;
	
	public int createUpdateTaskCount;
	public int createUpdateTaskCount_EXPECTED;
	
	protected IStructureModelListener structureListener = new IStructureModelListener() {
		@Override
		public void structureChanged(StructureInfo lockedStructureInfo) {
		}
	};
	
	protected void initializeTestsEngineClient() {
		createUpdateTaskCount = createUpdateTaskCount_EXPECTED = 0;
		engineClient2 = new InstrumentedEngineClient();
		engineClient = engineClient2; 
	}
	
	public class InstrumentedEngineClient extends EngineClient {
		
		@Override
		protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source,
				Location fileLocation) {
			createUpdateTaskCount++;
			
			return createUpdateTask.apply(structureInfo);
		}
		
	}
	
	protected final SourceFileStructure defaultSourceFileStructure = new SourceFileStructure(null, null, 
		(Indexable<ParserError>) null);
	
	protected Function<StructureInfo, StructureUpdateTask> createUpdateTask = (structureInfo) -> {
		return new StructureUpdateTask(structureInfo) {
			@Override
			protected SourceFileStructure createNewData() {
				return defaultSourceFileStructure;
			}
		};
	};
	
	public void checkTaskDelta(int updateTaskCount) {
		createUpdateTaskCount_EXPECTED += updateTaskCount;
		checkCounts();
	}
	
	public void checkCounts() {
		assertTrue(createUpdateTaskCount == createUpdateTaskCount_EXPECTED);
	}
	
	/* -----------------  ----------------- */
	
	int docSetCount = 0;
	
	protected void documentSet(Document doc) {
		doc.set("" + (docSetCount++));
	}
	
	@Test
	public void testWorkflows() throws Exception { testWorkflows$(); }
	public void testWorkflows$() throws Exception {
		initializeTestsEngineClient();
		
		assertTrue(engineClient.getStoredStructureInfo("Blah") == null);
		
		Object key = "Key1";
		
		testBasicFlow(key, new Document(), true);
		
		// Test again, new document
		testBasicFlow(key, new Document(), true);
		
		
		/* -----------------  ----------------- */
		
		testMultipleConnects(key, new Document());
	}
	
	protected void testBasicFlow(Object key, Document doc, boolean initialConnect) throws InterruptedException {
		checkCounts();
		if(initialConnect) {
			documentSet(doc);
			checkCounts();
		}
		
		SourceModelRegistration reconcileRegistration = testConnectStructureUpdates(key, doc, initialConnect);
		
		documentSet(doc);
		checkTaskDelta(1);
		
		documentSet(doc);
		checkTaskDelta(1);
		
		testBasicFlow_Cancellation(reconcileRegistration, doc);
		
		testDisconnectUpdates(key, reconcileRegistration, initialConnect ? false : true);
		
		if(initialConnect) {
			documentSet(doc);
			checkCounts();
		}
	}
	
	protected SourceModelRegistration testConnectStructureUpdates(Object key, Document doc, boolean initialConnect) {
		if(initialConnect) {
			StructureInfo storedStructureInfo = engineClient.getStoredStructureInfo(key);
			assertTrue(storedStructureInfo == null || !storedStructureInfo.hasConnectedListeners());
		}
		
		SourceModelRegistration registration = engineClient.connectStructureUpdates3(key, doc, structureListener);
		checkTaskDelta(initialConnect ? 1 : 0);
		
		StructureInfo structureInfo = registration.structureInfo;
		assertTrue(structureInfo.hasConnectedListeners());
		assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
		
		return registration;
	}
	
	protected void testDisconnectUpdates(Object key, SourceModelRegistration registration, boolean hasOtherConnections) 
			throws InterruptedException {
		StructureInfo structureInfo = registration.structureInfo;
		
		if(hasOtherConnections) {
			assertTrue(structureInfo.updateListeners.getListeners().size() > 1);
		} else {
			assertTrue(structureInfo.updateListeners.getListeners().size() == 1);
		}
		
		TestsStructureModelListener listener = 
				new TestsStructureModelListener(engineClient, structureInfo, hasOtherConnections ? 0 : 1);
		assertTrue(structureInfo.isStale() == false);
		try {
			registration.dispose();
			
			checkTaskDelta(0);
			assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
			assertTrue(structureInfo.hasConnectedListeners() == hasOtherConnections);
			
			if(hasOtherConnections == false) {
				listener.latch.countDown();
			}
			
			awaitUnchecked(listener.latch);
		} finally {
			owned.remove(listener);
			engineClient.removeListener(listener);
		}
		
		verifyThrows(() -> registration.dispose(), AssertFailedException.class);
	}
	
	protected class TestsStructureModelListener extends StrictDisposable implements IStructureModelListener {
		
		protected final CountDownLatch latch;
		protected final StructureInfo structureInfo;
		protected volatile int expectedChanges;
		
		public TestsStructureModelListener(EngineClient engineClient, StructureInfo structureInfo, int expectedChanges) 
				throws InterruptedException {
			this.structureInfo = structureInfo;
			this.expectedChanges = expectedChanges;
			assertTrue(expectedChanges >= 0);
			this.latch = new CountDownLatch(expectedChanges);
			
			structureInfo.awaitUpdatedData();
			engineClient.addListener(this);
			owned.add(this);
		}
		
		@Override
		public void structureChanged(StructureInfo lockedStructureInfo) {
			if(lockedStructureInfo == structureInfo) {
				assertTrue(expectedChanges > 0);
				expectedChanges--;
				latch.countDown();
			}
		}
		
		@Override
		protected void disposeDo() {
			assertFail();
		}
		
	}
	
	protected void testBasicFlow_Cancellation(SourceModelRegistration reconcileRegistration, Document doc) 
			throws InterruptedException {
		if(engineClient2 == null)
			return;
		
		CountDownLatch interruptionTerminationLatch = new CountDownLatch(1);
		
		Function<StructureInfo, StructureUpdateTask> _saved = createUpdateTask;
		try {
			CountDownLatch entryLatch = new CountDownLatch(1);
			
			createUpdateTask = (structureInfo) -> {
				return new StructureUpdateTask(structureInfo) {
					@Override
					protected SourceFileStructure createNewData() {
						entryLatch.countDown();
						
						try {
							new CountDownLatch(1).await(); // Infinite wait unless interrupted
						} catch(InterruptedException e) {
							interruptionTerminationLatch.countDown();
							return null;
						}
						throw assertFail();
					}
				};
			};
			
			doc.set("1");
			checkTaskDelta(1);
			awaitUnchecked(entryLatch);
			assertTrue(reconcileRegistration.structureInfo.isStale() == true);
			
		} finally {
			createUpdateTask = _saved;
		}
		
		// Task should be waiting on engineClient2.taskExitLatch now
		doc.set("2");
		// previous task will be cancelled, and interrupted
		checkTaskDelta(1);
		
		interruptionTerminationLatch.await();
	}
	
	/* -----------------  ----------------- */
	
	protected void testMultipleConnects(Object key, Document doc) throws InterruptedException {
		initializeTestsEngineClient();
		
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

		checkTaskDelta(1);
		
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
	
	/* -----------------  ----------------- */
	
//	public static class _WithActualTest extends StructureModelManager_WithActualTest { }
	
}