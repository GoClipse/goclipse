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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.process.EclipseProcessHelper;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;


public abstract class AbstractStartProcessTask {
	
	protected final ProcessBuilder pb;
	
	public AbstractStartProcessTask(ProcessBuilder pb) {
		this.pb = pb;
	}
	
	public ExternalProcessNotifyingHelper call() throws CoreException {
		try {
			return startProcess();
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce.getMessage(), ce.getCause());
		}
	}
	
	public ExternalProcessNotifyingHelper startProcess() throws CommonException {
		return startProcess(new NullProgressMonitor());
	}
	
	public ExternalProcessNotifyingHelper startProcess(IProgressMonitor pm) throws CommonException {
		Process process;
		try {
			process = ExternalProcessNotifyingHelper.startProcess(pb);
		} catch (CommonException ce) {
			/* FIXME: use CommonException in listerners*/
			handleProcessStartResult(null, LangCore.createCoreException(ce));
			throw ce;
		}
		
		return readFromProcess(pm, process);
	}
	
	public ExternalProcessResult runProcess(String inputText, IProgressMonitor pm) throws CommonException {
		ExternalProcessNotifyingHelper processHelper = startProcess(pm);
		processHelper.writeInput_(inputText);
		return processHelper.strictAwaitTermination_();
	}
	
	protected ExternalProcessNotifyingHelper readFromProcess(IProgressMonitor pm, Process process) {
		ExternalProcessNotifyingHelper processHelper = new EclipseProcessHelper(process, false, pm);
		handleProcessStartResult(processHelper, null);
		processHelper.startReaderThreads();
		return processHelper;
	}
	
	protected abstract void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CoreException ce);
	
}