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

/**
 * 
 */
public class ConcurrentDerivedData<DATA> {
	
	private DATA data = null;
	private UpdateTask latestUpdateTask = null;
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
	
	public synchronized void setUpdateTask(UpdateTask newUpdateTask) {
		if(latestUpdateTask == null) {
			assertTrue(latch.getCount() == 0);
			latch = new CountDownLatch(1);
		} else {
			assertTrue(latch.getCount() == 1);
			latestUpdateTask.cancel();
		}
		latestUpdateTask = newUpdateTask;
	}
	
	public synchronized void setNewStructure(DATA newData, UpdateTask updateTask) {
		if(latestUpdateTask != updateTask) {
			// Ignore, means this update task was cancelled
			assertTrue(updateTask.isCancelled());
		} else {
			latestUpdateTask = null;
			data = newData;
			latch.countDown();
			
			doHandleDataChanged();
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
	 * @see #awaitUpdatedData(long, TimeUnit)
	 */
	public DATA awaitUpdatedData(long timeout, TimeUnit unit) throws InterruptedException {
		boolean success = getLatchForCurrentUpdate().await(timeout, unit);
		if(success) {
			return data;
		}
		throw new InterruptedException();
	}
	
}