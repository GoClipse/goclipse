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
package melnorme.lang.ide.core.utils.process;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * An adapter to {@link ExternalProcessNotifyingHelper} that is customized to run in Eclipse. In particular:
 * Allows using an {@link IProgressMonitor} as a cancel monitor.
 * Converts IOExceptions and other exceptions into CoreException.
 * Log errors in Eclipse log.
 */
public class EclipseExternalProcessHelper {
	
	public static Process startProcess(ProcessBuilder pb) throws CoreException {
		try {
			return pb.start();
		} catch (IOException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_CouldNotStart, e);
		}
	}
	
	protected final ExternalProcessNotifyingHelper ph;
	protected final IProgressMonitor monitor;
	
	public EclipseExternalProcessHelper(ProcessBuilder pb, boolean startReaders, IProgressMonitor monitor)
			throws CoreException {
		this(startProcess(pb), startReaders, monitor);
	}
	
	public EclipseExternalProcessHelper(Process process, boolean startReaders, final IProgressMonitor monitor)
			throws CoreException {
		this.monitor = assertNotNull(monitor);
		this.ph = new ExternalProcessNotifyingHelper(process, true, startReaders) {
			@Override
			protected boolean isCanceled() {
				return monitor.isCanceled();
			}
			
			@Override
			protected void handleListenerException(RuntimeException e) {
				LangCore.logError("Internal error notifying listener", e);
			}
		};
	}
	
	public ExternalProcessNotifyingHelper getNotifyingProcessHelper() {
		return ph;
	}
	
	public Process getProcess() {
		return ph.getProcess();
	}
	
	public ExternalProcessResult strictAwaitTermination() throws CoreException {
		return strictAwaitTermination(ExternalProcessHelper.NO_TIMEOUT);
	}
	
	public ExternalProcessResult strictAwaitTermination(int timeout) throws CoreException {
		try {
			return ph.strictAwaitTermination(timeout);
		} catch (InterruptedException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_InterruptedAwaitingTermination, e);
		} catch (IOException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_ErrorStreamReaderIOException, e);
		} catch (TimeoutException te) {
			String message = monitor.isCanceled() ? 
					LangCoreMessages.ExternalProcess_TaskCancelled : 
					LangCoreMessages.ExternalProcess_ProcessTimeout;
			throw new CoreException(LangCore.createStatus(IStatus.INFO, message, te));
		}
	}
	
	public void writeInput(String input) throws CoreException {
		try {
			ph.writeInput(input, StringUtil.UTF8);
		} catch (IOException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_ErrorWritingInput , e);
		}
	}
	
}