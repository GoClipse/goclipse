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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationMonitor;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.StartOperationOptions;
import melnorme.lang.ide.core.operations.ToolManager.RunToolTask;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public class RunToolOperation implements ICommonOperation {
			
	protected final IProject project;
	protected final Indexable<String> commands;
	protected final StartOperationOptions opViewOptions;
	
	protected ProcessBuilder pb;
	
	public RunToolOperation(IProject project, Indexable<String> commands,
			StartOperationOptions opViewOptions) {
		this.project = project;
		this.commands = assertNotNull(commands);
		this.opViewOptions = assertNotNull(opViewOptions);
	}
	
	protected ToolManager getToolManager() {
		return LangCore.getToolManager();
	}
	
	@Override
	public void execute(IProgressMonitor pm) throws CommonException, OperationCancellation {
		pb = createProcessBuilder();
		
		IOperationMonitor opHandler = getToolManager().startNewOperation(opViewOptions);
		
		opHandler.writeInfoMessage(
			TextMessageUtils.headerBIG(getOperationStartMessage())
		);
		
		RunToolTask runToolTask = getToolManager().newRunProcessTask(opHandler, pb, pm);
		runProcessTask(runToolTask, pm);
	}
	
	protected ProcessBuilder createProcessBuilder() throws CommonException {
		return getToolManager().createSimpleProcessBuilder(project, getCommands());
	}
	
	protected String getOperationStartMessage() {
		return LangCoreMessages.RunningCommand;
	}
	
	protected void runProcessTask(RunToolTask runToolTask, @SuppressWarnings("unused") IProgressMonitor pm) 
			throws CommonException, OperationCancellation {
		runToolTask.runProcess();
	}
	
	protected String[] getCommands() {
		return commands.toArray(String.class);
	}
	
	/* -----------------  ----------------- */
	
	public static class RunSDKToolOperation extends RunToolOperation {
		
		public RunSDKToolOperation(IProject project, Indexable<String> commands) {
			super(project, commands, new StartOperationOptions(ProcessStartKind.BUILD, true, true));
		}
		
		@Override
		protected ProcessBuilder createProcessBuilder() throws CommonException {
			return getToolManager().createSDKProcessBuilder(project, getCommands());
		}
		
	}
	
}