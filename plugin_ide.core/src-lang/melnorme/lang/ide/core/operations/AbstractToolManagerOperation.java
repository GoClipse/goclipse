/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.AbstractToolManager.RunToolTask;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolManagerOperation implements ICoreOperation {
	
	protected final IProject project;
	
	public AbstractToolManagerOperation(IProject project) {
		this.project = project;
	}
	
	public IProject getProject() {
		return project;
	}
	
	protected Location getProjectLocation() throws CommonException {
		return ResourceUtils.getProjectLocation2(project);
	}
	
	protected AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	protected ExternalProcessResult runBuildTool(IOperationConsoleHandler opHandler, ProcessBuilder pb, 
			IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		AbstractToolManager toolMgr = getToolManager();
		RunToolTask newRunToolTask = toolMgr.newRunProcessTask(opHandler, pb, pm);
		return newRunToolTask.runProcess();
	}
	
}