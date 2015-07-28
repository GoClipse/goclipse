/*******************************************************************************
 * Copyright (c) 2013, 2013 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.utilbox.process;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Abstract helper class to start an external process and read its output concurrently,
 * using one or two reader threads (for stdout and stderr).
 * It also supports waiting for process termination with timeouts.
 * 
 * Subclasses must specify Runnable's for the worker threads reading the process stdout and stderr streams.
 */
public abstract class AbstractExternalProcessHelper {
	
	public static final int NO_TIMEOUT = -1;
	
	protected final Process process;
	protected final boolean readStdErr;
	
	/** This latch exists to signal that the process has terminated, and also that both reader threads 
	 * have finished reading all input. This last aspect is very important. */
	protected final CountDownLatch readersTerminationLatch;
	
	protected final Thread mainReaderThread;
	protected final Thread stderrReaderThread; // Can be null
	
	public AbstractExternalProcessHelper(ProcessBuilder pb) throws IOException {
		this(pb.start(), pb.redirectErrorStream() == false, true);
	}
	
	public AbstractExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders) {
		this.process = process;
		this.readStdErr = readStdErr;
		
		readersTerminationLatch = new CountDownLatch(2);
		
		mainReaderThread = new ProcessHelperMainThread(createMainReaderTask());
		
		if(readStdErr) {
			stderrReaderThread = new ProcessHelperStdErrThread(createStdErrReaderTask());
		} else {
			readersTerminationLatch.countDown(); // dont start stderr thread, so update latch
			stderrReaderThread = null;
		}
		if(startReaders) {
			startReaderThreads();
		}
	}
	
	public void startReaderThreads() {
		mainReaderThread.start();
		if(stderrReaderThread != null) {
			stderrReaderThread.start();
		}
	}
	
	public Process getProcess() {
		return process;
	}
	
	public boolean isReadingStdErr() {
		return readStdErr;
	}
	
	public boolean areReadersTerminated() {
		return readersTerminationLatch.getCount() == 0;
	}
	
	protected abstract Runnable createMainReaderTask();
	
	protected abstract Runnable createStdErrReaderTask();
	
	protected String getBaseNameForWorkerThreads() {
		return getClass().getSimpleName();
	}
	
	protected class ProcessHelperMainThread extends Thread {
		
		public ProcessHelperMainThread(Runnable runnable) {
			super(runnable, getBaseNameForWorkerThreads() + ".MainWorker");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				super.run();
			} finally {
				waitForProcessIndefinitely();
				readersTerminationLatch.countDown();
				
				mainReaderThread_Terminated();
			}
		}
		
		protected void waitForProcessIndefinitely() {
			while(true) {
				try {
					process.waitFor();
					return;
				} catch (InterruptedException e) {
					// retry waitfor, we must ensure process is terminated.
				}
			}
		}
		
	}
	
	/** Callback method for when main reader thread is about to terminate. Subclasses can extend. */
	public void mainReaderThread_Terminated() {
	}
	
	protected class ProcessHelperStdErrThread extends Thread {
		
		public ProcessHelperStdErrThread(Runnable runnable) {
			super(runnable, getBaseNameForWorkerThreads() + ".StdErrWorker");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				super.run();
			} finally {
				readersTerminationLatch.countDown();
			}
		}
		
	}
	
	/*----------  Waiting functionality ----------*/
	
	/**
	 * Await termination of process, with given timeoutMs timeout in milliseconds (-1 for no timeout).
	 * Periodically polls for cancellation.
	 * @return the process exit value.
	 * @throws InterruptedException if thread interrupted, or if cancellation is polled.
	 * @throws TimeoutException if timeout reached.
	 */
	protected int awaitTermination(int timeoutMs) throws InterruptedException, TimeoutException {
		int waitedTime = 0;
		
		while(true) {
			int cancelPollPeriodMs = getCancelPollingPeriodMs();
			boolean latchSuccess = doAwaitTermination(cancelPollPeriodMs);
			if(latchSuccess) {
				return process.exitValue();
			}
			if(isCanceled()) {
				throw new InterruptedException();
			}
			if(timeoutMs != NO_TIMEOUT && waitedTime >= timeoutMs) {
				throw new TimeoutException();
			}
			waitedTime += cancelPollPeriodMs;
		}
	}
	
	protected boolean doAwaitTermination(int cancelPollPeriodMs) throws InterruptedException {
		return readersTerminationLatch.await(cancelPollPeriodMs, TimeUnit.MILLISECONDS);
	}
	
	protected int getCancelPollingPeriodMs() {
		return 200;
	}
	
	protected abstract boolean isCanceled();
	
}