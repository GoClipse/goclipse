/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.misc.ILogHandler;

/**
 * Extends {@link ExternalProcessHelper} to allow optional listeners to be notified
 * of output read from the external process, as well as process termination events.
 */
public class ExternalProcessNotifyingHelper extends ExternalProcessHelper {
	
	public static enum FAwaitListeners { YES, NO ; public boolean isTrue() { return this == YES; } }
	public static enum FStartReaders { YES, NO ; public boolean isTrue() { return this == YES; } }
	
	protected final ILogHandler logHandler;
	protected final Indexable<IProcessOutputListener> listeners;
	protected final FAwaitListeners awaitListeners;
	protected final CountDownLatch listenersTerminationLatch = new CountDownLatch(1);
	
	public ExternalProcessNotifyingHelper(Process process, boolean readStdErr, 
			ICancelMonitor cancelMonitor, Indexable<IProcessOutputListener> listeners, ILogHandler logHandler) {
		this(process, readStdErr, FStartReaders.YES, FAwaitListeners.YES, cancelMonitor, listeners, logHandler);
	}
	
	protected ExternalProcessNotifyingHelper(Process process, boolean readStdErr, 
			FStartReaders startReaders, FAwaitListeners awaitListeners, 
			ICancelMonitor cancelMonitor, Indexable<IProcessOutputListener> listeners, ILogHandler logHandler) {
		super(process, readStdErr, false, cancelMonitor);
		
		this.awaitListeners = assertNotNull(awaitListeners);
		this.listeners = assertNotNull(listeners);
		this.logHandler = assertNotNull(logHandler);
		
		if(startReaders.isTrue()) {
			startReaderThreads();
		}
	}
	
	public static interface IProcessOutputListener {
		
		void notifyStdOutListeners(byte[] buffer, int offset, int readCount);
		
		void notifyStdErrListeners(byte[] buffer, int offset, int readCount);
		
		/** Notifies that the underlying process has terminated, and all reader threads have finished processing. */
		void notifyProcessTerminatedAndRead(int exitCode);
		
	}
	
	
	@Override
	protected ReadAllBytesTask createMainReaderTask() {
		return mainReader = new ReadAllBytesTask(process.getInputStream()) {
			@Override
			protected void notifyReadChunk(byte[] buffer, int offset, int readCount) {
				notifyDataRead(buffer, offset, readCount, true);
			}
		};
	}
	
	@Override
	protected ReadAllBytesTask createStdErrReaderTask() {
		return stderrReader = new ReadAllBytesTask(process.getErrorStream()) {
			@Override
			protected void notifyReadChunk(byte[] buffer, int offset, int readCount) {
				notifyDataRead(buffer, offset, readCount, false);
			}
		};
	}
	
	protected void notifyDataRead(byte[] buffer, int offset, int readCount, boolean stdOut) {
		for (IProcessOutputListener pol : listeners) {
			try {
				if(stdOut) 
					pol.notifyStdOutListeners(buffer, offset, readCount);
				else {
					pol.notifyStdErrListeners(buffer, offset, readCount);
				}
			} catch (RuntimeException e) {
				handleListenerException("Listener internal error at notifyDataRead.", e);
			}
		}
	}
	
	@Override
	public void mainReaderThread_Terminated() {
		while(true) {
			try {
				readersTerminationLatch.await();
				break;
			} catch (InterruptedException e) {
				// retry await
			}
		}
		try {
			// Notify listeners
			mainReaderThread_notifyProcessTerminatedAndRead(process.exitValue());
		} finally {
			listenersTerminationLatch.countDown();
		}
	}
	
	protected void mainReaderThread_notifyProcessTerminatedAndRead(int exitCode) {
		for (IProcessOutputListener pol : listeners) {
			try {
				pol.notifyProcessTerminatedAndRead(exitCode);
			} catch (RuntimeException e) {
				handleListenerException("Listener internal error at notifyProcessTerminatedAndRead", e);
			}
		}
	}
	
	protected void handleListenerException(String message, RuntimeException e) {
		logHandler.logStatus(new StatusException(StatusLevel.ERROR, message, e));
	}
	
	@Override
	protected boolean doAwaitTermination(int cancelPollPeriodMs) throws InterruptedException {
		if(awaitListeners.isTrue()) {
			return listenersTerminationLatch.await(cancelPollPeriodMs, TimeUnit.MILLISECONDS);
		} else {
			return super.doAwaitTermination(cancelPollPeriodMs);
		}
	}
	
}