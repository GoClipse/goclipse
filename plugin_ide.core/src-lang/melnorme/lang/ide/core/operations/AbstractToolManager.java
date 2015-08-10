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

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.ILangOperationsListener;
import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.operation.EclipseCancelMonitor;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask.ProcessStartHelper;
import melnorme.lang.tooling.data.PathValidator;
import melnorme.lang.tooling.data.StatusException;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.lang.tooling.ops.IOperationHelper;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.fields.IValidatedField;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class AbstractToolManager extends ListenerListHelper<ILangOperationsListener> {
	
	public AbstractToolManager() {
	}
	
	public void shutdownNow() {
	}
	
	/* -----------------  ----------------- */
	
	public Path getSDKToolPath() throws CommonException {
		return getSDKToolPathField().getValidatedField();
	}
	
	protected IValidatedField<Path> getSDKToolPathField() {
		return new SDKToolPathField(getSDKToolPathValidator());
	}
	
	public static class SDKToolPathField implements IValidatedField<Path> {
		
		protected final PathValidator pathValidator;
		
		public SDKToolPathField(PathValidator pathValidator) {
			this.pathValidator = pathValidator;
		}
		
		protected String getRawFieldValue() {
			return ToolchainPreferences.SDK_PATH.get();
		}
		
		@Override
		public Path getValidatedField() throws StatusException {
			String pathString = getRawFieldValue();
			return getPathValidator().getValidatedPath(pathString);
		}
		
		protected PathValidator getPathValidator() {
			return pathValidator;
		}
		
	}
	
	protected abstract PathValidator getSDKToolPathValidator();
	
	/* -----------------  ----------------- */
	
	public ProcessBuilder createSDKProcessBuilder(IProject project, String... sdkOptions)
			throws CoreException, CommonException {
		Location projectLocation = ResourceUtils.getProjectLocation(project);
		Path sdkToolPath = getSDKToolPath();
		return createToolProcessBuilder(sdkToolPath, projectLocation, sdkOptions);
	}
	
	public ProcessBuilder createToolProcessBuilder(Path buildToolCmdPath, Location workingDir, String... arguments) {
		return ProcessUtils.createProcessBuilder(buildToolCmdPath, workingDir, true, arguments);
	}
	
	public static ProcessBuilder createProcessBuilder(IProject project, String... commands) {
		Path workingDir = project != null ?
			project.getLocation().toFile().toPath() :
			EclipseUtils.getWorkspaceRoot().getLocation().toFile().toPath();
		return new ProcessBuilder(commands).directory(workingDir.toFile());
	}
	
	/* -----------------  ----------------- */
	
	public void notifyMessage(StatusLevel statusLevel, String title, String message) {
		for(ILangOperationsListener listener : getListeners()) {
			listener.notifyMessage(statusLevel, title, message);
		}
	}
	
	public void notifyOperationStarted(OperationInfo opInfo) {
		for(ILangOperationsListener processListener : getListeners()) {
			processListener.handleNewOperation(opInfo);
		}
	}
	
	public void notifyMessageEvent(MessageEventInfo messageInfo) {
		for(ILangOperationsListener processListener : getListeners()) {
			processListener.handleMessage(messageInfo);
		}
	}
	
	/* ----------------- ----------------- */
	
	public OperationInfo startNewToolOperation() {
		OperationInfo opInfo = new OperationInfo(null);
		notifyOperationStarted(opInfo);
		opInfo.setStarted(true);
		return opInfo;
	}
	
	protected EclipseCancelMonitor cm(IProgressMonitor pm) {
		return new EclipseCancelMonitor(pm);
	}
	
	public RunProcessTask newRunToolOperation2(ProcessBuilder pb, IProgressMonitor pm) {
		OperationInfo opInfo = startNewToolOperation();
		return newRunToolTask(opInfo, pb, cm(pm));
	}
	
	public RunProcessTask newRunToolTask(OperationInfo opInfo, ProcessBuilder pb, IProgressMonitor pm) {
		return newRunToolTask(opInfo, pb, cm(pm));
	}
	public RunProcessTask newRunToolTask(OperationInfo opInfo, ProcessBuilder pb, ICancelMonitor cm) {
		return new RunProcessTask(opInfo, pb, cm);
	}
	
	public class RunProcessTask extends AbstractRunProcessTask {
		
		protected final OperationInfo opInfo;
		
		public RunProcessTask(OperationInfo opInfo, ProcessBuilder pb, ICancelMonitor cancelMonitor) {
			super(pb, cancelMonitor);
			this.opInfo = opInfo;
		}
		
		@Override
		protected void handleProcessStartResult(ProcessStartHelper psh) {
			for(ILangOperationsListener processListener : getListeners()) {
				processListener.handleProcessStart(newProcessStartInfo(opInfo, pb, psh));
			}
		}
		
	}
	
	protected ProcessStartInfo newProcessStartInfo(OperationInfo opInfo, ProcessBuilder pb, ProcessStartHelper psh) {
		return new ProcessStartInfo(opInfo, pb, ">> Running: ", psh);
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
		protected void handleProcessStartResult(ProcessStartHelper psh) {
			for (ILangOperationsListener listener : AbstractToolManager.this.getListeners()) {
				listener.engineClientToolStart(pb, psh);
			}
		}
		
	}
	
	public class StartEngineDaemonOperation extends AbstractRunProcessTask {
		
		public StartEngineDaemonOperation(ProcessBuilder pb, ICancelMonitor cancelMonitor) {
			super(pb, cancelMonitor);
		}
		
		@Override
		protected void handleProcessStartResult(ProcessStartHelper psh) {
			for (ILangOperationsListener listener : getListeners()) {
				listener.engineDaemonStart(pb, psh);
			}
		}
		
	}
	
	/* -----------------  ----------------- */
	
	/** 
	 * Helper to start engine client processes in the tool manager. 
	 */
	public class ToolManagerEngineToolRunner implements IOperationHelper {
		
		protected final boolean throwOnNonZeroStatus;
		protected final EclipseCancelMonitor cm;
		
		public ToolManagerEngineToolRunner(IProgressMonitor monitor, boolean throwOnNonZeroStatus) {
			this.throwOnNonZeroStatus = throwOnNonZeroStatus;
			this.cm = new EclipseCancelMonitor(monitor);
		}
		
		@Override
		public ExternalProcessResult runProcess(ProcessBuilder pb, String input) throws CommonException,
				OperationCancellation {
			return new RunEngineClientOperation(pb, cm).runProcess(input, throwOnNonZeroStatus);
		}
		
		@Override
		public void logStatus(StatusException statusException) {
			LangCore.logStatusException(statusException);
		}
		
	}
	
}