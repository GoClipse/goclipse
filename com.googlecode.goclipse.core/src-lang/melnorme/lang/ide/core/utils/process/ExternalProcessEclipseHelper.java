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
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.utilbox.misc.ByteArrayOutputStreamExt;
import melnorme.utilbox.misc.StreamUtil;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * An extension to {@link ExternalProcessNotifyingHelper} that is customized to run in Eclipse. In particular:
 * Allows using an {@link IProgressMonitor} as a cancel monitor.
 * Converts IOExceptions for most operations into CoreException
 * Log errors in Eclipse log.
 * 
 * TODO: perhaps this should not be a subclass, but use composition instead
 */
public class ExternalProcessEclipseHelper extends ExternalProcessNotifyingHelper {
	
	protected final IProgressMonitor monitor;
	
	public ExternalProcessEclipseHelper(ProcessBuilder pb, boolean startReaders, IProgressMonitor monitor)
			throws CoreException {
		super(startProcess(pb), true, startReaders);
		this.monitor = assertNotNull(monitor);
	}
	
	protected static Process startProcess(ProcessBuilder pb) throws CoreException {
		try {
			return pb.start();
		} catch (IOException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_CouldNotStart, e);
		}
	}
	
	@Override
	protected boolean isCanceled() {
		return monitor.isCanceled();
	}
	
	@Override
	protected void handleListenerException(RuntimeException e) {
		LangCore.logError("Internal error notifying listener", e);
	}
	
	public ByteArrayOutputStreamExt getStdOutBytes_CoreException() throws CoreException {
		try {
			return super.getStdOutBytes();
		} catch (IOException e) {
			throw LangCore.createCoreException("IO Error reading process stdout stream.", e);
		}
	}
	
	public ByteArrayOutputStreamExt getStdErrBytes_CoreException() throws CoreException {
		try {
			return super.getStdErrBytes();
		} catch (IOException e) {
			throw LangCore.createCoreException("IO Error reading process stderr stream.", e);
		}
	}
	
	public void awaitTermination_CoreException() throws CoreException {
		awaitTermination_CoreException(NO_TIMEOUT);
	}
	
	public void awaitTermination_CoreException(int timeout) throws CoreException {
		try {
			awaitTerminationStrict_destroyOnException(timeout);
		} catch (InterruptedException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_InterruptedAwaitingTermination, e);
		} catch (TimeoutException e) {
			if(monitor.isCanceled()) {
				throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_TaskCancelled, null);
			} else {
				throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_ProcessTimeout, null);
			}
		}
	}
	
	public void writeInput(String input) throws CoreException {
		if(input == null)
			return;
		
		OutputStream processInputStream = getProcess().getOutputStream();
		try {
			StreamUtil.writeStringToStream(input, processInputStream, StringUtil.UTF8);
		} catch (IOException e) {
			throw LangCore.createCoreException(LangCoreMessages.ExternalProcess_ErrorWritingInput , e);
		}
	}
	
}