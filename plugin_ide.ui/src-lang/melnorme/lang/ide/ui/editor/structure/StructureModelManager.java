/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.editor.structure;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.utils.EntryMapExt;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.Location;

public abstract class StructureModelManager {
	
	public static StructureModelManager manager = LangUIPlugin_Actual.createStructureModelManager();
	
	public static StructureModelManager getDefault() {
		return manager;
	}
	
	/* -----------------  ----------------- */
	
	
	protected final ExecutorService executor = Executors.newCachedThreadPool();
	
	// TODO: ideally this should be a cache, otherwise it's a potential memory leak if structures are not removed.
	protected final EntryMapExt<Location, StructureInfo> structureInfos = 
			new EntryMapExt<Location, StructureInfo>() {
		@Override
		protected StructureInfo createEntry(Location key) {
			return new StructureInfo(key);
		}
	};
	
	public StructureInfo getStructureInfo(Location location) {
		return structureInfos.getEntry(location);
	}
	
	public SourceFileStructure getStructure(Location location) {
		return getStructureInfo(location).getStructure();
	}
	
	public void queueRebuildStructure(Location location, String source) {
		getStructureInfo(location).queueRebuild(source);
	}
	
	
	public class StructureInfo {
		
		protected final Location location;
		protected SourceFileStructure structure;
		protected StructureUpdateTask updater;
		protected volatile CountDownLatch latch = new CountDownLatch(0);
		
		public StructureInfo(Location location) {
			this.location = location;
		}
		
		public synchronized SourceFileStructure getStructure() {
			return structure;
		}
		
		public synchronized boolean isStale() {
			return updater != null;
		}
		
		public synchronized void queueRebuild(final String source) {
			if(updater != null) {
				updater.cancelTask();
			} else {
				assertTrue(latch.getCount() == 0);
				latch = new CountDownLatch(1);
			}
			updater = createStructureUpdateTask(location, source);
			executor.submit(updater);
		}
		
		public synchronized void setNewStructure(SourceFileStructure newStructure, StructureUpdateTask updateTask) {
			if(updater != updateTask) {
				return; // Ignore, only updater is valid to update structure
			}
			updater = null;
			latch.countDown();
			this.structure = newStructure;
			
			notifyListeners(location, newStructure);
		}
		
		public SourceFileStructure getUpdatedStructure() throws InterruptedException {
			latch.await();
			return getStructure();
		}
		
		public SourceFileStructure getUpdatedStructure(long timeout, TimeUnit unit) throws InterruptedException {
			latch.await(timeout, unit);
			return getStructure();
		}
		
	}

	protected abstract StructureUpdateTask createStructureUpdateTask(Location location, final String source);
	
	public abstract class StructureUpdateTask implements Runnable {
		
		protected final Location location;
		protected final String source;
		
		protected volatile boolean cancelled = true;
		
		protected final ICancelMonitor cm = new ICancelMonitor() {
			@Override
			public boolean isCanceled() {
				return cancelled;
			}
		};
		
		public StructureUpdateTask(Location location, String source) {
			this.location = location;
			this.source = source;
		}
		
		public void cancelTask() {
			cancelled = true;
		}
		
		@Override
		public void run() {
			SourceFileStructure newStructure = createSourceFileStructure();
			
			StructureInfo structureInfo = getStructureInfo(location);
			if(cancelled) {
				return;
			}
			structureInfo.setNewStructure(newStructure, this);
		}
		
		protected abstract SourceFileStructure createSourceFileStructure();
		
	}
	
	/* -----------------  ----------------- */
	
	protected final ListenerListHelper<IStructureModelListener> listenerList = new ListenerListHelper<>();
	
	public void addListener(IStructureModelListener listener) {
		listenerList.addListener(listener);
	}
	
	public void removeListener(IStructureModelListener listener) {
		listenerList.removeListener(listener);
	}
	
	protected void notifyListeners(Location location, SourceFileStructure sourceFileStructure) {
		for(IStructureModelListener listener : listenerList.getListeners()) {
			try {
				listener.structureChanged(location, sourceFileStructure);
			} catch (Exception e) {
				LangCore.logInternalError(e);
			}
		}
	}
	
}