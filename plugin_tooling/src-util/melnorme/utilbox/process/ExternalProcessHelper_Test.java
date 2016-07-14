/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
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
import java.io.OutputStream;

import org.junit.Test;

import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.tests.CommonTest;

public class ExternalProcessHelper_Test extends CommonTest {
	
	public static class EndlessInputStream extends InputStream {
		
		public volatile boolean endStream = false;
		
		@Override
		public int read() throws IOException {
			if(endStream) {
				return -1;
			}
			return 0;
		}
	}
	
	public static class MockProcess extends Process {

		protected final CancelMonitor terminationMonitor;
		
		protected final EndlessInputStream stdoutStream = new EndlessInputStream();
		protected final EndlessInputStream stderrStream = new EndlessInputStream();

		public MockProcess(CancelMonitor cancelMonitor) {
			this.terminationMonitor = assertNotNull(cancelMonitor);
		}

		@Override
		public OutputStream getOutputStream() {
			throw assertFail();
		}
		
		@Override
		public InputStream getInputStream() {
			return stdoutStream;
		}
		
		@Override
		public InputStream getErrorStream() {
			return stderrStream;
		}
		
		@Override
		public int waitFor() throws InterruptedException {
			terminationMonitor.getCancelLatch().await();
			return exitValue();
		}
		
		@Override
		public int exitValue() {
			return 0;
		}
		
		@Override
		public void destroy() {
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() {
		
		CancelMonitor cancelMonitor = new CancelMonitor();
		Process process = new MockProcess(cancelMonitor);
		ExternalProcessHelper eph = new ExternalProcessHelper(process, true, false, cancelMonitor) {
			@Override
			protected Runnable createMainReaderTask() {
				mainReader = new EndlessReadTask(process.getInputStream());
				return mainReader.runnableFuture;
			}
			
			@Override
			protected Runnable createStdErrReaderTask() {
				stderrReader = new EndlessReadTask(process.getErrorStream());
				return stderrReader.runnableFuture;
			}
			
			class EndlessReadTask extends ReadAllBytesTask {
				
				public EndlessReadTask(InputStream is) {
					super(is);
				}
				
				@Override
				protected void notifyReadChunk(byte[] buffer, int offset, int readCount) {
					byteArray.reset();
				}
			}
			
		};
		
		eph.startReaderThreads();
		
		// Test cancellation
		cancelMonitor.cancel(); 
		
		try {
			eph.awaitTerminationAndResult();
		} catch(IOException e) {
			assertFail();
		} catch(InterruptedException e) {
			// continue
		}
		
		try {
			// ensure threads terminate
			eph.mainReaderThread.join();
			eph.stderrReaderThread.join();
		} catch(InterruptedException e) {
			assertFail();
		}
	}
	
}