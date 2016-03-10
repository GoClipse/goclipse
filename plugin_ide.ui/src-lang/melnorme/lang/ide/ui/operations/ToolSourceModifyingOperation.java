/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;

import melnorme.lang.ide.core.operations.AbstractToolManager.RunToolTask;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

/**
 * Run a tool that modifies source files.
 */
public class ToolSourceModifyingOperation extends RunToolOperation {
	
	protected IResource lockingRule;
	
	public ToolSourceModifyingOperation(String operationName, IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		super(operationName, project, commands, opViewOptions);
		
		lockingRule = project;
	}
	
	@Override
	protected void doOperation() throws CoreException, CommonException, OperationCancellation {
		boolean result = PlatformUI.getWorkbench().saveAllEditors(true);
		if(result == false) {
			throw new OperationCancellation();
		}
		
		super.doOperation();
	}
	
	@Override
	protected void runProcessTask(RunToolTask runToolTask, IProgressMonitor pm) 
			throws CommonException, OperationCancellation, CoreException {
		
		ResourceUtils.runToolOperation(lockingRule, pm, 
			(pm2) -> {
				pm2.setTaskName(getTaskName());
				ExternalProcessResult result = runToolTask.runProcess();
				
				String toolName = pb.command().get(0);
				ProcessUtils.validateNonZeroExitValue(toolName, result.exitValue);
				do_refreshResources(pm2);
			});
	}
	
	protected void do_refreshResources(IProgressMonitor pm) throws CoreException {
		lockingRule.refreshLocal(IResource.DEPTH_INFINITE, pm);
	}
	
}