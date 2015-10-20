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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.utils.EntryMapTS;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.NamingThreadFactory;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.fields.ListenerListHelper;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.misc.SimpleLogger;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

/**
 * The purpose of Engine manager is two-fold:
 * 
 * - Keep track of document text changes and send those updates to the engine.
 * - Retrieve parsing/structural information from the client after such text updates.
 *
 */
public abstract class StructureModelManager {
	
	public static SimpleLogger log = new SimpleLogger(Platform.inDebugMode());
	
	protected final ExecutorService executor = Executors.newCachedThreadPool(
		new NamingThreadFactory(getClass().getSimpleName())); 
	
	public StructureModelManager() {
	}
	
	public void dispose() {
		executor.shutdown();
	}
	
	/* -----------------  ----------------- */
	
	// TODO: ideally this should be a cache, otherwise it's a potential memory leak if structures are not removed.
	protected final EntryMapTS<Object, StructureInfo> structureInfos = 
			new EntryMapTS<Object, StructureInfo>() {
		@Override
		protected StructureInfo createEntry(Object key) {
			return new StructureInfo(key);
		}
	};
	
	/**  @return the {@link SourceFileStructure} currently stored under given key. Can be null. */
	public StructureInfo getStoredStructureInfo(Object key) {
		return structureInfos.getEntryOrNull(key);
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
	public StructureInfo connectStructureUpdates(Object key, IDocument document, 
			IStructureModelListener structureListener) {
		assertNotNull(key);
		assertNotNull(document);
		assertNotNull(structureListener);
		log.println("connectStructureUpdates: " + key);
		
		StructureInfo structureInfo = structureInfos.getEntry(key);
		
		boolean connected = structureInfo.connectDocument(document, structureListener);
		
		if(!connected) {
			// Odd case: we tried to connect with equivalent key, but the document is other.
			// return a non-primary structure info
			structureInfo = new StructureInfo(key);
			connected = structureInfo.connectDocument(document, structureListener);
		}
		assertTrue(connected);
		
		return structureInfo;
	}
	
	protected void queueUpdateTask(StructureInfo structureInfo, IDocument document) {
		Location location = getLocation(structureInfo.getKey());
		// Note: document should only be acessed in the same thread that fires document listeners
		// So retrieve the source now.
		final String source = document.get(); 
		structureInfo.queueTask(createUpdateTask2(structureInfo, source, location));
	}
	
	/**
	 * Create an update task for the given structureInfo.
	 * @param source 
	 * @param fileLocation the file location if document is based on a file, null otherwise.
	 */
	protected abstract StructureUpdateTask createUpdateTask2(StructureInfo structureInfo, String source,
			Location fileLocation);
	
	
	public void disconnectStructureUpdates2(StructureInfo structureInfo, IStructureModelListener structureListener,
			MDocumentSynchedAcess docAcess) {
		assertNotNull(structureInfo);
		assertNotNull(structureListener);
		log.println("disconnectStructureUpdates: " + structureInfo.getKey());
		
		Location fileLoc = getLocation(structureInfo.getKey());
		structureInfo.disconnectFromDocument(structureListener, createDisposeTask2(structureInfo, fileLoc), 
			docAcess);
	}
	
	/**
	 * Create a dispose task for the given structureInfo.
	 * @param fileLocation the file location if document is based on a file, null otherwise.
	 */
	protected abstract StructureUpdateTask createDisposeTask2(StructureInfo structureInfo, Location fileLocation);

	
	public class StructureInfo {
		
		protected final Object key;
		
		private final ListenerListHelper<IStructureModelListener> workingCopyListeners = new ListenerListHelper<>();
		
		private IDocument document = null;
		
		private SourceFileStructure structure = null;
		private StructureUpdateTask latestTask = null;
		private CountDownLatch latch = new CountDownLatch(0);
		
		public StructureInfo(Object key) {
			this.key = assertNotNull(key);
		}
		
		public final Object getKey() {
			return key;
		}
		
		public synchronized SourceFileStructure getStoredStructure() {
			return structure;
		}
		
		public synchronized boolean isWorkingCopy() {
			return workingCopyListeners.getListeners().size() > 0;
		}
		
		public synchronized boolean isStale() {
			return latestTask != null;
		}
		
		public synchronized boolean connectDocument(IDocument newDocument, IStructureModelListener structureListener) {
			if(document == null) {
				document = newDocument;
				connectDocumentListener(newDocument, this);
			}
			else if(document != newDocument) {
				return false;
			}
			
			workingCopyListeners.addListener(structureListener);
			return true;
		}
		
		private void connectDocumentListener(IDocument document, StructureInfo structureInfo) {
			document.addDocumentListener(docListener);
			queueUpdateTask(structureInfo, document);
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
				StructureUpdateTask disposeTask, @SuppressWarnings("unused") MDocumentSynchedAcess docAcess) {
			workingCopyListeners.removeListener(structureListener);
			if(!isWorkingCopy()) {
				document.removeDocumentListener(docListener);
				document = null;
			}
			
			if(disposeTask != null) {
				queueTask(disposeTask);
			}
		}
		
		public synchronized void queueTask(StructureUpdateTask updateTask) {
			if(latestTask != null) {
				assertTrue(latch.getCount() == 1);
				latestTask.cancel();
			} else {
				assertTrue(latch.getCount() == 0);
				latch = new CountDownLatch(1);
			}
			latestTask = updateTask;
			
			executor.submit(updateTask);
		}
		
		public synchronized void setNewStructure(SourceFileStructure newStructure, StructureUpdateTask updateTask) {
			if(latestTask != updateTask) {
				// Ignore, task is cancelled
				assertTrue(updateTask.isCancelled());
			} else {
				latestTask = null;
				structure = newStructure;
				latch.countDown();
				
				notifyStructureChanged(this, workingCopyListeners);
				notifyStructureChanged(this, modelListeners);
			}
		}
		
		/** @return an up-to-date SourceFileStructure, after all current update tasks are finish. 
		 * Can be null (if no updates were ever requested). */
		public SourceFileStructure getUpdatedStructure() throws InterruptedException {
			latch.await();
			return structure;
		}
		
		/**
		 * @see #getUpdatedStructure()
		 */
		public SourceFileStructure getUpdatedStructure(long timeout, TimeUnit unit) throws InterruptedException {
			boolean success = latch.await(timeout, unit);
			if(success) {
				return structure;
			}
			throw new InterruptedException();
		}
		
		public SourceFileStructure getUpdatedStructure(IProgressMonitor pm) throws OperationCancellation {
			while(true) {
				if(pm.isCanceled()) {
					throw new OperationCancellation();
				}
				
				try {
					return getUpdatedStructure(100, TimeUnit.MILLISECONDS);
				} catch(InterruptedException e) {
					continue;
				}
			}
		}
		
	}
	
	/** Marker interface to represent that one has access to {@link StructureInfo#document} . */
	public static class MDocumentSynchedAcess {
		
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class StructureUpdateTask implements Runnable {
		
		protected final StructureInfo structureInfo;
		protected volatile boolean cancelled = false;
		
		public StructureUpdateTask(StructureInfo structureInfo) {
			this.structureInfo = structureInfo;
		}
		
		public void cancel() {
			cancelled = true;
		}
		
		public boolean isCancelled() {
			return cancelled;
		}
		
		protected final ICancelMonitor cm = new ICancelMonitor() {
			@Override
			public boolean isCanceled() {
				return cancelled;
			}
		};
		
		@Override
		public void run() {
			Thread currentThread = Thread.currentThread();
			String originalName = currentThread.getName();
			try {
				currentThread.setName(originalName + " " + structureInfo.key);
				
				doRun();
			} catch(RuntimeException e) {
				LangCore.logInternalError(e);
				structureInfo.setNewStructure(null, this);
			} finally {
				currentThread.setName(originalName);
			}
		}
		
		protected void doRun() {
			if(cancelled) {
				return;
			}
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
	
	protected void notifyStructureChanged(final StructureInfo structureInfo, 
			ListenerListHelper<IStructureModelListener> listeners) {
		for(IStructureModelListener listener : listeners.getListeners()) {
			try {
				listener.structureChanged(structureInfo, structureInfo.structure);
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
		structureInfo.getUpdatedStructure(pm);
	}
	
	/* ----------------- util ----------------- */
	
	protected Location getLocation(Object key) {
		if(key instanceof Location) {
			return (Location) key;
		}
		return null;
	}
	
}