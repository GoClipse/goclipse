/*******************************************************************************
 * Copyright (c) 2014, 2014 IBM Corporation and others.
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
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.utilbox.core.fntypes.ICallable;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class RunExternalProcessTask implements ICallable<ExternalProcessNotifyingHelper, CoreException> {
	
	protected final ProcessBuilder pb;
	protected final IProject project;
	protected final IProgressMonitor cancelMonitor;
	protected final ListenerListHelper<? extends IExternalProcessListener> listenersHelper;
	
	public RunExternalProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor,
			ListenerListHelper<? extends IExternalProcessListener> listenersHelper) {
		this.pb = assertNotNull(pb);
		this.project = project; // can be null
		this.cancelMonitor = assertNotNull(cancelMonitor);
		this.listenersHelper = assertNotNull(listenersHelper);
	}
	
	@Override
	public ExternalProcessNotifyingHelper call() throws CoreException {
		return startProcessAndAwait();
	}
	
	public void notifyProcessStarted(ExternalProcessNotifyingHelper processHelper) {
		for (IExternalProcessListener processListener : listenersHelper.getListeners()) {
			processListener.handleProcessStarted(pb, project, processHelper);
		}
	}
	
	public void notifyProcessFailedToStart(IOException e) {
		for (IExternalProcessListener processListener : listenersHelper.getListeners()) {
			processListener.handleProcessStartFailure(pb, project, e);
		}
	}
	
	public ExternalProcessEclipseHelper startProcessAndAwait() throws CoreException {
		ExternalProcessEclipseHelper processHelper;
		try {
			processHelper = new ExternalProcessEclipseHelper(pb, false, cancelMonitor);
		} catch (IOException e) {
			notifyProcessFailedToStart(e);
			throw createProcessException(LangCoreMessages.ExternalProcess_CouldNotStart,  e);
		}
		
		notifyProcessStarted(processHelper);
		processHelper.startReaderThreads();
		
		try {
			processHelper.awaitTerminationStrict_destroyOnException();
		} catch (InterruptedException e) {
			throw createProcessException(LangCoreMessages.ExternalProcess_InterruptedAwaitingTermination, null);
		} catch (TimeoutException e) {
			assertTrue(cancelMonitor.isCanceled());
			throw createProcessException(LangCoreMessages.ExternalProcess_TaskCancelledProcessTerminated, null);
		}
		
		return processHelper;
	}
	
	protected CoreException createProcessException(String message, IOException e) {
		return new CoreException(LangCore.createErrorStatus(message, e));
	}
	
	public static class ExternalProcessEclipseHelper extends ExternalProcessNotifyingHelper {
		
		protected final IProgressMonitor monitor;
		
		public ExternalProcessEclipseHelper(ProcessBuilder pb, boolean startReaders, IProgressMonitor monitor) 
				throws IOException {
			super(pb.start(), true, startReaders);
			this.monitor = assertNotNull(monitor);
		}
		
		@Override
		protected boolean isCanceled() {
			return monitor.isCanceled();
		}
		
		@Override
		protected void handleListenerException(RuntimeException e) {
			LangCore.logError("Internal error notifying listener", e);
		}
		
	}
	
}