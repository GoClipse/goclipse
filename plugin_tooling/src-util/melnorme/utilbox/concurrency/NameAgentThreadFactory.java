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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Similar behavior as DefaultThreadFactory, but allows seting an unique name for the pool/executor threads.
 */
public class NameAgentThreadFactory implements ThreadFactory {
	
	protected final String poolName;
	protected boolean useThreadNumber;
	protected final AtomicInteger threadNumber = new AtomicInteger(1);
	protected final ThreadGroup group;
	
	public NameAgentThreadFactory(String poolName) {
		this(poolName, false);
	}
	
	public NameAgentThreadFactory(String poolName, boolean useThreadNumber) {
		this.poolName = poolName;
		this.useThreadNumber = useThreadNumber;
		SecurityManager sm = System.getSecurityManager();
		this.group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
	}
	
	@Override
	public Thread newThread(Runnable runable) {
		String threadName = poolName;
		if(useThreadNumber) {
			threadName = poolName + "-" + threadNumber.getAndIncrement();
		}
		Thread thread = new Thread(group, runable, threadName, 0);
		
		if(thread.isDaemon()) {
			thread.setDaemon(false);
		}
		if(thread.getPriority() != Thread.NORM_PRIORITY) {
			thread.setPriority(Thread.NORM_PRIORITY);
		}
		return thread;
	}
	
}