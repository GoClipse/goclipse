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

import static melnorme.lang.ide.core.utils.TextMessageUtils.headerBIG;

import java.nio.file.Path;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.lang.ide.core.utils.process.EclipseCancelMonitor;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class AbstractToolsManager extends ListenerListHelper<ILangOperationsListener> {
	
	public AbstractToolsManager() {
	}
	
	public void shutdownNow() {
	}
	
	/* -----------------  ----------------- */
	
	public ProcessBuilder createToolProcessBuilder(Path buildToolCmdPath, Location workingDir, 
			String... arguments) {
		return ProcessUtils.createProcessBuilder(buildToolCmdPath, workingDir, true, arguments);
	}
	
	/* -----------------  ----------------- */
	
	public void notifyMessage(StatusLevel statusLevel, String title, String message) {
		for(ILangOperationsListener listener : getListeners()) {
			listener.notifyMessage(statusLevel, title, message);
		}
	}
	
	public void notifyBuildStarting(IProject project, boolean clearConsole) {
		for(ILangOperationsListener listener : getListeners()) {
			listener.handleBuildStarted(project, clearConsole);
		}
	}
	
	public void notifyBuildTerminated(IProject project) {
		for(ILangOperationsListener listener : getListeners()) {
			listener.handleBuildTerminated(project);
		}
	}
	
	public void notifyOperationStarted(OperationInfo opInfo) {
		for(ILangOperationsListener processListener : getListeners()) {
			processListener.handleToolOperationStart(opInfo);
		}
	}
	
	/* ----------------- ----------------- */
	
	protected EclipseCancelMonitor cm(IProgressMonitor pm) {
		return new EclipseCancelMonitor(pm);
	}
	
	public AbstractRunProcessTask newRunToolTask(ProcessBuilder pb, IProject project, IProgressMonitor pm) {
		return newRunToolTask(pb, project, cm(pm));
	}
	public AbstractRunProcessTask newRunToolTask(ProcessBuilder pb, IProject project, ICancelMonitor cm) {
		return new AbstractRunProcessTask(pb, cm) {
			@Override
			protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
				for(ILangOperationsListener processListener : getListeners()) {
					processListener.handleProcessStart(newStartProcessInfo(pb, project, processHelper, ce), null);
				}
			}
		};
	}
	
	protected ProcessStartInfo newStartProcessInfo(ProcessBuilder pb, IProject project,
			ExternalProcessNotifyingHelper processHelper, CommonException ce) {
		return new ProcessStartInfo(pb, project, ">> Running: ", false, processHelper, ce);
	}
	
	/* -----------------  ----------------- */
	
	public RunProcessOperation newRunProcessOperation(IProject project, String operationName, 
			String[] commands, IProgressMonitor pm) {
		OperationInfo opInfo = new OperationInfo(project, false, headerBIG(operationName));
		return newRunProcessTask(opInfo, commands, pm);
	}
	
	public RunProcessOperation newRunProcessTask(OperationInfo opInfo, String[] commands, IProgressMonitor pm) {
		ProcessBuilder pb = createProcessBuilder(opInfo.project, commands);
		return new RunProcessOperation(pb, new EclipseCancelMonitor(pm), opInfo);
	}
	
	public static ProcessBuilder createProcessBuilder(IProject project, String... commands) {
		Path workingDir = project != null ?
			project.getLocation().toFile().toPath() :
			EclipseUtils.getWorkspaceRoot().getLocation().toFile().toPath();
		return new ProcessBuilder(commands).directory(workingDir.toFile());
	}
	
	public class RunProcessOperation extends AbstractRunProcessTask {
		
		protected final OperationInfo opInfo;
		
		public RunProcessOperation(ProcessBuilder pb, ICancelMonitor cancelMonitor, OperationInfo opInfo) {
			super(pb, cancelMonitor);
			this.opInfo = opInfo;
		}
		
		@Override
		protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
			for(ILangOperationsListener processListener : getListeners()) {
				processListener.handleProcessStart(
					new ProcessStartInfo(pb, opInfo.project, "> ", false, processHelper, ce), opInfo);
			}
		}
		
	}
	
	/* ----------------- ----------------- */
	
	public ExternalProcessResult runEngineTool(ProcessBuilder pb, String clientInput, IProgressMonitor pm) 
			throws CoreException, OperationCancellation {
		return runEngineTool(pb, clientInput, cm(pm));
	}
	
	public ExternalProcessResult runEngineTool(ProcessBuilder pb, String clientInput, ICancelMonitor cm) 
			throws CoreException, OperationCancellation {
		try {
			return new RunEngineClientOperation(pb, cm).runProcess(clientInput);
		} catch(CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	public class RunEngineClientOperation extends AbstractRunProcessTask {
		
		public RunEngineClientOperation(ProcessBuilder pb, ICancelMonitor cancelMonitor) {
			super(pb, cancelMonitor);
		}
		
		@Override
		protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
			for (ILangOperationsListener listener : AbstractToolsManager.this.getListeners()) {
				listener.engineClientToolStart(pb, ce, processHelper);
			}
		}
		
	}
	
	public class StartEngineDaemonOperation extends AbstractRunProcessTask {
		
		public StartEngineDaemonOperation(ProcessBuilder pb, ICancelMonitor cancelMonitor) {
			super(pb, cancelMonitor);
		}
		
		@Override
		protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
			for (ILangOperationsListener listener : getListeners()) {
				listener.engineDaemonStart(pb, ce, processHelper);
			}
		}
		
	}
	
}