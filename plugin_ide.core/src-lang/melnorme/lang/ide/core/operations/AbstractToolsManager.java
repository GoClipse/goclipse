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

import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.process.AbstractRunProcessTask;
import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;
import melnorme.utilbox.process.ExternalProcessNotifyingHelper;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class AbstractToolsManager<LISTENER extends ILangOperationsListener> 
		extends ListenerListHelper<LISTENER> {
	
	
	public void notifyBuildStarting(IProject project, boolean clearConsole) {
		for (ILangOperationsListener listener : getListeners()) {
			listener.handleBuildStarted(project, clearConsole);
		}
	}
	
	public void notifyBuildTerminated(IProject project) {
		for (ILangOperationsListener listener : getListeners()) {
			listener.handleBuildTerminated(project);
		}
	}
	
	/* ----------------- ----------------- */
	
	public RunExternalProcessTask newRunToolTask(ProcessBuilder pb, IProject project, IProgressMonitor pm) {
		return new RunExternalProcessTask(pb, project, pm, this);
	}
	
	public ExternalProcessResult runTool(IProject project, IProgressMonitor pm, ProcessBuilder pb) 
			throws CoreException {
		// Note: project can be null
		return runTool(project, pm, pb, null, false);
	}
	
	public ExternalProcessResult runTool(IProject project, IProgressMonitor pm, ProcessBuilder pb,
			String processInput, boolean throwOnNonZero) throws CoreException {
		Path workingDir = project == null ? null : ResourceUtils.getProjectLocation(project);
		if(workingDir != null) {
			pb.directory(workingDir.toFile());
		}
		return newRunToolTask(pb, project, pm).runProcess(processInput, throwOnNonZero);
	}
	
	/* ----------------- ----------------- */
	
	public ExternalProcessResult runEngineTool(ProcessBuilder pb, String clientInput, IProgressMonitor pm)
			throws CoreException {
		return new RunEngineClientOperation(pb, pm).runProcess(clientInput);
	}
	
	public class RunEngineClientOperation extends AbstractRunProcessTask {
		
		public RunEngineClientOperation(ProcessBuilder pb, IProgressMonitor cancelMonitor) {
			super(pb, cancelMonitor);
		}
		
		@Override
		protected void handleProcessStartResult(ExternalProcessNotifyingHelper processHelper, CommonException ce) {
			for (ILangOperationsListener listener : AbstractToolsManager.this.getListeners()) {
				listener.engineClientToolStart(pb, ce, processHelper);
			}
		}
		
	}
	
}