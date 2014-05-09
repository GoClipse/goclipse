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
	
	/** This latch exists to signal that the process has terminated, and also that the reader threads 
	 * have finished reading all input. This last aspect is very important. */
	protected final CountDownLatch fullTerminationLatch;
	
	protected final Thread mainReaderThread;
	protected final Thread stderrReaderThread; // Can be null
	
	public AbstractExternalProcessHelper(ProcessBuilder pb) throws IOException {
		this(pb.start(), pb.redirectErrorStream() == false, true);
	}
	
	public AbstractExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders) {
		this.process = process;
		this.readStdErr = readStdErr;
		
		fullTerminationLatch = new CountDownLatch(2);
		
		mainReaderThread = new ProcessHelperMainThread(createMainReaderTask());
		
		if(readStdErr) {
			stderrReaderThread = new ProcessHelperStdErrThread(createStdErrReaderTask());
		} else {
			fullTerminationLatch.countDown(); // dont start stderr thread, so update latch
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
	
	public boolean isFullyTerminated() {
		return fullTerminationLatch.getCount() == 0;
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
				fullTerminationLatch.countDown();
				
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
				fullTerminationLatch.countDown();
			}
		}
		
	}
	
	/*----------  Waiting functionality ----------*/
	
	/**
	 * Await termination of process, with given timeoutMs timeout. Can use -1 for no timeout.
	 * Periodically polls cancel monitor, if one exists.
	 * @return true if termination reached, false if timeout reached, or cancel requested.
	 * @throws InterruptedException if interrupted.
	 */
	protected boolean awaitTermination(int timeoutMs) throws InterruptedException {
		int waitedTime = 0;
		
		while(true) {
			int cancelPollPeriodMs = getCancelPollingPeriodMs();
			boolean latchSuccess = fullTerminationLatch.await(cancelPollPeriodMs, TimeUnit.MILLISECONDS);
			if(latchSuccess) {
				return true;
			}
			if(isCanceled()) {
				return false;
			}
			if(timeoutMs != NO_TIMEOUT && waitedTime >= timeoutMs) {
				return false;
			}
			waitedTime += cancelPollPeriodMs;
		}
	}
	
	protected int getCancelPollingPeriodMs() {
		return 200;
	}
	
	protected abstract boolean isCanceled();
	
	/** 
	 * Same as {@link #awaitTermination(int))} but: 
	 * throws exception on any outcome other than successful awaitTermination.
	 * Note that even with no timeout provided (value -1), a TimeoutException can be thrown if cancel is requested.
	 * @return The process exit value.
	 */
	public int awaitTerminationStrict(int timeoutMs) throws InterruptedException, TimeoutException {
		boolean success = awaitTermination(timeoutMs);
		if(!success) {
			throw new TimeoutException();
		}
		return process.exitValue();
	}
	
	
	public int awaitTerminationStrict_destroyOnException(int timeoutMs) 
			throws InterruptedException, TimeoutException {
		try {
			return awaitTerminationStrict(timeoutMs);
		} catch (Exception e) {
			process.destroy();
			throw e;
		}
	}
	
	public int awaitTerminationStrict_destroyOnException() throws InterruptedException, TimeoutException {
		return awaitTerminationStrict_destroyOnException(NO_TIMEOUT);
	}
	
}