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
package melnorme.utilbox.process;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.NullCancelMonitor;
import melnorme.utilbox.concurrency.IRunnableFuture2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.StreamUtil;

/**
 * Abstract helper class to start an external process and read its output concurrently,
 * using one or two reader threads (for stdout and stderr).
 * It also supports waiting for process termination with timeouts.
 * 
 * Subclasses must specify Runnable's for the worker threads reading the process stdout and stderr streams.
 */
public abstract class AbstractExternalProcessHelper<
	STDOUT_TASK extends IRunnableFuture2<? extends Result<?, IOException>>, 
	STDERR_TASK extends IRunnableFuture2<? extends Result<?, IOException>>
> implements IExternalProcessHandler {
	
	protected final Process process;
	protected final boolean readStdErr;
	protected final ICancelMonitor cancelMonitor;
	
	protected STDOUT_TASK mainReader;
	protected STDERR_TASK stderrReader; // Can be null
	
	protected final CountDownLatch readerThreadsTerminationLatch;
	/** This latch exists to signal that the process has terminated, and also that both reader threads 
	 * have finished reading all input. This last aspect is very important. */
	protected final CountDownLatch readersAndProcessTerminationLatch;
	
	protected final Thread mainReaderThread;
	protected final Thread stderrReaderThread; // Can be null
	
	public AbstractExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders,
			ICancelMonitor cancelMonitor) {
		this.process = process;
		this.readStdErr = readStdErr;
		this.cancelMonitor = cancelMonitor == null ? new NullCancelMonitor() : cancelMonitor;
		
		readerThreadsTerminationLatch= new CountDownLatch(2);
		readersAndProcessTerminationLatch = new CountDownLatch(1);
		
		mainReaderThread = new ProcessHelperMainThread(createMainReaderTask());
		
		if(readStdErr) {
			stderrReaderThread = new ProcessHelperStdErrThread(createStdErrReaderTask());
		} else {
			readerThreadsTerminationLatch.countDown(); // dont start stderr thread, so update latch
			stderrReaderThread = null;
		}
		if(startReaders) {
			startReaderThreads();
		}
	}
	
	protected abstract STDOUT_TASK createMainReaderTask();
	
	protected abstract STDERR_TASK createStdErrReaderTask();
	
	@Override
	public STDOUT_TASK getStdOutTask() {
		return mainReader;
	}
	
	@Override
	public STDERR_TASK getStdErrTask() {
		return stderrReader;
	}
	
	public void startReaderThreads() {
		mainReaderThread.start();
		if(stderrReaderThread != null) {
			stderrReaderThread.start();
		}
	}
	
	@Override
	public Process getProcess() {
		return process;
	}
	
	public boolean isReadingStdErr() {
		return readStdErr;
	}
	
	public boolean areReadersTerminated2() {
		return readerThreadsTerminationLatch.getCount() == 0;
	}
	
	public boolean areReadersAndProcessTerminated() {
		return readersAndProcessTerminationLatch.getCount() == 0;
	}
	
	protected String getBaseNameForWorkerThreads() {
		String simpleName = getClass().getSimpleName();
		if(simpleName.isEmpty()) {
			return getClass().getName();
		}
		return simpleName;
		
	}
	
	protected class ProcessHelperMainThread extends Thread {
		
		public ProcessHelperMainThread(Runnable runnable) {
			super(runnable, getBaseNameForWorkerThreads() + "/StdOutReader");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				super.run();
			} finally {
				readerThreadsTerminationLatch.countDown();
				
				waitForProcessIndefinitely();
				readersAndProcessTerminationLatch.countDown();
				
				mainReaderThread_Terminated();
			}
		}
		
		protected void waitForProcessIndefinitely() {
			while(true) {
				try {
					process.waitFor();
					// Await stderr too:
					readerThreadsTerminationLatch.await();
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
			super(runnable, getBaseNameForWorkerThreads() + "/StdErrReader");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			try {
				super.run();
			} finally {
				readerThreadsTerminationLatch.countDown();
			}
		}
		
	}
	
	/*----------  Termination awaiting functionality ----------*/
	
	protected abstract boolean isCanceled();
	
	/**
	 * Await termination of process, with given timeoutMs timeout in milliseconds (-1 for no timeout).
	 * Periodically polls for cancellation.
	 * @return the process exit value.
	 * @throws InterruptedException if thread interrupted.
	 * @throws TimeoutException if timeout reached.
	 * @throws OperationCancellation if process reader cancellation was requested.
	 */
	protected void awaitReadersTermination(int timeoutMs) 
			throws InterruptedException, TimeoutException, OperationCancellation {
		int waitedTime = 0;
		
		while(true) {
			if(isCanceled()) {
				throw new OperationCancellation();
			}
			int cancelPollPeriodMs = getCancelPollingPeriodMs();
			boolean latchSuccess = doAwaitTermination(cancelPollPeriodMs);
			if(latchSuccess) {
				return;
			}
			if(timeoutMs != NO_TIMEOUT && waitedTime >= timeoutMs) {
				throw new TimeoutException();
			}
			waitedTime += cancelPollPeriodMs;
		}
	}
	
	protected int getCancelPollingPeriodMs() {
		return 200;
	}
	
	protected boolean doAwaitTermination(int cancelPollPeriodMs) throws InterruptedException {
		return readersAndProcessTerminationLatch.await(cancelPollPeriodMs, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void awaitTermination(int timeoutMs, boolean destroyOnError) 
			throws InterruptedException, TimeoutException, OperationCancellation, IOException {
		try {
			awaitReadersTermination(timeoutMs);
			
			// Check for IOExceptions (although I'm not sure this scenario is possible)
			mainReader.awaitResult2().get();
			if(stderrReader != null) {
				stderrReader.awaitResult2().get();
			}
		} catch(Exception e) {
			if(destroyOnError) {
				destroyProcess();
			}
			throw e;
		}
	}
	
	protected void destroyProcess() {
		process.destroy();
	}
	
	/* ----------------- writing helpers ----------------- */
	
	@Override
	public void writeInput(String input, Charset charset) throws IOException {
		if(input == null)
			return;
		
		OutputStream processInputStream = getProcess().getOutputStream();
		StreamUtil.writeStringToStream(input, processInputStream, charset);
	}
	
}