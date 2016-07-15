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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.tests.CommonTest;

public class ExternalProcessHelper_Test extends CommonTest {
	
	public static class MockProcess extends Process {

		protected final CancelMonitor terminationMonitor;
		
		protected final EndlessInputStream stdoutStream = new EndlessInputStream();
		protected final EndlessInputStream stderrStream = new EndlessInputStream();

		public MockProcess(CancelMonitor cancelMonitor) {
			this.terminationMonitor = assertNotNull(cancelMonitor);
		}
		
		public class EndlessInputStream extends InputStream {
			
			@Override
			public int read() throws IOException {
				if(terminationMonitor.isCanceled()) {
					return -1;
				}
				return 0;
			}
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
			if(!terminationMonitor.isCanceled()) {
				throw new IllegalThreadStateException();
			}
			return 0;
		}
		
		@Override
		public void destroy() {
			terminationMonitor.cancel();
		}
	}
	
	public static class TestsExternalProcessHelper extends ExternalProcessHelper {
		
		protected final MockProcess mockProcess;
		
		public TestsExternalProcessHelper(boolean readStdErr, boolean startReaders, ICancelMonitor cancelMonitor) {
			this(new MockProcess(new CancelMonitor()), readStdErr, startReaders, cancelMonitor);
		}
		
		public TestsExternalProcessHelper(MockProcess mockProcess, boolean readStdErr,
				boolean startReaders, ICancelMonitor cancelMonitor) {
			super(mockProcess, readStdErr, startReaders, cancelMonitor);
			this.mockProcess = mockProcess;
		}
		
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
		
		public class EndlessReadTask extends ReadAllBytesTask {
			
			public EndlessReadTask(InputStream is) {
				super(is);
			}
			
			@Override
			protected void notifyReadChunk(byte[] buffer, int offset, int readCount) {
				byteArray.reset();
			}
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() {
		for(int i = 0; i < 10; i++) {
			test_process_death();
			test_cancellation();
			test_cancellation_vs_interrupt();
		}
	}
	
	protected void check_awaitTermination(TestsExternalProcessHelper eph, boolean destroyOnError)
			throws InterruptedException, OperationCancellation, IOException {
		eph.awaitTerminationAndResult(destroyOnError);
		
		// ensure threads terminate
		eph.mainReaderThread.join();
		eph.stderrReaderThread.join();
	}
	
	protected void test_process_death() {
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, true, null);
		
		// Test process death
		eph.mockProcess.destroy();
		
		verifyThrows(() -> check_awaitTermination(eph, false), null);
	}
	
	protected void test_cancellation() {
		CancelMonitor cancelMonitor = new CancelMonitor();
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, false, cancelMonitor);
		
		cancelMonitor.cancel();
		eph.startReaderThreads();
		
		verifyThrows(() -> check_awaitTermination(eph, false), OperationCancellation.class);
		assertTrue(eph.isCanceled());
		// We allow cancellation of the process reader, whilst keeping the process alive
		assertTrue(eph.process.isAlive() == true);
	}
	
	public void test_cancellation_vs_interrupt() {
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, true, null);
		
		Runnable runnable = () -> {
			try {
				eph.awaitReadersTermination(-1);
				assertFail();
			} catch(TimeoutException | OperationCancellation e) {
				assertFail();
			} catch(InterruptedException e) {
				return;
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.start();
		thread.interrupt();
		
		// check that EPH is not compromised because of interrupt
		assertTrue(eph.cancelMonitor.isCanceled() == false);
		assertTrue(eph.process.isAlive());
		
		eph.process.destroy();
		verifyThrows(() -> check_awaitTermination(eph, false), null);
	}
	
}