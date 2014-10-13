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

import melnorme.lang.ide.core.utils.process.RunExternalProcessTask;
import melnorme.utilbox.misc.ListenerListHelper;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract class for running external tools and notifying interested listeners (normally the UI only).
 */
public abstract class AbstractToolsManager<LISTENER extends ILangOperationsListener> 
		extends ListenerListHelper<LISTENER> {
	
	/* ----------------- ----------------- */
	
	public RunExternalProcessTask newRunToolTask(ProcessBuilder pb, IProject project, IProgressMonitor pm) {
		return new RunExternalProcessTask(pb, project, pm, this);
	}
	
	public ExternalProcessResult runTool(IProject project, IProgressMonitor pm, ProcessBuilder pb) 
			throws CoreException {
		// Note: project can be null
		return newRunToolTask(pb, project, pm).runProcess();
	}
	
	/* ----------------- ----------------- */
	
	public ExternalProcessResult runEngineTool(ProcessBuilder pb, String clientInput, IProgressMonitor pm)
			throws CoreException {
		return new RunEngineClientOperation(this, pb, pm).runProcess(clientInput);
	}
	
}