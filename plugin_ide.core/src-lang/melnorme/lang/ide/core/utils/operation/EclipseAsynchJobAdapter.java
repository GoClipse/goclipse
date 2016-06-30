/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.utils.operation;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.concurrent.CountDownLatch;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import melnorme.lang.ide.core.EclipseCore;

/**
 * A {@link EclipseAsynchJobAdapter} encompasses a task that should run associated with an Eclipse Job
 * (thus possibly visible to the UI, and cancellable),  but whose actual running code must execute 
 * in a thread other than the job thread.
 */
public class EclipseAsynchJobAdapter extends Job {
	
	protected final CountDownLatch jobStartLatch = new CountDownLatch(1);
	protected volatile IProgressMonitor monitor;
	
	protected EclipseAsynchJobAdapter(String name) {
		super(name);
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		jobStartLatch.countDown();
		return ASYNC_FINISH;
	}
	
	
	public static void runUnderAsynchJob(String jobName, IRunnableWithJob runnable) throws InterruptedException {
		EclipseAsynchJobAdapter job = new EclipseAsynchJobAdapter(jobName);
		job.schedule();
		
		try {
			job.jobStartLatch.await();
			assertNotNull(job.monitor);
			
			job.setThread(Thread.currentThread());
			runnable.runUnderEclipseJob(job.monitor);
		} finally {
			job.done(EclipseCore.createOkStatus(null));
		}
	}
	
	public static interface IRunnableWithJob {
		
		/** The main code to run. An Eclipse asynch job connect to this thread will be active.
		 * The given monitor comes from that job. */
		void runUnderEclipseJob(IProgressMonitor monitor);
		
	}
	
}