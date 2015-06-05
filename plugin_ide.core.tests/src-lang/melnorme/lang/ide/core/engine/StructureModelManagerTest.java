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
import melnorme.lang.ide.core.engine.StructureModelManager.MDocumentSynchedAcess;
import melnorme.lang.ide.core.engine.StructureModelManager.StructureInfo;
import melnorme.lang.ide.core.tests.CommonCoreTest;
import melnorme.lang.tooling.structure.SourceFileStructure;
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
		public void structureChanged(StructureInfo lockedStructureInfo, SourceFileStructure sourceFileStructure) {
		}
	};
	
	protected EngineClient createEngineClient() {
		return new InstrumentedEngineClient();
	}
	
	public class InstrumentedEngineClient extends EngineClient {
		
		public CountDownLatch updateLatch = new CountDownLatch(0);
		public CountDownLatch disposeLatch = new CountDownLatch(0);
		
		@Override
		protected StructureUpdateTask createUpdateTask2(StructureInfo structureInfo, String source,
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
		protected StructureUpdateTask createDisposeTask2(StructureInfo structureInfo, Location fileLocation) {
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
	
	public void checkCounts(int updateTaskCount, int disposeTaskCount) {
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
		engineClient = createEngineClient();
		
		assertTrue(engineClient.getStoredStructureInfo("Blah") == null);
		
		Object key = "Key1";
		
		testBasicFlow(key, new Document(), true);
		
		// Test again, new document
		testBasicFlow(key, new Document(), true);
		
		
		/* -----------------  ----------------- */
		
		testMultipleConnects(key, new Document());
	}
	
	protected StructureInfo testConnectStructureUpdates(Object key, Document doc, boolean initialConnect) {
		if(initialConnect) {
			StructureInfo storedStructureInfo = engineClient.getStoredStructureInfo(key);
			assertTrue(storedStructureInfo == null || !storedStructureInfo.isWorkingCopy());
		}
		
		StructureInfo structureInfo = engineClient.connectStructureUpdates(key, doc, structureListener);
		checkCounts(initialConnect ? 1 : 0, 0);
		
		assertTrue(structureInfo.isWorkingCopy());
		assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
		
		return structureInfo;
	}
	
	protected void testDisconnectStructureUpdates(Object key, StructureInfo structureInfo, boolean isWorkingCopy) {
		engineClient.disconnectStructureUpdates2(structureInfo, structureListener, new MDocumentSynchedAcess() {});
		checkCounts(0, 1);
		
		assertTrue(engineClient.getStoredStructureInfo(key) == structureInfo);
		assertTrue(structureInfo.isWorkingCopy() == isWorkingCopy);
	}
	
	protected void testBasicFlow(Object key, Document doc, boolean initialConnect) {
		checkCounts();
		if(initialConnect) {
			doc.set("");
			checkCounts();
		}
		
		StructureInfo structureInfo;
		structureInfo = testConnectStructureUpdates(key, doc, initialConnect);
		
		doc.set("");
		checkCounts(1, 0);
		
		doc.set("");
		checkCounts(1, 0);
		
		testDisconnectStructureUpdates(key, structureInfo, initialConnect ? false : true);
		
		if(initialConnect) {
			doc.set("");
			checkCounts();
		}
	}
	
	protected void testMultipleConnects(Object key, Document doc) {
		engineClient = createEngineClient();
		
		StructureInfo structureInfo;
		structureInfo = testConnectStructureUpdates(key, doc, true);
		
		checkCounts();
		assertTrue(engineClient.connectStructureUpdates(key, doc, structureListener) == structureInfo);
		// Test no extra updates
		checkCounts();
		
		testBasicFlow(key, doc, false);
		
		StructureInfo structureInfo2 = engineClient.connectStructureUpdates(key, new Document(), structureListener);
		assertTrue(structureInfo != structureInfo2);
		checkCounts(1, 0);
		
		testBasicFlow(key, doc, false);
		
		testDisconnectStructureUpdates(key, structureInfo, true);
		testDisconnectStructureUpdates(key, structureInfo, false);
		
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
		protected EngineClient createEngineClient() {
			return LangCore_Actual.createEngineClient();
		}
		
		@Override
		public void checkCounts() {
			// Do nothing.
		}
	}
	
}