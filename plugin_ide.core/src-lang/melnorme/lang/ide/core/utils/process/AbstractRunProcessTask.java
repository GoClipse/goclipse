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
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper.IProcessOutputListener;


/**
 * Helper class around {@link ExternalProcessNotifyingHelper} to start and await completion of an external process, 
 * and providing listeners notification for those events.
 */
public abstract class AbstractRunProcessTask implements IRunProcessTask {
	
	protected final ProcessBuilder pb;
	protected final ICancelMonitor cancelMonitor;
	protected ArrayList2<IProcessOutputListener> processListeners = new ArrayList2<>();
	
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
			handleProcessStartResult(new ProcessStartHelper() {
				@Override
				public void addProcessListener(IProcessOutputListener listener) throws CommonException {
					throw ce;
				}
			});
			throw ce;
		}
		
		return readFromStartedProcess(process, cm);
	}
	
	protected ExternalProcessNotifyingHelper readFromStartedProcess(Process process, ICancelMonitor pm) {
		handleProcessStartResult(new ProcessStartHelper() {
			@Override
			public void addProcessListener(IProcessOutputListener listener) throws CommonException {
				assertNotNull(processListeners);
				processListeners.add(listener);
			}
		});
		
		try {
			return new ExternalProcessNotifyingHelper(process, true, pm, processListeners, LangCore.LOG_HANDLER);
		} finally {
			processListeners = null; // Set to null to fail fast if anyone else tries to modify afterward.
		}
	}
	
	protected abstract void handleProcessStartResult(ProcessStartHelper psh);
	
	
	public static interface ProcessStartHelper {
		
		/**
		 * Add given process listener to a process that is about to have its output read. 
		 * @throws CommonException if process was unable to be started.
		 */
		void addProcessListener(IProcessOutputListener listener) throws CommonException;
		
	}
	
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
		ExternalProcessResult processResult = processHelper.awaitTerminationAndResult_ce();
		
		if(throwOnNonZeroStatus) {
			ProcessUtils.validateNonZeroExitValue(processResult.exitValue);
		}
		return processResult;
	}
	
}