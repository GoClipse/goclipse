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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeoutException;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.NullCancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.concurrency.RunnableFuture2;
import melnorme.utilbox.concurrency.RunnableFuture2.ResultRunnableFuture;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
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
	protected Runnable createMainReaderTask() {
		mainReader = new ReadAllBytesTask(process.getInputStream());
		return stderrReader.runnableFuture;
	}
	
	@Override
	protected Runnable createStdErrReaderTask() {
		stderrReader = new ReadAllBytesTask(process.getErrorStream());
		return stderrReader.runnableFuture;
	}
	
	protected static class ReadAllBytesTask {
		
		protected final InputStream is;
		protected final ByteArrayOutputStreamExt byteArray = new ByteArrayOutputStreamExt(32);
		protected final ResultRunnableFuture<ByteArrayOutputStreamExt, IOException> runnableFuture;
		
		public ReadAllBytesTask(InputStream is) {
			this.is = is;
			this.runnableFuture = RunnableFuture2.toResultRunnableFuture(this::doRun);
		}
		
		public RunnableFuture2<Result<ByteArrayOutputStreamExt, IOException>> asRunnableFuture() {
			return runnableFuture;
		}
		
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
		} catch (IOException ioe) {
			String msg = ioe.getMessage();
			if(msg == null) {
				msg = ProcessHelperMessages.ExternalProcess_CouldNotStart;
			}
			throw new CommonException(msg, ioe);
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
		assertTrue(areReadersTerminated());
		return mainReader.byteArray;
	}
	
	protected ByteArrayOutputStreamExt getStdErrBytes2() {
		assertTrue(areReadersTerminated());
		if(readStdErr) {
			return stderrReader.byteArray;
		}
		return null;
	}
	
	public class ExternalProcessResult {
		
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
	
	public ExternalProcessResult awaitTerminationAndResult() 
			throws InterruptedException, IOException {
		try {
			return awaitTerminationAndResult(NO_TIMEOUT);
		} catch (TimeoutException e) {
			throw assertFail(); // Cannot happen
		}
	}
	
	/** 
	 * Awaits for successful process termination, as well as successful termination of reader threads,
	 * throws an exception otherwise (and destroys the process).
	 * @param timeoutMs the timeout in milliseconds to wait for (or -1 for no TIMEOUT).
	 * @return the process output result in an {@link ExternalProcessResult}. 
	 * @throws InterruptedException if thread interrupted, or if cancellation is polled.
	 * @throws TimeoutException if timeout reached.
	 * @throws IOException if an IO error occured in the reader threads.
	 */
	public ExternalProcessResult awaitTerminationAndResult(int timeoutMs) 
			throws InterruptedException, TimeoutException, IOException {
		try {
			awaitTermination(timeoutMs);
			
			// Check for IOExceptions (although I'm not sure this scenario is possible)
			mainReader.asRunnableFuture().getResult();
			if(stderrReader != null) {
				stderrReader.asRunnableFuture().getResult();
			}
		} catch (Exception e) {
			process.destroy();
			throw e;
		}
		return new ExternalProcessResult(process.exitValue(), getStdOutBytes(), getStdErrBytes2());
	}
	
	public ExternalProcessResult awaitTerminationAndResult_ce() throws CommonException, OperationCancellation {
		return awaitTerminationAndResult_ce(NO_TIMEOUT);
	}
	
	public ExternalProcessResult awaitTerminationAndResult_ce(int timeout) 
			throws CommonException, OperationCancellation 
	{
		try {
			return awaitTerminationAndResult(timeout);
		} catch (IOException e) {
			throw createCommonException(ProcessHelperMessages.ExternalProcess_ErrorStreamReaderIOException, e);
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