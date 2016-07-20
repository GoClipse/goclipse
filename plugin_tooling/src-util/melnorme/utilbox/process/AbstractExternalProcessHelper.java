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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.AbstractRunnableFuture2;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.ICancelMonitor.NullCancelMonitor;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;

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
	protected final ICancelMonitor cancelMonitor;
	
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
	
	public boolean areReadersTerminated2() {
		return readerThreadsTerminationLatch.getCount() == 0;
	}
	
	public boolean areReadersAndProcessTerminated() {
		return readersAndProcessTerminationLatch.getCount() == 0;
	}
	
	protected abstract Runnable createMainReaderTask();
	
	protected abstract Runnable createStdErrReaderTask();
	
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
	
	/* ----------------- Reader Task ----------------- */
	
	public static abstract class ReaderTask<RET> 
		extends AbstractRunnableFuture2<Result<RET, IOException>> 
	{
		
		protected final InputStream is;
		protected final ICancelMonitor cancelMonitor;
		
		public ReaderTask(InputStream is, ICancelMonitor cancelMonitor) {
			this.is = assertNotNull(is);
			this.cancelMonitor = assertNotNull(cancelMonitor);
		}
		
		@Override
		protected Result<RET, IOException> internalInvoke() {
			return Result.callToResult(this::doRun);
		}
		
		public RET doRun() throws IOException {
			// BM: Hum, should we treat an IOException not as an error, but just like an EOF?
			try {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				
				int read;
				while((read = is.read(buffer)) != StreamUtil.EOF && !cancelMonitor.isCanceled()) {
					notifyReadChunk2(buffer, 0, read);
				}
				return doGetReturnValue();
			} finally {
				is.close();
			}
		}
		
		protected abstract RET doGetReturnValue();
		
		@SuppressWarnings("unused")
		protected void notifyReadChunk2(byte[] buffer, int offset, int readCount) {
			// Default implementation: do nothing
		}
		
	}
	
	/*----------  Waiting functionality ----------*/
	
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
	
	protected abstract boolean isCanceled();
	
	/* ----------------- writing helpers ----------------- */
	
	public void writeInput(String input) throws IOException {
		writeInput(input, StringUtil.UTF8);
	}
	
	public void writeInput(String input, Charset charset) throws IOException {
		if(input == null)
			return;
		
		OutputStream processInputStream = getProcess().getOutputStream();
		StreamUtil.writeStringToStream(input, processInputStream, charset);
	}
	
	public void writeInput_(String input) throws CommonException {
		writeInput_(input, StringUtil.UTF8);
	}
	
	public void writeInput_(String input, Charset charset) throws CommonException {
		try {
			writeInput(input, charset);
		} catch (IOException e) {
			throw createCommonException(ProcessHelperMessages.ExternalProcess_ErrorWritingInput, e);
		}
	}
	
	protected CommonException createCommonException(String message, Throwable cause) {
		return new CommonException(message, cause);
	}
	
}