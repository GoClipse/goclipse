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

import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.utils.EntryMapTS;
import melnorme.lang.utils.concurrency.ConcurrentlyDerivedData;
import melnorme.lang.utils.concurrency.ConcurrentlyDerivedData.DataUpdateTask;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.fields.ListenerListHelper;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.ownership.StrictDisposable;

/**
 * The purpose of Engine manager is two-fold:
 * 
 * - Keep track of document text changes and send those updates to the engine.
 * - Retrieve parsing/structural information from the client after such text updates.
 *
 */
/* FIXME: rename */
public abstract class StructureModelManager extends AbstractModelUpdateManager<Object> {
	
	public StructureModelManager() {
	}
	
	/* -----------------  ----------------- */
	
	protected final EntryMapTS<Object, StructureInfo> infosMap = 
			new EntryMapTS<Object, StructureInfo>() {
		@Override
		protected StructureInfo createEntry(Object key) {
			return new StructureInfo(key);
		}
	};
	
	/**  @return the {@link SourceFileStructure} currently stored under given key. Can be null. */
	public StructureInfo getStoredStructureInfo(Object key) {
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
	public SourceModelRegistration connectStructureUpdates3(Object key, IDocument document, 
			IStructureModelListener structureListener) {
		assertNotNull(key);
		assertNotNull(document);
		assertNotNull(structureListener);
		log.println("connectStructureUpdates: " + key);
		
		StructureInfo sourceInfo = infosMap.getEntry(key);
		
		boolean connected = sourceInfo.connectDocument(document, structureListener);
		
		if(!connected) {
			// Odd case: we tried to connect with equivalent key, but the document is other.
			// return a unmanaged StructureInfo
			sourceInfo = new StructureInfo(key);
			connected = sourceInfo.connectDocument(document, structureListener);
		}
		assertTrue(connected);
		
		return new SourceModelRegistration(sourceInfo, structureListener);
	}
	
	public class SourceModelRegistration extends StrictDisposable {
		
		public final StructureInfo structureInfo;
		protected final IStructureModelListener structureListener;
		
		public SourceModelRegistration(StructureInfo structureInfo, IStructureModelListener structureListener) {
			this.structureInfo = assertNotNull(structureInfo);
			this.structureListener = assertNotNull(structureListener);
		}
		
		@Override
		protected void disposeDo() {
			disconnectStructureUpdates();
		}
		
		private void disconnectStructureUpdates() {
			log.println("disconnectStructureUpdates: " + structureInfo.getKey());
			structureInfo.disconnectFromDocument(structureListener);
		}
		
	}
	
	/* -----------------  ----------------- */
	
	public class StructureInfo extends ConcurrentlyDerivedData<SourceFileStructure> {
		
		protected final Object key;
		protected final ListenerListHelper<IStructureModelListener> updateListeners = new ListenerListHelper<>();
		protected final StructureUpdateTask disconnectTask; // Can be null
		
		protected IDocument document = null;
		
		public StructureInfo(Object key) {
			this.key = assertNotNull(key);
			
			this.disconnectTask = assertNotNull(createDisconnectTask(this));
		}
		
		public final Object getKey() {
			return key;
		}
		
		public Location getLocation() {
			if(key instanceof Location) {
				return (Location) key;
			}
			return null;
		}
		
		public synchronized boolean hasConnectedListeners() {
			return updateListeners.getListeners().size() > 0;
		}
		
		protected synchronized boolean connectDocument(IDocument newDocument, IStructureModelListener structureListener) {
			if(document == null) {
				document = newDocument;
				newDocument.addDocumentListener(docListener);
				queueNewUpdateTask();
			}
			else if(document != newDocument) {
				return false;
			}
			
			updateListeners.addListener(structureListener);
			return true;
		}
		
		protected final IDocumentListener docListener = new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
			@Override
			public void documentChanged(DocumentEvent event) {
				queueNewUpdateTask();
			}
		};
		
		protected synchronized void disconnectFromDocument(IStructureModelListener structureListener) {
			updateListeners.removeListener(structureListener);
			
			if(!hasConnectedListeners()) {
				document.removeDocumentListener(docListener);
				document = null;
				
				if(disconnectTask != null) {
					/* FIXME: disconnect task*/
					queueUpdateTask(disconnectTask);
				}
				
//				infosMap.removeEntry(key);
			}
		}
		
		protected void queueNewUpdateTask() {
			// Note: document should only be acessed in the same thread that fires document listeners,
			// so retrieve the document contents now.
			final String source = document.get();
			Location location = getLocation();
			StructureUpdateTask updateTask = createUpdateTask(this, source, location);
			queueUpdateTask(updateTask);
		}
		
		protected synchronized void queueUpdateTask(StructureUpdateTask updateTask) {
			setUpdateTask(updateTask);
			
			executor.submit(updateTask);
		}
		
		@Override
		protected void doHandleDataChanged() {
			notifyStructureChanged(this, updateListeners);
			notifyStructureChanged(this, modelListeners);
		}
		
		public SourceFileStructure awaitUpdatedData(IProgressMonitor pm) throws OperationCancellation {
			while(true) {
				if(pm.isCanceled()) {
					throw new OperationCancellation();
				}
				
				try {
					return awaitUpdatedData(100, TimeUnit.MILLISECONDS);
				} catch(InterruptedException e) {
					continue;
				}
			}
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/**
	 * Create an update task for the given structureInfo, due to a document change.
	 * @param source the new source of the document being listened to.
	 * @param fileLocation the file location if document is based on a file, null otherwise.
	 */
	protected abstract StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source,
			Location fileLocation);
	
	/**
	 * Create an update task for when the last listener of given structureInfo disconnects.
	 */
	protected DisconnectUpdatesTask createDisconnectTask(StructureInfo structureInfo) {
		return new DisconnectUpdatesTask(structureInfo);
	}
	
	public static abstract class StructureUpdateTask extends DataUpdateTask<SourceFileStructure> {
		
		protected final StructureInfo structureInfo;
		
		public StructureUpdateTask(StructureInfo structureInfo) {
			super(structureInfo, structureInfo.getKey().toString());
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
		protected SourceFileStructure createNewData() {
			Location location = structureInfo.getLocation();
			if(location != null) {
				handleDisconnectForLocation(location);
			} else {
				handleDisconnectForNoLocation();
			}
			
			return null;
		}
		
		@SuppressWarnings("unused")
		protected void handleDisconnectForLocation(Location location) {
		}
		
		//@SuppressWarnings("unused")
		protected void handleDisconnectForNoLocation() {
		}
		
	}
	
	
	/* ----------------- Listeners ----------------- */
	
	protected final ListenerListHelper<IStructureModelListener> modelListeners = new ListenerListHelper<>();
	
	public void addListener(IStructureModelListener listener) {
		assertNotNull(listener);
		modelListeners.addListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		modelListeners.removeListener(listener);
	}
	
	protected static void notifyStructureChanged(final StructureInfo structureInfo, 
			ListenerListHelper<IStructureModelListener> listeners) {
		for(IStructureModelListener listener : listeners.getListeners()) {
			try {
				listener.structureChanged(structureInfo);
			} catch (Exception e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
	/* -----------------  ----------------- */
	
	public void awaitUpdatedWorkingCopy(Object modelKey, IProgressMonitor pm) throws OperationCancellation {
		// TODO: this could be update to await until server responds (meaning that its working copy is updated), 
		// no need to actually wait for structure to be parsed. 
		StructureInfo structureInfo = getStoredStructureInfo(modelKey);
		if(structureInfo == null) {
			return; // No updates pending
		}
		structureInfo.awaitUpdatedData(pm);
	}
	
	/* ----------------- util ----------------- */
	
}