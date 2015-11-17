/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.utils.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import melnorme.utilbox.concurrency.ICancelMonitor;

/**
 * 
 */
public class ConcurrentlyDerivedData<DATA> {
	
	private DATA data = null;
	private DataUpdateTask<DATA> latestUpdateTask = null;
	private CountDownLatch latch = new CountDownLatch(0);
	
	public synchronized DATA getStoredData() {
		return data;
	}
	
	public synchronized boolean isStale() {
		return latestUpdateTask != null;
	}
	
	public synchronized void runSynchronized(Runnable runnable) {
		runnable.run();
	}
	
	public synchronized CountDownLatch getLatchForCurrentUpdate() {
		return latch;
	}
	
	public synchronized void setUpdateTask(DataUpdateTask<DATA> newUpdateTask) {
		if(latestUpdateTask == null) {
			assertTrue(latch.getCount() == 0);
			latch = new CountDownLatch(1);
		} else {
			assertTrue(latch.getCount() == 1);
			latestUpdateTask.cancel();
		}
		latestUpdateTask = newUpdateTask;
	}
	
	public synchronized void setNewData(DATA newData, DataUpdateTask<DATA> updateTask) {
		if(latestUpdateTask != updateTask) {
			// Ignore, means this update task was cancelled
			assertTrue(updateTask.isCancelled());
		} else {
			latestUpdateTask = null;
			data = newData;
			doHandleDataChanged();
			
			latch.countDown();
		}
	}
	
	protected void doHandleDataChanged() {
	}
	
	/** Waits for the current update task to finish, and returns the first non-stale data that becomes available.
	 * Note that the data can become stale if a new update is requested after this method returns.
	 * @return an up-to-date data. Can be null (if no updates were ever requested). 
	 */
	public DATA awaitUpdatedData() throws InterruptedException {
		getLatchForCurrentUpdate().await();
		return data;
	}
	
	/**
	 * @see #awaitUpdatedData()
	 */
	public DATA awaitUpdatedData(long timeout, TimeUnit unit) throws InterruptedException {
		boolean success = getLatchForCurrentUpdate().await(timeout, unit);
		if(success) {
			return data;
		}
		throw new InterruptedException();
	}
	
	/* -----------------  ----------------- */
	
	public static abstract class DataUpdateTask<DATA> implements Runnable {
		
		protected final ConcurrentlyDerivedData<DATA> derivedData;
		protected final String taskDisplayName;
		
		public DataUpdateTask(ConcurrentlyDerivedData<DATA> derivedData, String taskDisplayName) {
			this.taskDisplayName = taskDisplayName;
			this.derivedData = derivedData;
		}
		
		private boolean cancelled = false;
		private Thread thread;
		
		protected final ICancelMonitor cm = this::isCancelled;
		
		public synchronized void cancel() {
			cancelled = true;
			if(thread != null) {
				thread.interrupt();
			}
		}
		
		public synchronized boolean isCancelled() {
			return cancelled;
		}
		
		@Override
		public void run() {
			synchronized(this) {
				if(cancelled) {
					return;
				} else {
					thread = Thread.currentThread();
				}
			}
			
			String originalName = thread.getName();
			try {
				thread.setName(originalName + " : " + taskDisplayName);
				
				DATA newData = createNewData();
				derivedData.setNewData(newData, this);
				
			} catch(RuntimeException e) {
				derivedData.setNewData(null, this);
				handleRuntimeException(e);
			} finally {
				thread.setName(originalName);
			}
		}
		
		protected abstract void handleRuntimeException(RuntimeException e);
		
		protected abstract DATA createNewData();
		
	}
	
}