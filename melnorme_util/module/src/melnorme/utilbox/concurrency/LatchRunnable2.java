/*******************************************************************************
 * Copyright (c) 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.concurrency;

import java.util.concurrent.CountDownLatch;

import melnorme.utilbox.ownership.IDisposable;

/**
 * A {@link LatchRunnable2} provides a two way (entry and exit) concurrency barrier.
 * It is useful mostly for tests related to concurrent code.
 * (Improved version over {@link LatchRunnable})
 */
public class LatchRunnable2 implements Runnable {
	
	public final CountDownLatch entryLatch;
	public final CountDownLatch exitLatch;
	
	public LatchRunnable2() {
		this.entryLatch = new CountDownLatch(1);
		this.exitLatch = new CountDownLatch(1);
	}
	
	@Override
	public void run() {
		entryLatch.countDown();
		
		try {
			doRun();
		} finally {
			exitLatch.countDown();
		}
	}
	
	protected void doRun() {
	}
	
	public void awaitEntry() {
		awaitLatch(entryLatch);
	}
	
	public void awaitExit() {
		awaitLatch(exitLatch);
	}
	
	public static void awaitLatch(CountDownLatch latch) {
		while(true) {
			try {
				latch.await();
				return;
			} catch(InterruptedException e) {
				continue;
			}
		}
	}
	
	public void releaseAll() {
		entryLatch.countDown();
		exitLatch.countDown();
	}
	
	protected final IDisposable asDisposable = new IDisposable() {
		@Override
		public void dispose() {
			releaseAll(); // Ensure whatever was holding on the latch is released, even for error cleanup
		}
	};
	
	public IDisposable asDisposable() {
		return asDisposable;
	}

}