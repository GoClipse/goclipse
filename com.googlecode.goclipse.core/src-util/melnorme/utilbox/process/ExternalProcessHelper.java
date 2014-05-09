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

import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.misc.ExceptionTrackingRunnable;
import melnorme.utilbox.misc.IByteSequence;
import melnorme.utilbox.misc.StreamUtil;

/**
 * Helper for running external processes.
 * Reads all stdout and stderr output into a byte array (using worker threads)
 * 
 * @see AbstractExternalProcessHelper
 */
public class ExternalProcessHelper extends AbstractExternalProcessHelper {
	
	protected ReadAllBytesTask mainReader;
	protected ReadAllBytesTask stderrReader;
	
	public ExternalProcessHelper(ProcessBuilder pb) throws IOException {
		super(pb);
	}
	
	public ExternalProcessHelper(Process process, boolean readStdErr, boolean startReaders) {
		super(process, readStdErr, startReaders);
	}
	
	@Override
	protected ReadAllBytesTask createMainReaderTask() {
		return mainReader = new ReadAllBytesTask(process.getInputStream());
	}
	
	@Override
	protected ReadAllBytesTask createStdErrReaderTask() {
		return stderrReader = new ReadAllBytesTask(process.getErrorStream());
	}
	
	@Override
	protected boolean isCanceled() {
		return false;
	}
	
	protected static class ReadAllBytesTask extends ExceptionTrackingRunnable<IByteSequence, IOException> {
		
		protected final InputStream is;
		
		public ReadAllBytesTask(InputStream is) {
			this.is = is;
		}
		
		@Override
		public IByteSequence doRun() throws IOException {
			try {
				final int BUFFER_SIZE = 1024;
				byte[] buffer = new byte[BUFFER_SIZE];
				ByteArrayOutputStreamExt byteArray = new ByteArrayOutputStreamExt(32);
				
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
	
	public IByteSequence getStdOutBytes() throws IOException {
		assertTrue(isFullyTerminated());
		return mainReader.getResult();
	}
	
	public IByteSequence getStdErrBytes() throws IOException {
		assertTrue(isFullyTerminated());
		assertTrue(readStdErr);
		return stderrReader.getResult();
	}
	
}