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
package melnorme.utilbox.concurrency;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Same behavior as {@link Executors#defaultThreadFactory()}, 
 * but allows setting a custom name for created threads.
 */
public class NamingThreadFactory implements ThreadFactory {
	
	protected final ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
	
	protected final String poolName;
	protected boolean useThreadNumber;
	protected final AtomicInteger threadNumber = new AtomicInteger(1);
	
	public NamingThreadFactory(String poolName) {
		this(poolName, false);
	}
	
	public NamingThreadFactory(String poolName, boolean useThreadNumber) {
		this.poolName = poolName;
		this.useThreadNumber = useThreadNumber;
	}
	
	@Override
	public Thread newThread(Runnable runable) {
		Thread thread = defaultThreadFactory.newThread(runable);
		
		thread.setName(getThreadName());
		
		return thread;
	}
	
	protected String getThreadName() {
		if(useThreadNumber) {
			return poolName + "-" + threadNumber.getAndIncrement();
		} else {
			return poolName;
		}
	}
	
}