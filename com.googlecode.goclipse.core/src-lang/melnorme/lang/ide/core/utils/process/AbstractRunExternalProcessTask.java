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

import melnorme.utilbox.core.fntypes.ICallable;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * {@link ICallable} task that runs an external process and waits for it to terminate.
 */
public abstract class AbstractRunExternalProcessTask implements
		ICallable<ExternalProcessEclipseHelper, CoreException> {
	
	protected final ProcessBuilder pb;
	protected final IProject project;
	protected final IProgressMonitor cancelMonitor;
	
	public AbstractRunExternalProcessTask(ProcessBuilder pb, IProject project, IProgressMonitor cancelMonitor) {
		this.pb = assertNotNull(pb);
		this.project = project; // can be null
		this.cancelMonitor = assertNotNull(cancelMonitor);
	}
	
	@Override
	public ExternalProcessEclipseHelper call() throws CoreException {
		return startProcessAndAwait();
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
	
	protected abstract List<? extends IExternalProcessListener> getListeners();
	
	public ExternalProcessEclipseHelper startProcessAndAwait() throws CoreException {
		ExternalProcessEclipseHelper processHelper = startProcess();
		processHelper.awaitTermination_CoreException();
		return processHelper;
	}
	
	public ExternalProcessEclipseHelper startProcess() throws CoreException {
		ExternalProcessEclipseHelper processHelper;
		try {
			processHelper = new ExternalProcessEclipseHelper(pb, false, cancelMonitor);
		} catch (CoreException ce) {
			IOException ioe = (IOException) ce.getStatus().getException();
			notifyProcessFailedToStart(ioe);
			throw ce;
		}
		
		notifyProcessStarted(processHelper);
		processHelper.startReaderThreads();
		
		return processHelper;
	}
	
	public ExternalProcessEclipseHelper startProcessAndAwait(String input) throws CoreException {
		ExternalProcessEclipseHelper processHelper = startProcess();
		processHelper.writeInput(input);
		processHelper.awaitTermination_CoreException();
		return processHelper;
	}
	
}