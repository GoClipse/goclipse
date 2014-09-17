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
package melnorme.lang.ide.core.operations;

import melnorme.lang.ide.core.utils.process.EclipseExternalProcessHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;


public abstract class AbstractStartProcessTask {
	
	protected final ProcessBuilder pb;
	
	public AbstractStartProcessTask(ProcessBuilder pb) {
		this.pb = pb;
	}
	
	public EclipseExternalProcessHelper call() throws CoreException {
		return startProcess();
	}
	
	public ExternalProcessResult runProcess(String bufferText, IProgressMonitor pm) throws CoreException {
		EclipseExternalProcessHelper processHelper = startProcess(pm);
		processHelper.writeInput(bufferText);
		return processHelper.strictAwaitTermination();
	}
	
	public EclipseExternalProcessHelper startProcess() throws CoreException {
		return startProcess(new NullProgressMonitor());
	}
	
	public EclipseExternalProcessHelper startProcess(IProgressMonitor pm) throws CoreException {
		Process process;
		try {
			process = EclipseExternalProcessHelper.startProcess(pb);
		} catch (CoreException ce) {
			handleProcessStartResult(null, ce);
			throw ce;
		}
		
		return readFromProcess(pm, process);
	}
	
	protected EclipseExternalProcessHelper readFromProcess(IProgressMonitor pm, Process process) {
		EclipseExternalProcessHelper eclipseProcessHelper = new EclipseExternalProcessHelper(process, false, pm);
		ExternalProcessNotifyingHelper processHelper = eclipseProcessHelper.getProcessHelper(); 
		handleProcessStartResult(processHelper, null);
		processHelper.startReaderThreads();
		return eclipseProcessHelper;
	}
	
	protected abstract void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CoreException ce);
	
}