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
import melnorme.utilbox.concurrency.ICancelMonitor.CancelMonitorWithLatch;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.fntypes.Result;
import melnorme.utilbox.tests.CommonTest;

public class ExternalProcessHelper_Test extends CommonTest {
	
	public static class EndlessInputStream extends InputStream {
		
		protected final ICancelMonitor processTerminationMonitor;
		
		protected volatile boolean closed = false;
		protected volatile int count = 0;
		
		public EndlessInputStream(ICancelMonitor processTerminationMonitor) {
			this.processTerminationMonitor = assertNotNull(processTerminationMonitor);
		}
		
		@Override
		public void close() {
			closed = true;
		}
		
		@Override
		public int read() {
			if(closed || processTerminationMonitor.isCancelled()) {
				return -1;
			}
			++count;
			if(count % 1000 == 0) {
				try {
					Thread.sleep(20);
				} catch(InterruptedException e) {
				}
			}
			return 0;
		}
	}

	public static class MockProcess extends Process {
		
		protected final CancelMonitorWithLatch processTerminationMonitor = new CancelMonitorWithLatch();
		protected final EndlessInputStream stdoutStream = new EndlessInputStream(processTerminationMonitor);
		protected final EndlessInputStream stderrStream = new EndlessInputStream(processTerminationMonitor);
		
		public MockProcess() {
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
			processTerminationMonitor.getCancelLatch().await();
			return exitValue();
		}
		
		@Override
		public int exitValue() {
			if(!processTerminationMonitor.isCancelled()) {
				throw new IllegalThreadStateException();
			}
			return 0;
		}
		
		@Override
		public void destroy() {
			stdoutStream.close();
			stderrStream.close();
			processTerminationMonitor.cancel();
		}
	}
	
	public static class EndlessReadTask extends ReaderTask<Void> {
		
		public EndlessReadTask(InputStream is, ICancelMonitor cancelMonitor) {
			super(is, cancelMonitor);
		}
		
		@Override
		protected Void doGetReturnValue() {
			return null;
		}
		
	}
	
	public static class TestsExternalProcessHelper 
		extends ExternalProcessHandler<EndlessReadTask, EndlessReadTask> {
		
		protected final MockProcess mockProcess;
		
		public TestsExternalProcessHelper(boolean readStdErr, boolean startReaders, ICancelMonitor cancelMonitor) {
			this(new MockProcess(), readStdErr, startReaders, cancelMonitor);
		}
		
		public TestsExternalProcessHelper(MockProcess mockProcess, boolean readStdErr,
				boolean startReaders, ICancelMonitor cancelMonitor) {
			super(mockProcess, readStdErr, startReaders, cancelMonitor);
			this.mockProcess = mockProcess;
		}
		
		@Override
		protected EndlessReadTask init_StdOutReaderTask() {
			return new EndlessReadTask(process.getInputStream(), cancelMonitor);
		}
		
		@Override
		protected EndlessReadTask init_StdErrReaderTask() {
			return new EndlessReadTask(process.getErrorStream(), cancelMonitor);
		}
		
		@Override
		protected void completeStderrResult(EndlessReadTask stderrReaderTask) {
			stderrReaderTask.completeWithResult(new Result<>(null));
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
		
		verifyThrows(() -> eph.awaitTermination(false), expectedThrows);
		
		checkReaderThreadsTerminate(eph, awaitProcessTermination);
	}
	
	protected void checkReaderThreadsTerminate(TestsExternalProcessHelper eph, boolean awaitProcessTermination) {
		// ensure threads terminate
		checkThreadJoin(eph.stderrReaderThread);
		try {
			eph.stdoutReaderTask.awaitTermination();
			eph.stderrReaderTask.awaitTermination();
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
		assertTrue(eph.stdoutReaderTask.isCancelled());
		assertTrue(eph.stderrReaderTask.isCancelled());
		
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
		assertTrue(eph.cancelMonitor.isCancelled() == false);
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