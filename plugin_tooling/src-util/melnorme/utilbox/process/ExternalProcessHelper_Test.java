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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.tests.CommonTest;

public class ExternalProcessHelper_Test extends CommonTest {
	
	public static class EndlessInputStream extends InputStream {
		
		protected volatile boolean closed = false;
		
		@Override
		public void close() {
			closed = true;
		}
		
		@Override
		public int read() {
			if(closed) {
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
			if(!terminationMonitor.isCanceled()) {
				throw new IllegalThreadStateException();
			}
			return 0;
		}
		
		@Override
		public void destroy() {
			stdoutStream.close();
			stderrStream.close();
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
			return mainReader = new EndlessReadTask(process.getInputStream(), cancelMonitor);
		}
		
		@Override
		protected Runnable createStdErrReaderTask() {
			return stderrReader = new EndlessReadTask(process.getErrorStream(), cancelMonitor);
		}
		
		public class EndlessReadTask extends ReadAllBytesTask {
			
			public EndlessReadTask(InputStream is, ICancelMonitor cancelMonitor) {
				super(is, cancelMonitor);
			}
			
			@Override
			protected void notifyReadChunk2(byte[] buffer, int offset, int readCount) {
				assertTrue(byteArray.size() == 0);
			}
		}
	}
	
	@Test
	public void test() throws Exception { test$(); }
	public void test$() {
		for(int i = 0; i < 10; i++) {
			do_tests();
		}
	}
	
	protected void do_tests() {
		test_process_death();
		test_process_streams_close();
		test_cancellation();
		test_cancellation_vs_interrupt();
	}
	
	protected void check_awaitProcessTermination(TestsExternalProcessHelper eph, 
			Class<? extends Throwable> expectedThrows, boolean awaitProcessTermination) {
		
		verifyThrows(() -> eph.awaitTerminationAndResult(false), expectedThrows);
		
		checkReaderThreadsTerminate(eph, awaitProcessTermination);
	}
	
	protected void checkReaderThreadsTerminate(TestsExternalProcessHelper eph, boolean awaitProcessTermination) {
		// ensure threads terminate
		checkThreadJoin(eph.stderrReaderThread);
		try {
			eph.readerThreadsTerminationLatch.await();
		} catch(InterruptedException e) {
			assertFail();
		}
		if(awaitProcessTermination) {
			checkThreadJoin(eph.mainReaderThread);
		}
	}
	
	protected void test_process_death() {
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, true, null);
		
		// Test process death
		eph.mockProcess.destroy();
		
		check_awaitProcessTermination(eph, null, true);
	}
	
	protected void test_process_streams_close() {
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, true, null);
		
		// Test process death
		eph.mockProcess.stderrStream.close();
		eph.mockProcess.stdoutStream.close();
		
		checkReaderThreadsTerminate(eph, false);
	}
	
	protected void test_cancellation() {
		CancelMonitor cancelMonitor = new CancelMonitor();
		TestsExternalProcessHelper eph = new TestsExternalProcessHelper(true, false, cancelMonitor);
		
		cancelMonitor.cancel();
		eph.startReaderThreads();
		
		check_awaitProcessTermination(eph, OperationCancellation.class, false);
		assertTrue(eph.isCanceled());
		// We allow cancellation of the process reader, whilst keeping the process alive
		assertTrue(eph.process.isAlive() == true);
		
		checkReaderThreadsTerminate(eph, false);
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
		assertTrue(eph.stderrReaderThread.isAlive());
		assertTrue(eph.mainReaderThread.isAlive());
		
		eph.process.destroy();
		check_awaitProcessTermination(eph, null, true);
	}
	
	/* -----------------  ----------------- */
	
	protected static void checkThreadJoin(Thread thread) {
		try {
			thread.join();
		} catch(InterruptedException e) {
			assertFail();
		}
	}
	
}