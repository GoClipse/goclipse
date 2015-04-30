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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.NullCancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.misc.ExceptionTrackingRunnable;
import melnorme.utilbox.misc.IByteSequence;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;

/**
 * Helper for running external processes.
 * Reads all stdout and stderr output into a byte array (using worker threads)
 * 
 * @see AbstractExternalProcessHelper
 */
public class ExternalProcessHelper extends AbstractExternalProcessHelper {
	
	protected final ICancelMonitor cancelMonitor;
	protected ReadAllBytesTask mainReader;
	protected ReadAllBytesTask stderrReader;
	
	public ExternalProcessHelper(ProcessBuilder pb) throws IOException {
		this(pb.start(), pb.redirectErrorStream() == false, true);
	}
	
	public ExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders) {
		this(process, readStdErr, startReaders, null);
	}
	
	public ExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders, 
			ICancelMonitor cancelMonitor) {
		super(process, readStdErr, startReaders);
		this.cancelMonitor = cancelMonitor == null ? new NullCancelMonitor() : cancelMonitor;
	}
	
	@Override
	protected boolean isCanceled() {
		return cancelMonitor.isCanceled();
	}
	
	@Override
	protected ReadAllBytesTask createMainReaderTask() {
		return mainReader = new ReadAllBytesTask(process.getInputStream());
	}
	
	@Override
	protected ReadAllBytesTask createStdErrReaderTask() {
		return stderrReader = new ReadAllBytesTask(process.getErrorStream());
	}
	
	protected static class ReadAllBytesTask extends ExceptionTrackingRunnable<ByteArrayOutputStreamExt, IOException> {
		
		protected final InputStream is;
		protected final ByteArrayOutputStreamExt byteArray = new ByteArrayOutputStreamExt(32);
		
		public ReadAllBytesTask(InputStream is) {
			this.is = is;
		}
		
		@Override
		public ByteArrayOutputStreamExt doRun() throws IOException {
			// BM: Hum, should we treat an IOException not as an error, but just like an EOF?
			try {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				
				int read;
				while((read = is.read(buffer)) != StreamUtil.EOF) {
					byteArray.write(buffer, 0, read);
					notifyReadChunk(buffer, 0, read);
				}
				return byteArray;
			} finally {
				is.close();
			}
		}
		
		@SuppressWarnings("unused")
		protected void notifyReadChunk(byte[] buffer, int offset, int readCount) {
			// Default implementation: do nothing
		}
		
	}
	
	/* ----------------- ----------------- */
	
	protected CommonException createCommonException(String message, Throwable cause) {
		return new CommonException(message, cause);
	}
	
	public static Process startProcess(ProcessBuilder pb) throws CommonException {
		try {
			return pb.start();
		} catch (IOException ie) {
			throw new CommonException(ProcessHelperMessages.ExternalProcess_CouldNotStart, ie);
		}
	}
	
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
	
	/* ----------------- result helpers ----------------- */
	
	protected ByteArrayOutputStreamExt getStdOutBytes() {
		assertTrue(isFullyTerminated());
		return mainReader.byteArray;
	}
	
	protected ByteArrayOutputStreamExt getStdErrBytes() {
		assertTrue(isFullyTerminated());
		assertTrue(readStdErr);
		return stderrReader.byteArray;
	}
	
	public class ExternalProcessResult {
		
		public final int exitValue;
		public final ByteArrayOutputStreamExt stdout;
		public final ByteArrayOutputStreamExt stderr;
		
		public ExternalProcessResult(int exitValue, ByteArrayOutputStreamExt stdout, ByteArrayOutputStreamExt stderr) {
			this.exitValue = exitValue;
			this.stdout = stdout;
			this.stderr = stderr;
		}
		
		public IByteSequence getStdOutBytes() {
			return stdout;
		}
		
		public IByteSequence getStdErrBytes() {
			return stderr;
		}
		
	}
	
	public void tryStrictAwaitTermination(int timeoutMs) throws InterruptedException, TimeoutException, IOException {
		awaitTermination(timeoutMs);
		mainReader.getResult();
		stderrReader.getResult();
	}
	
	/** 
	 * Awaits for successful process termination, as well as successful termination of reader threads,
	 * throws an exception otherwise (and destroys the process).
	 * @return the process result encapsulated in data class {@link ExternalProcessResult}. 
	 * @throws InterruptedException if interrupted
	 * @throws TimeoutException if timeout occurs, or cancel requested.
	 * @throws IOException if an IO error occured in the reader threads.
	 */
	public ExternalProcessResult strictAwaitTermination(int timeoutMs) 
			throws InterruptedException, TimeoutException, IOException {
		try {
			tryStrictAwaitTermination(timeoutMs);
		} catch (Exception e) {
			process.destroy();
			throw e;
		}
		return new ExternalProcessResult(process.exitValue(), getStdOutBytes(), getStdErrBytes());
	}
	
	public ExternalProcessResult strictAwaitTermination() throws InterruptedException, TimeoutException, IOException {
		return strictAwaitTermination(NO_TIMEOUT);
	}
	
	public ExternalProcessResult strictAwaitTermination_(int timeout) 
			throws CommonException, OperationCancellation 
	{
		try {
			return strictAwaitTermination(timeout);
		} catch (InterruptedException e) {
			throw createCommonException(ProcessHelperMessages.ExternalProcess_InterruptedAwaitingTermination, e);
		} catch (IOException e) {
			throw createCommonException(ProcessHelperMessages.ExternalProcess_ErrorStreamReaderIOException, e);
		} catch (TimeoutException te) {
			// at this point a TimeoutException can be one of two things, an actual timeout, or a cancellation.
			
			if(isCanceled()) {
				throw new OperationCancellation();
			} else {
				throw createCommonException(ProcessHelperMessages.ExternalProcess_ProcessTimeout, te);
			}
		}
	}
	
	public ExternalProcessResult strictAwaitTermination_() 
			throws CommonException, OperationCancellation {
		return strictAwaitTermination_(false);
	}
	
	public ExternalProcessResult strictAwaitTermination_(boolean throwOnNonZeroStatus) 
			throws CommonException, OperationCancellation 
	{
		ExternalProcessResult processResult = strictAwaitTermination_(NO_TIMEOUT);
		
		if(throwOnNonZeroStatus && processResult.exitValue != 0) {
			throw new CommonException("Process completed with non-zero exit value (" + processResult.exitValue + ")");
		}
		return processResult;
	}
	
}