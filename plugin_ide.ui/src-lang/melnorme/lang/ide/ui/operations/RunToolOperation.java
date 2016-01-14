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
package melnorme.lang.ide.ui.operations;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.operations.AbstractToolManager;
import melnorme.lang.ide.core.operations.AbstractToolManager.RunToolTask;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.lang.ide.ui.utils.operations.AbstractUIOperation;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class RunToolOperation extends AbstractUIOperation {
			
	protected final IProject project;
	protected final Indexable<String> commands;
	protected final StartOperationOptions opViewOptions;
	
	public RunToolOperation(String operationName, IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		super(operationName);
		this.project = project;
		this.commands = assertNotNull(commands);
		this.opViewOptions = assertNotNull(opViewOptions);
	}
	
	protected AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	@Override
	protected void doBackgroundComputation(IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		ProcessBuilder pb = createProcessBuilder();
		
		IOperationConsoleHandler opHandler = getToolManager().startNewOperation(opViewOptions);
		
		opHandler.writeInfoMessage(
			TextMessageUtils.headerBIG(getOperationStartMessage())
		);
		
		RunToolTask runToolTask = getToolManager().newRunProcessTask(opHandler, pb, monitor);
		runProcessTask(runToolTask);
	}
	
	protected String getOperationStartMessage() {
		return LangCoreMessages.RunningCommand;
	}
	
	protected ProcessBuilder createProcessBuilder() throws CoreException, CommonException {
		return AbstractToolManager.createProcessBuilder(project, getCommands());
	}
	
	protected void runProcessTask(RunToolTask runToolTask) throws CommonException, OperationCancellation {
		runToolTask.runProcess();
	}
	
	protected String[] getCommands() {
		return commands.toArray(String.class);
	}
	
	/* -----------------  ----------------- */
	
	public abstract static class RunSDKToolOperation extends RunToolOperation {
		
		public RunSDKToolOperation(String operationName, IProject project, Indexable<String> commands) {
			super(operationName, project, commands, 
				new StartOperationOptions(ProcessStartKind.BUILD, true, false));
		}
		
		@Override
		protected ProcessBuilder createProcessBuilder() throws CoreException, CommonException {
			return getToolManager().createSDKProcessBuilder(project, getCommands());
		}
		
	}
	
}