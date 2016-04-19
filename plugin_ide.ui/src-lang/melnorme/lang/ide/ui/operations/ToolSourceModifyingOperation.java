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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PlatformUI;

import melnorme.lang.ide.core.operations.ToolManager.RunToolTask;
import melnorme.lang.ide.core.LangCore;
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
	
	protected IResource resource;
	
	public ToolSourceModifyingOperation(String operationName, IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions, @SuppressWarnings("unused") boolean APIdummy) {
		super(operationName, project, commands, opViewOptions);
		
		resource = assertNotNull(project);
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
		
		ResourceUtils.runOperation(resource, pm, 
			(pm2) -> {
				pm2.setTaskName(getTaskName());
				ExternalProcessResult result = runToolTask.runProcess();
				
				String toolName = pb.command().get(0);
				ProcessUtils.validateNonZeroExitValue(toolName, result.exitValue);
				do_refreshResources(pm2);
			});
	}
	
	protected void do_refreshResources(IProgressMonitor pm) throws CommonException {
		try {
			resource.refreshLocal(IResource.DEPTH_INFINITE, pm);
		} catch(CoreException e) {
			throw LangCore.createCommonException(e);
		}
	}
	
}