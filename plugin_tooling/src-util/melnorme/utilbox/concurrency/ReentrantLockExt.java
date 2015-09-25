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
package melnorme.utilbox.concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Minor Extension to {@link ReentrantLock} to allow using of {@link AutoUnlockable}
 */
@SuppressWarnings("serial")
public class ReentrantLockExt extends ReentrantLock implements AutoUnlockable {
	
	public AutoUnlockable lock_() {
		super.lock();
		return this;
	}
	
	public AutoUnlockable lockInterruptibly_() throws InterruptedException {
		super.lockInterruptibly();
		return this;
	}
	
	@Override
	public void close() throws IllegalMonitorStateException {
		 unlock();
	}
	
}