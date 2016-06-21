/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.jface.text.IDocument;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.engine.DocumentReconcileManager.DocumentReconcileConnection;
import melnorme.lang.tooling.LocationKey;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.utils.concurrency.ConcurrentlyDerivedResult;
import melnorme.lang.utils.concurrency.SynchronizedEntryMap;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.CResult;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.fields.ListenerListHelper;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.IDisposable;
import melnorme.utilbox.ownership.StrictDisposable;

/**
 * The SourceModelManager keeps track of text document changes, and updates derived models, such as:
 * 
 * - Source module parse-analysis and structure-info (possibly with problem markers update).
 * - Possible persist document changes to files in filesystem, or update a semantic engine buffers.
 *
 */
public abstract class SourceModelManager extends AbstractAgentManager {
	
	protected final DocumentReconcileManager reconcileMgr;
	
	public SourceModelManager() {
		this(new DocumentReconcileManager(), new ProblemMarkerUpdater());
	}
	
	public SourceModelManager(DocumentReconcileManager reconcileMgr, ProblemMarkerUpdater problemUpdater) {
		this.reconcileMgr = assertNotNull(reconcileMgr);
		asOwner().bind(reconcileMgr);
		
		if(problemUpdater != null) {
			problemUpdater.install(this);
		}
	}
	
	/* -----------------  ----------------- */
	
	protected final SynchronizedEntryMap<LocationKey, StructureInfo> infosMap = 
			new SynchronizedEntryMap<LocationKey, StructureInfo>() {
		@Override
		protected StructureInfo createEntry(LocationKey key) {
			return new StructureInfo(key);
		}
	};
	
	/**  @return the {@link SourceFileStructure} currently stored under given key. Can be null. */
	public StructureInfo getStoredStructureInfo(LocationKey key) {
		return infosMap.getEntryOrNull(key);
	}
	
	/**
	 * Connect given structure listener to structure under given key, 
	 * and begin structure updates using given document as input.
	 * 
	 * If a previous listener was already connected, but with a different document,
	 * an unmanaged {@link StructureInfo} will be returned
	 * 
	 * @return non-null. The {@link StructureInfo} resulting from given connection.
	 */
	public StructureModelRegistration connectStructureUpdates(LocationKey key, IDocument document, 
			IStructureModelListener structureListener) {
		assertNotNull(key);
		assertNotNull(document);
		assertNotNull(structureListener);
		log.println("connectStructureUpdates: " + key);
		
		StructureInfo structureInfo = infosMap.getEntry(key);
		
		boolean connected = structureInfo.connectDocument(document, structureListener);
		
		if(!connected) {
			// Special case: this key has already been connected to, but with a different document.
			// As such, return a unmanaged StructureInfo
			structureInfo = new StructureInfo(key);
			connected = structureInfo.connectDocument(document, structureListener);
		}
		assertTrue(connected);
		assertTrue(areEqual(structureInfo.getLocation(), key.getLocation()));
		
		return new StructureModelRegistration(structureInfo, structureListener);
	}
	
	public class StructureModelRegistration extends StrictDisposable {
		
		public final StructureInfo structureInfo;
		protected final IStructureModelListener structureListener;
		
		public StructureModelRegistration(StructureInfo structureInfo, IStructureModelListener structureListener) {
			this.structureInfo = assertNotNull(structureInfo);
			this.structureListener = assertNotNull(structureListener);
		}
		
		@Override
		protected void disposeDo() {
			log.println("disconnectStructureUpdates: " + structureInfo.getKey2());
			structureInfo.disconnectFromDocument(structureListener);
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public class StructureInfo extends ConcurrentlyDerivedResult<SourceFileStructure, CommonException, StructureInfo> {
		
		protected final LocationKey key2;
		protected final StructureUpdateTask disconnectTask; // Can be null
		
		protected IDocument document = null;
		protected DocumentReconcileConnection reconcileConnection = null;
		
		
		public StructureInfo(LocationKey key) {
			this.key2 = assertNotNull(key);
			
			this.disconnectTask = assertNotNull(createDisconnectTask(this));
		}
		
		public final LocationKey getKey2() {
			return key2;
		}
		
		/** @return the file location if source is based on a file, null otherwise. */
		public Location getLocation() {
			return key2.getLocation();
		}
		
		@Override
		protected Result<SourceFileStructure, CommonException> createResult(SourceFileStructure resultValue,
				CommonException resultException) {
			// CResult will provide better runtime assertion checks for the exception contract, 
			// better than just using Result
			return new CResult<>(resultValue, resultException);
		}
		
		public synchronized boolean hasConnectedListeners() {
			return connectedListeners.getListeners().size() > 0;
		}
		
		protected synchronized boolean connectDocument(IDocument newDocument, IStructureModelListener listener) {
			if(document == null) {
				document = newDocument;
				queueSourceUpdateTask(document.get());
				
				reconcileConnection = reconcileMgr.connectDocument(document, this);
			}
			else if(document != newDocument) {
				return false;
			}
			
			connectedListeners.addListener(listener);
			return true;
		}
		
		protected synchronized void disconnectFromDocument(IStructureModelListener structureListener) {
			connectedListeners.removeListener(structureListener);
			
			if(!hasConnectedListeners()) {
				reconcileConnection.disconnect();
				reconcileConnection = null;
				
				document = null;
				
				queueUpdateTask(disconnectTask);
			}
		}
		
		protected StructureUpdateTask queueSourceUpdateTask(final String source) {
			StructureUpdateTask updateTask = createUpdateTask(this, source);
			queueUpdateTask(updateTask);
			return updateTask;
		}
		
		public synchronized StructureUpdateTask documentSaved(IDocument document) {
			// need to recheck, the underlying document might have changed
			if(document != this.document) {
				return null;
			}
			StructureUpdateTask updateTask = createUpdateTask_forFileSave(this, document.get());
			if(updateTask != null) {
				queueUpdateTask(updateTask);
			}
			return updateTask;
		}
		
		protected synchronized void queueUpdateTask(StructureUpdateTask updateTask) {
			setUpdateTask(updateTask);
			
			executor.submitR(updateTask);
		}
		
		@Override
		protected void doHandleDataChanged() {
			super.doHandleDataChanged();
			notifyStructureChanged(this, globalListeners);
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Create an update task for the given structureInfo, due to a document change.
	 * @param source the new source of the document being listened to.
	 */
	protected abstract StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source);
	
	/**
	 * Similar to {@link #createUpdateTask(StructureInfo, String)} but only for when the file buffer 
	 * is saved to disk. Default is to return null, which ignores this event.
	 */
	@SuppressWarnings("unused")
	protected StructureUpdateTask createUpdateTask_forFileSave(StructureInfo structureInfo, String source) {
		return null;
	}
	
	/**
	 * Create an update task for when the last listener of given structureInfo disconnects.
	 */
	protected DisconnectUpdatesTask createDisconnectTask(StructureInfo structureInfo) {
		return new DisconnectUpdatesTask(structureInfo);
	}
	
	public static abstract class StructureUpdateTask extends StructureInfo.ResultUpdateTask {
		
		protected final StructureInfo structureInfo;
		
		public StructureUpdateTask(StructureInfo structureInfo) {
			structureInfo.super(structureInfo.getKey2().toString());
			this.structureInfo = structureInfo;
		}
		
		@Override
		protected void handleRuntimeException(RuntimeException e) {
			LangCore.logInternalError(e);
		}
		
	}
	
	public static class DisconnectUpdatesTask extends StructureUpdateTask {
		
		public DisconnectUpdatesTask(StructureInfo structureInfo) {
			super(structureInfo);
		}
		
		@Override
		protected SourceFileStructure doCreateNewData() throws CommonException, OperationCancellation {
			Location location = structureInfo.getLocation();
			if(location != null) {
				handleDisconnectForLocation(location);
			} else {
				handleDisconnectForNoLocation();
			}
			
			throw new OperationCancellation();
		}
		
		@SuppressWarnings("unused")
		protected void handleDisconnectForLocation(Location location) {
		}
		
		//@SuppressWarnings("unused")
		protected void handleDisconnectForNoLocation() {
		}
		
	}
	
	
	/* ----------------- Listeners ----------------- */
	
	protected final ListenerListHelper<IStructureModelListener> globalListeners = new ListenerListHelper<>();
	
	public IDisposable addListener(IStructureModelListener listener) {
		assertNotNull(listener);
		globalListeners.addListener(listener);
		return () -> removeListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		globalListeners.removeListener(listener);
	}
	
}