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

import java.io.IOException;
import java.util.List;

import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A task that runs an external process and notifies listeners of process lifecycle events.
 */
public class RunExternalProcessTask<LISTENER extends IExternalProcessListener> implements IRunProcessTask {
	
	protected final ProcessBuilder pb;
	protected final IProject project;
	protected final IProgressMonitor cancelMonitor;
	
	protected final ListenerListHelper<LISTENER> listenersList;
	
	public RunExternalProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor,
			ListenerListHelper<LISTENER> listenersList) {
		this.pb = assertNotNull(pb);
		this.project = project; // can be null
		this.cancelMonitor = assertNotNull(cancelMonitor);
		this.listenersList = assertNotNull(listenersList);
	}
	
	protected void notifyProcessStarted(ExternalProcessNotifyingHelper processHelper) {
		for(IExternalProcessListener processListener : getListeners()) {
			processListener.handleProcessStarted(pb, project, processHelper);
		}
	}
	
	protected void notifyProcessFailedToStart(IOException e) {
		for(IExternalProcessListener processListener : getListeners()) {
			processListener.handleProcessStartFailure(pb, project, e);
		}
	}
	
	protected List<LISTENER> getListeners() {
		return listenersList.getListeners();
	}
	
	@Override
	public ExternalProcessResult call() throws CoreException {
		return startProcessAndAwait();
	}
	
	public ExternalProcessResult startProcessAndAwait() throws CoreException {
		return startProcess().strictAwaitTermination();
	}
	
	public EclipseExternalProcessHelper startProcess() throws CoreException {
		EclipseExternalProcessHelper eclipseProcessHelper;
		try {
			eclipseProcessHelper = new EclipseExternalProcessHelper(pb, false, cancelMonitor);
		} catch (CoreException ce) {
			IOException ioe = (IOException) ce.getStatus().getException();
			notifyProcessFailedToStart(ioe);
			throw ce;
		}
		
		ExternalProcessNotifyingHelper processHelper = eclipseProcessHelper.getNotifyingProcessHelper();
		notifyProcessStarted(processHelper);
		processHelper.startReaderThreads();
		
		return eclipseProcessHelper;
	}
	
}