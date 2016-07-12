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

/**
 * A {@link LatchRunnable2} provides a two way (entry and exit) concurrency barrier.
 * It is useful mostly for tests related to concurrent code.
 * (Improved version over {@link LatchRunnable})
 */
public class LatchRunnable2 implements Runnable, AutoCloseable {
	
	public final CountDownLatch entryLatch;
	public final CountDownLatch exitLatch;
	
	public LatchRunnable2() {
		this(2, 2);
	}
	
	public LatchRunnable2(int entryCount, int exitCount) {
		this.entryLatch = new CountDownLatch(entryCount);
		this.exitLatch = new CountDownLatch(exitCount);
	}
	
	@Override
	public void run() {
		entryLatch.countDown();
		awaitLatch(entryLatch);
		
		try {
			doRun();
		} finally {
			exitLatch.countDown();
			awaitLatch(exitLatch);
		}
	}
	
	protected void doRun() {
	}
	
	protected void awaitLatch(CountDownLatch latch) {
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
	
	@Override
	public void close() {
		releaseAll(); // Ensure whatever was holding on the latch is released, even for error cleanup
	}
	
}