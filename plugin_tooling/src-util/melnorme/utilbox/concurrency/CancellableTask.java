/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.concurrent.FutureTask;

/**
 * A task that can be cancelled before, or while it is running.
 * If cancelled while it is running, the running thread is interrupted.
 * 
 * - This is related to {@link FutureTask}, achieves part of what it does.
 * I do wonder if we should be using {@link FutureTask} instead as it might be more robust than rolling our own.
 * (although we don't need the whole of {@link FutureTask}, just a small subset)
 *
 */
public abstract class CancellableTask implements ICancellableTask {
	
	private boolean cancelled = false;
	private Thread runningThread;
	private boolean executed = false;
	
	protected final ICancelMonitor cm = this::isCancelled;
	
	public synchronized boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public boolean canExecute() {
		return !hasExecuted();
	}
	
	public synchronized boolean hasExecuted() {
		return executed;
	}
	
	public void cancel() {
		tryCancel();
	}
	
	@Override
	public synchronized boolean tryCancel() {
		if(cancelled) {
			return false;
		}
		cancelled = true;
		
		if(runningThread != null) {
			runningThread.interrupt();
		}
		return true;
	}
	
	@Override
	public final void run() {
		synchronized(this) {
			
			if(isCancelled()) {
				return;
			}
			
			// Can only run once
			checkExecuted();
			assertTrue(runningThread == null);
			runningThread = Thread.currentThread();
		}
		
		String taskDisplayName = getTaskDisplayName();
		
		if(taskDisplayName == null) {
			doRun();
		} else {
			String originalName = runningThread.getName();
			try {
				runningThread.setName(originalName + " >> " + taskDisplayName);
				doRun();
			} finally {
				runningThread.setName(originalName);
			}
		}
		
		synchronized(this) {
			// make it null so that the thread can no longer be interrupted
			runningThread = null;
			Thread.interrupted();
		}
	}
	
	protected void checkExecuted() {
		assertTrue(executed == false); // Check that run is only called once
		executed = true;
	}
	
	public String getTaskDisplayName() {
		return null;
	}
	
	protected abstract void doRun();

}