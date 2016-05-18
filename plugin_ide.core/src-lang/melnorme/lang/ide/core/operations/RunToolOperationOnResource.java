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
package melnorme.lang.ide.core.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.operations.ToolManager.RunToolTask;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public class RunToolOperationOnResource extends RunToolOperation {
	
	protected final IResource resource = project;
	
	public RunToolOperationOnResource(IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		super(project, commands, opViewOptions);
	}
	
	@Override
	protected void runProcessTask(RunToolTask runToolTask, IOperationMonitor om) 
			throws CommonException, OperationCancellation {
		
		ResourceUtils.runOperationUnderResource(resource, om, 
			(pm2) -> {
				ExternalProcessResult result = runToolTask.runProcess();
				
				String toolName = pb.command().get(0);
				ProcessUtils.validateNonZeroExitValue(toolName, result.exitValue);
			});
	}
	
}