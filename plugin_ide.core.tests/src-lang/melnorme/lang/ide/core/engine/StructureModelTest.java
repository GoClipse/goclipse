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

import melnorme.lang.ide.core.engine.SourceModelManager.StructureModelRegistration;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureInfo;
import melnorme.lang.ide.core.engine.SourceModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.tooling.LocationKey;
import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.Assert.AssertFailedException;
import melnorme.utilbox.ownership.StrictDisposable;
import melnorme.utilbox.tests.TestsWorkingDir;

public class StructureModelTest extends CommonCoreTest {
	
	static {
		assertTrue(StructureModelManager_WithActual_Test.class != null); // Ensure class exists
	}
	
	/* -----------------  ----------------- */
	
	protected SourceModelManager mgr;
	protected FixtureSourceModelManager fixtureMgr;
	
	public int createUpdateTaskCount_EXPECTED;
	
	protected void initializeTestsEngineClient() {
		fixtureMgr = new FixtureSourceModelManager();
		mgr = fixtureMgr; 
		createUpdateTaskCount_EXPECTED = 0;
	}
	
	public static class FixtureSourceModelManager extends SourceModelManager {
		
		public FixtureSourceModelManager() {
			super(new DocumentReconcileManager(), new ProblemMarkerUpdater());
		}
		
		public final SourceFileStructure DEFAULT_STRUCTURE = new SourceFileStructure(null, null, 
			(Indexable<ParserError>) null);
		
		public final Function<StructureInfo, StructureUpdateTask> DEFAULT_UPDATE_TASK = (structureInfo) -> {
			return new StructureUpdateTask(structureInfo) {
				@Override
				protected SourceFileStructure doCreateNewData() {
					return DEFAULT_STRUCTURE;
				}
			};
		};
		protected Function<StructureInfo, StructureUpdateTask> updateTaskProvider = DEFAULT_UPDATE_TASK;
		
		public int createUpdateTaskCount = 0;
		
		@Override
		protected StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source) {
			createUpdateTaskCount++;
			
			return updateTaskProvider.apply(structureInfo);
		}
		
	}
	
	public void checkTaskDelta(int updateTaskCount) {
		createUpdateTaskCount_EXPECTED += updateTaskCount;
		checkCounts();
	}
	
	public void checkCounts() {
		assertTrue(fixtureMgr.createUpdateTaskCount == createUpdateTaskCount_EXPECTED);
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
		
		assertTrue(mgr.getStoredStructureInfo(new LocationKey("Blah", "Blah")) == null);
		
		LocationKey key = new LocationKey("Key1", "Key1");
		
		testBasicFlow(key, new Document(), true);
		
		// Test again, new document
		testBasicFlow(key, new Document(), true);
		
		
		// Test with actual location
		LocationKey locationKey = new LocationKey(TestsWorkingDir.getWorkingDir("StructureModelTest/blah"));
		testBasicFlow(locationKey, new Document(), true);
		
		/* -----------------  ----------------- */
		
		testMultipleConnects(key, new Document());
	}
	
	protected void testBasicFlow(LocationKey key, Document doc, boolean initialConnect) throws InterruptedException {
		checkCounts();
		if(initialConnect) {
			documentSet(doc);
			checkCounts();
		}
		
		StructureModelRegistration modelRegistration = testConnectUpdates(key, doc, initialConnect);
		
		documentSet(doc);
		checkTaskDelta(1);
		
		documentSet(doc);
		checkTaskDelta(1);
		
		testBasicFlow_Cancellation(modelRegistration, doc);
		
		testDisconnectUpdates(key, modelRegistration, initialConnect ? false : true);
		
		if(initialConnect) {
			documentSet(doc);
			checkCounts();
		}
	}
	
	protected StructureModelRegistration testConnectUpdates(LocationKey key, Document doc, boolean initialConnect) {
		if(initialConnect) {
			StructureInfo storedStructureInfo = mgr.getStoredStructureInfo(key);
			assertTrue(storedStructureInfo == null || !storedStructureInfo.hasConnectedListeners());
		}
		
		StructureModelRegistration registration = mgr.connectStructureUpdates(key, doc, IStructureModelListener.NIL_LISTENER);
		checkTaskDelta(initialConnect ? 1 : 0);
		
		StructureInfo structureInfo = registration.structureInfo;
		assertTrue(structureInfo.hasConnectedListeners());
		assertTrue(mgr.getStoredStructureInfo(key) == structureInfo);
		
		return registration;
	}
	
	protected void testDisconnectUpdates(LocationKey key, StructureModelRegistration regist, boolean hasOtherConnections) 
			throws InterruptedException {
		StructureInfo structureInfo = regist.structureInfo;
		
		TestsStructureModelListener listener = 
				new TestsStructureModelListener(mgr, structureInfo, hasOtherConnections ? 0 : 1);
		assertTrue(structureInfo.isStale() == false);
		try {
			regist.dispose();
			
			checkTaskDelta(0);
			assertTrue(mgr.getStoredStructureInfo(key) == structureInfo);
			assertTrue(structureInfo.hasConnectedListeners() == hasOtherConnections);
			
			if(hasOtherConnections == false) {
				listener.latch.countDown();
			}
			
			awaitUnchecked(listener.latch);
		} finally {
			owned.remove(listener);
			mgr.removeListener(listener);
		}
		
		verifyThrows(() -> regist.dispose(), AssertFailedException.class);
	}
	
	protected class TestsStructureModelListener extends StrictDisposable implements IStructureModelListener {
		
		protected final CountDownLatch latch;
		protected final StructureInfo structureInfo;
		protected volatile int expectedChanges;
		
		public TestsStructureModelListener(SourceModelManager manager, StructureInfo structureInfo, int expectedChanges) 
				throws InterruptedException {
			this.structureInfo = structureInfo;
			this.expectedChanges = expectedChanges;
			assertTrue(expectedChanges >= 0);
			this.latch = new CountDownLatch(expectedChanges);
			
			structureInfo.awaitUpdatedData();
			manager.addListener(this);
			owned.add(this);
		}
		
		@Override
		public void dataChanged(StructureInfo lockedStructureInfo) {
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
	
	protected void testBasicFlow_Cancellation(StructureModelRegistration registration, Document doc) 
			throws InterruptedException {
		if(fixtureMgr == null)
			return;
		
		CountDownLatch interruptionTerminationLatch = new CountDownLatch(1);
		
		Function<StructureInfo, StructureUpdateTask> _saved = fixtureMgr.updateTaskProvider;
		try {
			CountDownLatch entryLatch = new CountDownLatch(1);
			
			fixtureMgr.updateTaskProvider = (structureInfo) -> {
				return new StructureUpdateTask(structureInfo) {
					@Override
					protected SourceFileStructure doCreateNewData() throws OperationCancellation {
						entryLatch.countDown();
						
						try {
							new CountDownLatch(1).await(); // Infinite wait unless interrupted
						} catch(InterruptedException e) {
							interruptionTerminationLatch.countDown();
							throw new OperationCancellation();
						}
						throw assertFail();
					}
				};
			};
			
			doc.set("1");
			checkTaskDelta(1);
			awaitUnchecked(entryLatch);
			assertTrue(registration.structureInfo.isStale() == true);
			
		} finally {
			fixtureMgr.updateTaskProvider = _saved;
		}
		
		// Task should be waiting on engineClient2.taskExitLatch now
		doc.set("2");
		// previous task will be cancelled, and interrupted
		checkTaskDelta(1);
		
		interruptionTerminationLatch.await();
	}
	
	/* -----------------  ----------------- */
	
	protected void testMultipleConnects(LocationKey key, Document doc) throws InterruptedException {
		initializeTestsEngineClient();
		
		StructureModelRegistration registration = testConnectUpdates(key, doc, true);
		StructureInfo structureInfo = registration.structureInfo;
		
		checkCounts();
		StructureModelRegistration registration2 = mgr.connectStructureUpdates(key, doc, IStructureModelListener.NIL_LISTENER);
		assertTrue(registration2.structureInfo == structureInfo);
		// Test no extra updates
		checkCounts();
		
		testBasicFlow(key, doc, false);
		
		StructureModelRegistration registration_unmanaged = 
				mgr.connectStructureUpdates(key, new Document(), IStructureModelListener.NIL_LISTENER);
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