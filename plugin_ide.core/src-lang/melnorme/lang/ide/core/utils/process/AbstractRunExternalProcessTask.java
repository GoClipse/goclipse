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

import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * A task that runs an external process and notifies listeners of process lifecycle events.
 */
@Deprecated /* FIXME: need to refactor*/
public abstract class AbstractRunExternalProcessTask<LISTENER extends IExternalProcessListener> 
		implements IRunProcessTask {
	
	protected final ProcessBuilder pb;
	protected final IProject project;
	protected final IProgressMonitor cancelMonitor;
	
	public AbstractRunExternalProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor) {
		this.pb = assertNotNull(pb);
		this.project = project; // can be null
		this.cancelMonitor = assertNotNull(cancelMonitor);
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
	
	protected abstract List<? extends LISTENER> getListeners();
	
	@Override
	public ExternalProcessResult call() throws CommonException {
		return startProcessAndAwait();
	}
	
	public ExternalProcessResult startProcessAndAwait() throws CommonException {
		return startProcess().strictAwaitTermination_();
	}
	
	public ExternalProcessNotifyingHelper startProcess() throws CommonException {
		ExternalProcessNotifyingHelper processHelper;
		try {
			Process process = ExternalProcessHelper.startProcess(pb);
			processHelper = new EclipseProcessHelper(process, false, cancelMonitor); 
		} catch (CommonException ce) {
			IOException ioe = (IOException) ce.getCause();
			/* FIXME: refactor this */
			notifyProcessFailedToStart(ioe);
			throw ce;
		}
		
		notifyProcessStarted(processHelper);
		processHelper.startReaderThreads();
		
		return processHelper;
	}
	
}