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
import melnorme.lang.utils.concurrency.ConcurrentDerivedData;
import melnorme.lang.utils.concurrency.UpdateTask;
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
		
		StructureInfo structureInfo = infosMap.getEntry(key);
		
		boolean connected = structureInfo.connectDocument(document, structureListener);
		
		if(!connected) {
			// Odd case: we tried to connect with equivalent key, but the document is other.
			// return a unmanaged StructureInfo
			structureInfo = new StructureInfo(key);
			connected = structureInfo.connectDocument(document, structureListener);
		}
		assertTrue(connected);
		
		return new SourceModelRegistration(structureInfo, structureListener);
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
			disconnectStructureUpdates(structureInfo, structureListener);
		}
		
	}
	
	protected void queueUpdateTask(StructureInfo structureInfo, IDocument document) {
		Location location = getLocation(structureInfo.getKey());
		// Note: document should only be acessed in the same thread that fires document listeners
		// So retrieve the source now.
		final String source = document.get(); 
		StructureUpdateTask updateTask = createUpdateTask(structureInfo, source, location);
		structureInfo.queueTask(updateTask);
	}
	
	/**
	 * Create an update task for the given structureInfo.
	 * @param source 
	 * @param fileLocation the file location if document is based on a file, null otherwise.
	 */
	protected abstract StructureUpdateTask createUpdateTask(StructureInfo structureInfo, String source,
			Location fileLocation);
	
	
	public void disconnectStructureUpdates(StructureInfo structureInfo, IStructureModelListener structureListener) {
		assertNotNull(structureInfo);
		assertNotNull(structureListener);
		log.println("disconnectStructureUpdates: " + structureInfo.getKey());
		
		// TODO: ideally this should remove info object from infosMap, otherwise it's a minor memory leak.
		// However, since this is only used by open editors, it's acceptable for now.
		
		Location fileLoc = getLocation(structureInfo.getKey());
		structureInfo.disconnectFromDocument(structureListener, createDisposeTask(structureInfo, fileLoc));
	}
	
	/**
	 * Create a dispose task for the given structureInfo.
	 * @param fileLocation the file location if document is based on a file, null otherwise.
	 */
	protected abstract StructureUpdateTask createDisposeTask(StructureInfo structureInfo, Location fileLocation);
	
	
	public class StructureInfo extends ConcurrentDerivedData<SourceFileStructure> {
		
		protected final Object key;
		
		private final ListenerListHelper<IStructureModelListener> updateListeners = new ListenerListHelper<>();
		
		private IDocument document = null;
		
		public StructureInfo(Object key) {
			this.key = assertNotNull(key);
		}
		
		public final Object getKey() {
			return key;
		}
		
		public synchronized boolean hasConnectedListeners() {
			return updateListeners.getListeners().size() > 0;
		}
		
		
		public synchronized boolean connectDocument(IDocument newDocument, IStructureModelListener structureListener) {
			if(document == null) {
				document = newDocument;
				newDocument.addDocumentListener(docListener);
				queueUpdateTask(this, newDocument);
			}
			else if(document != newDocument) {
				return false;
			}
			
			updateListeners.addListener(structureListener);
			return true;
		}
		
		private final IDocumentListener docListener = new IDocumentListener() {
			@Override
			public void documentAboutToBeChanged(DocumentEvent event) {
			}
			@Override
			public void documentChanged(DocumentEvent event) {
				queueUpdateTask(StructureInfo.this, document);
			}
		};
		
		public synchronized void disconnectFromDocument(IStructureModelListener structureListener,
				StructureUpdateTask disposeTask) {
			updateListeners.removeListener(structureListener);
			if(!hasConnectedListeners()) {
				document.removeDocumentListener(docListener);
				document = null;
			}
			
			if(disposeTask != null) {
				queueTask(disposeTask);
			}
		}
		
		public synchronized void queueTask(UpdateTask updateTask) {
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
	
	/** Marker interface to represent that one has access to {@link StructureInfo#document} . */
	public static enum MDocumentSynchedAcess { FLAG }
	
	/* -----------------  ----------------- */
	
	public static abstract class StructureUpdateTask extends UpdateTask {
		
		protected final StructureInfo structureInfo;
		protected final String customName;
		
		public StructureUpdateTask(StructureInfo structureInfo) {
			this.structureInfo = structureInfo;
			this.customName = structureInfo.key.toString();
		}
		
		@Override
		public void doRun() {
			Thread currentThread = Thread.currentThread();
			String originalName = currentThread.getName();
			try {
				currentThread.setName(originalName + " " + customName);
				
				createFileStructure();
			} catch(RuntimeException e) {
				LangCore.logInternalError(e);
				structureInfo.setNewStructure(null, this);
			} finally {
				currentThread.setName(originalName);
			}
		}
		
		protected void createFileStructure() {
			SourceFileStructure newStructure = createSourceFileStructure();
			
			structureInfo.setNewStructure(newStructure, this);
		}
		
		protected abstract SourceFileStructure createSourceFileStructure();
		
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
	
	protected Location getLocation(Object key) {
		if(key instanceof Location) {
			return (Location) key;
		}
		return null;
	}
	
}