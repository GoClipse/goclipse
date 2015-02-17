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
import melnorme.lang.ide.core.LangCore;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;


/**
 * Helper class to start, or run (start & await completion) of an external process.
 * Provides support for {@link IProgressMonitor} cancellation, and notifying listeners. 
 */
public abstract class AbstractRunProcessTask implements IRunProcessTask {
	
	protected final ProcessBuilder pb;
	protected final IProgressMonitor cancelMonitor;
	
	public AbstractRunProcessTask(ProcessBuilder pb, IProgressMonitor cancelMonitor) {
		this.pb = assertNotNull(pb);
		this.cancelMonitor = assertNotNull(cancelMonitor);
	}
	
	public ExternalProcessNotifyingHelper startProcess() throws CommonException {
		return startProcess(cancelMonitor);
	}
	
	protected ExternalProcessNotifyingHelper startProcess(IProgressMonitor pm) throws CommonException {
		Process process;
		try {
			process = ExternalProcessNotifyingHelper.startProcess(pb);
		} catch (CommonException ce) {
			handleProcessStartResult(null, ce);
			throw ce;
		}
		
		return readFromStartedProcess(process, pm);
	}
	
	protected ExternalProcessNotifyingHelper readFromStartedProcess(Process process, IProgressMonitor pm) {
		ExternalProcessNotifyingHelper processHelper = new EclipseProcessHelper(process, false, pm);
		handleProcessStartResult(processHelper, null);
		processHelper.startReaderThreads();
		return processHelper;
	}
	
	protected abstract void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce);
	
	
	@Override
	public ExternalProcessResult call() throws CoreException {
		return runProcess();
	}
	
	public ExternalProcessResult runProcess() throws CoreException {
		return runProcess(null);
	}
	
	public ExternalProcessResult runProcess(String input) throws CoreException {
		return runProcess(input, false);
	}
	
	public ExternalProcessResult runProcess(String input, boolean throwOnNonZeroStatus) throws CoreException {
		try {
			return doRunProcess(input, throwOnNonZeroStatus);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	public ExternalProcessResult doRunProcess(String input, boolean throwOnNonZeroStatus) throws CommonException {
		ExternalProcessNotifyingHelper processHelper = startProcess(cancelMonitor);
		processHelper.writeInput_(input, StringUtil.UTF8);
		return processHelper.strictAwaitTermination_(throwOnNonZeroStatus);
	}
	
}