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

import java.util.concurrent.locks.ReentrantLock;

import melnorme.utilbox.core.fntypes.CallableX;

public class ConcurrencyUtil {
	
	/**
	 * Run given runnable under given lock
	 */
	public static void runUnderLock(ReentrantLock lock, Runnable runnable) {
		lock.lock();
		try {
			runnable.run();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Call given callable under given lock, return the callable result.
	 */
	public static <RET, EXC extends Exception> RET callUnderLock(ReentrantLock lock, CallableX<RET, EXC> callable) 
			throws EXC {
		lock.lock();
		try {
			return callable.call();
		} finally {
			lock.unlock();
		}
	}
	
}