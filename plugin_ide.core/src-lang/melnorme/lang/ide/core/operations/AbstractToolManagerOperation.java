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
import melnorme.lang.ide.core.operations.build.IToolOperation;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractToolManagerOperation implements IToolOperation {
	
	protected final IProject project;
	
	public AbstractToolManagerOperation(IProject project) {
		this.project = project;
	}
	
	public IProject getProject() {
		return project;
	}
	
	protected AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	protected ExternalProcessResult runBuildTool(OperationInfo opInfo, ProcessBuilder pb, IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		return getToolManager().newRunToolTask(opInfo, pb, pm).runProcess();
	}
	
}