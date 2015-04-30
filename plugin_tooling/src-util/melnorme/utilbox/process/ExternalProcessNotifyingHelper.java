/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
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
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.misc.ILogHandler;
import melnorme.utilbox.misc.ListenerListHelper;

/**
 * Extends {@link ExternalProcessHelper} to allow optional listeners to be notified
 * of output read from the external process, as well as process termination events.
 */
public class ExternalProcessNotifyingHelper extends ExternalProcessHelper {
	
	protected final ILogHandler logHandler;
	
	public ExternalProcessNotifyingHelper(Process process, boolean readStdErr, boolean startReaders,
			ICancelMonitor cancelMonitor, ILogHandler logHandler) {
		super(process, readStdErr, startReaders, cancelMonitor);
		this.logHandler = assertNotNull(logHandler);
	}
	
	public static interface IProcessOutputListener {
		
		void notifyStdOutListeners(byte[] buffer, int offset, int readCount);
		
		void notifyStdErrListeners(byte[] buffer, int offset, int readCount);
		
		/** Notifies that the underlying process has terminated, and all reader threads have finished processing. */
		void notifyProcessTerminatedAndRead(int exitCode);
		
	}
	
	protected final ListenerListHelper<IProcessOutputListener> outputListeners = new ListenerListHelper<>();
	
	public ListenerListHelper<IProcessOutputListener> getOutputListenersHelper() {
		return outputListeners;
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
		for (IProcessOutputListener pol : outputListeners.getListeners()) {
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
				fullTerminationLatch.await();
				break;
			} catch (InterruptedException e) {
				// retry await
			}
		}
		notifyProcessTerminatedAndRead(process.exitValue());
	}
	
	
	protected void notifyProcessTerminatedAndRead(int exitCode) {
		for (IProcessOutputListener pol : outputListeners.getListeners()) {
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
	
}