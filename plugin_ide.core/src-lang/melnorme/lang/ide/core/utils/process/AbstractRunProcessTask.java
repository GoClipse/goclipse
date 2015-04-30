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
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;


/**
 * Helper class around {@link ExternalProcessNotifyingHelper} to start and await completion of an external process, 
 * and providing listeners notification for those events.
 */
public abstract class AbstractRunProcessTask implements IRunProcessTask {
	
	protected final ProcessBuilder pb;
	protected final ICancelMonitor cancelMonitor;
	
	public AbstractRunProcessTask(ProcessBuilder pb, ICancelMonitor cancelMonitor) {
		this.pb = assertNotNull(pb);
		this.cancelMonitor = assertNotNull(cancelMonitor);
	}
	
	public ExternalProcessNotifyingHelper startProcess() throws CommonException {
		return startProcess(cancelMonitor);
	}
	
	protected ExternalProcessNotifyingHelper startProcess(ICancelMonitor cm) throws CommonException {
		Process process;
		try {
			process = ExternalProcessNotifyingHelper.startProcess(pb);
		} catch (CommonException ce) {
			handleProcessStartResult(null, ce);
			throw ce;
		}
		
		return readFromStartedProcess(process, cm);
	}
	
	protected ExternalProcessNotifyingHelper readFromStartedProcess(Process process, ICancelMonitor pm) {
		ExternalProcessNotifyingHelper processHelper = 
				new ExternalProcessNotifyingHelper(process, true, false, pm, LangCore.LOG_HANDLER);
		handleProcessStartResult(processHelper, null);
		processHelper.startReaderThreads();
		return processHelper;
	}
	
	protected abstract void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce);
	
	
	@Override
	public ExternalProcessResult call() throws CommonException, OperationCancellation {
		return runProcess();
	}
	
	public ExternalProcessResult runProcess() throws CommonException, OperationCancellation {
		return runProcess(null);
	}
	
	public ExternalProcessResult runProcess(String input) throws CommonException, OperationCancellation {
		return runProcess(input, false);
	}
	
	public ExternalProcessResult runProcess(String input, boolean throwOnNonZeroStatus) 
			throws CommonException, OperationCancellation 
	{
		return doRunProcess(input, throwOnNonZeroStatus);
	}
	
	public ExternalProcessResult doRunProcess(String input, boolean throwOnNonZeroStatus) 
			throws CommonException, OperationCancellation 
	{
		ICancelMonitor._Util.checkCancelation(cancelMonitor);
		
		ExternalProcessNotifyingHelper processHelper = startProcess(cancelMonitor);
		processHelper.writeInput_(input, StringUtil.UTF8);
		return processHelper.strictAwaitTermination_(throwOnNonZeroStatus);
	}
	
}