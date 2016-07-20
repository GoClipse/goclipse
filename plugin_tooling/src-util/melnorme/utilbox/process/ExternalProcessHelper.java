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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.misc.IByteSequence;
import melnorme.utilbox.process.ExternalProcessHelper.ReadAllBytesTask;

/**
 * Helper for running external processes.
 * Reads all stdout and stderr output into a byte array (using worker threads)
 * 
 * @see AbstractExternalProcessHelper
 */
public class ExternalProcessHelper 
	extends AbstractExternalProcessHelper<ReadAllBytesTask, ReadAllBytesTask> 
{
	
	public ExternalProcessHelper(ProcessBuilder pb) throws IOException {
		this(pb.start(), pb.redirectErrorStream() == false, true);
	}
	
	public ExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders) {
		this(process, readStdErr, startReaders, null);
	}
	
	public ExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders, 
			ICancelMonitor cancelMonitor) {
		super(process, readStdErr, startReaders, cancelMonitor);
	}
	
	@Override
	protected boolean isCanceled() {
		return cancelMonitor.isCanceled();
	}
	
	@Override
	protected ReadAllBytesTask createMainReaderTask() {
		return mainReader = new ReadAllBytesTask(process.getInputStream(), cancelMonitor);
	}
	
	@Override
	protected ReadAllBytesTask createStdErrReaderTask() {
		return stderrReader = new ReadAllBytesTask(process.getErrorStream(), cancelMonitor);
	}
	
	public static class ReadAllBytesTask extends ReaderTask<ByteArrayOutputStreamExt> {
		
		protected final ByteArrayOutputStreamExt byteArray = new ByteArrayOutputStreamExt(32);
		
		public ReadAllBytesTask(InputStream is, ICancelMonitor cancelMonitor) {
			super(is, cancelMonitor);
		}
		
		@Override
		protected void notifyReadChunk2(byte[] buffer, int offset, int readCount) {
			byteArray.write(buffer, 0, readCount);
		}
		
		@Override
		protected ByteArrayOutputStreamExt doGetReturnValue() {
			return byteArray;
		}
		
	}
	
	/* ----------------- result helpers ----------------- */
	
	protected ByteArrayOutputStreamExt getStdOutBytes3() throws IOException {
		return mainReader.getResult_forSuccessfulyCompleted().get();
	}
	
	protected ByteArrayOutputStreamExt getStdErrBytes3() throws IOException {
		if(readStdErr) {
			return stderrReader.getResult_forSuccessfulyCompleted().get();
		}
		return null;
	}
	
	public static class ExternalProcessResult {
		
		public final int exitValue;
		public final ByteArrayOutputStreamExt stdout;
		public final ByteArrayOutputStreamExt stderr;
		
		public ExternalProcessResult(int exitValue, ByteArrayOutputStreamExt stdout, ByteArrayOutputStreamExt stderr) {
			this.exitValue = exitValue;
			this.stdout = assertNotNull(stdout);
			this.stderr = stderr != null ? stderr : new ByteArrayOutputStreamExt();
		}
		
		public IByteSequence getStdOutBytes() {
			return stdout;
		}
		
		public IByteSequence getStdErrBytes() {
			return stderr;
		}
		
	}
	
	/* ----------------- Await termination ----------------- */
	
	public ExternalProcessResult awaitTerminationAndResult(boolean destroyOnError) 
			throws InterruptedException, OperationCancellation, IOException {
		try {
			return awaitTerminationAndResult(NO_TIMEOUT, destroyOnError);
		} catch (TimeoutException e) {
			throw assertFail(); // Cannot happen
		}
	}
	
	/** 
	 * Awaits for successful process termination, as well as successful termination of reader threads,
	 * throws an exception otherwise (and destroys the process).
	 * @param timeoutMs the timeout in milliseconds to wait for (or -1 for no TIMEOUT).
	 * @param destroyOnError if the process should be destroyed should any exception occur
	 * @return the process output result in an {@link ExternalProcessResult}. 
	 * @throws InterruptedException if thread interrupted while waiting.
	 * @throws TimeoutException if timeout reached.
	 * @throws OperationCancellation if reader thread cancellation has occured.
	 * @throws IOException if an IO error occured in the reader threads.
	 */
	public ExternalProcessResult awaitTerminationAndResult(int timeoutMs, boolean destroyOnError) 
			throws InterruptedException, TimeoutException, OperationCancellation, IOException {
		awaitTermination(timeoutMs, destroyOnError);
		return new ExternalProcessResult(process.exitValue(), getStdOutBytes3(), getStdErrBytes3());
	}
	
	public ExternalProcessResult awaitTerminationAndResult_ce(boolean destroyOnError) 
			throws CommonException, OperationCancellation 
	{
		return awaitTerminationAndResult_ce(NO_TIMEOUT, destroyOnError);
	}
	
	public ExternalProcessResult awaitTerminationAndResult_ce(int timeout, boolean destroyOnError) 
			throws CommonException, OperationCancellation 
	{
		try {
			return awaitTerminationAndResult(timeout, destroyOnError);
		} catch (IOException e) {
			throw new CommonException(ProcessHelperMessages.ExternalProcess_ErrorStreamReaderIOException, e);
		} catch (TimeoutException te) {
			// at this point a TimeoutException can be one of two things, an actual timeout, 
			// or an explicit cancellation. In both case we throw OperationCancellation
			
			throw new OperationCancellation();
		} catch (InterruptedException e) {
			// InterrruptedException also threated as OperationCancellation
			throw new OperationCancellation();
		}
	}
	
}