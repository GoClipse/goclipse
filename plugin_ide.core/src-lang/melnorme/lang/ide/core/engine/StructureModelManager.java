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
import melnorme.utilbox.misc.ListenerListHelper;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The purpose of Engine manager is two-fold:
 * 
 * - Keep track of document text changes and send those updates to the engine.
 * - Retrieve parsing/structural information from the client after such text updates.
 *
 */
public class StructureModelManager {
	
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
	
	public StructureInfo getStructureInfo(Object key) {
		return structureInfos.getEntry(key);
	}
	
	public SourceFileStructure getStoredStructure(Object key) {
		return structureInfos.getEntry(key).getStoredStructure();
	}
	
	
	public class StructureInfo {
		
		protected final Object key;
		
		protected SourceFileStructure structure = null;
		protected StructureUpdateTask latestTask = null;
		protected CountDownLatch latch = new CountDownLatch(0);
		
		public StructureInfo(Object key) {
			this.key = assertNotNull(key);
		}
		
		public Object getKey() {
			return key;
		}
		
		public synchronized SourceFileStructure getStoredStructure() {
			return structure;
		}
		
		public synchronized boolean isStale() {
			return latestTask != null;
		}
		
		public synchronized void queueUpdateTask(StructureUpdateTask updateTask) {
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
				
				notifyStructureChanged(this);
			}
		}
		
		/** @return an up-to-date SourceFileStructure, after all current update tasks are finish. 
		 * Can be null (if no updates were ever requested). */
		public SourceFileStructure getCurrentStructure() throws InterruptedException {
			latch.await();
			return structure;
		}
		
		/**
		 * @see #getCurrentStructure()
		 */
		public SourceFileStructure getCurrentStructure(long timeout, TimeUnit unit) throws InterruptedException {
			boolean success = latch.await(timeout, unit);
			if(success) {
				return structure;
			}
			throw new InterruptedException();
		}
		
		public SourceFileStructure getCurrentStructure(IProgressMonitor pm) throws OperationCancellation {
			while(true) {
				if(pm.isCanceled()) {
					throw new OperationCancellation();
				}
				
				try {
					return getCurrentStructure(100, TimeUnit.MILLISECONDS);
				} catch(InterruptedException e) {
					continue;
				}
			}
		}
		
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
			} catch(Exception e) {
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
	
	protected final ListenerListHelper<IStructureModelListener> listenerList = new ListenerListHelper<>();
	
	public void addListener(IStructureModelListener listener) {
		assertNotNull(listener);
		listenerList.addListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		listenerList.removeListener(listener);
	}
	
	protected void notifyStructureChanged(final StructureInfo structureInfo) {
		for(IStructureModelListener listener : listenerList.getListeners()) {
			try {
				listener.structureChanged(structureInfo.structure, structureInfo);
			} catch (Exception e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
}