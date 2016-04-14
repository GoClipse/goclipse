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
package melnorme.lang.ide.core.operations.build;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.utils.ProgressSubTaskHelper;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final IOperationMonitor opMonitor;
	protected final String buildTargetName;
	protected final CommandInvocation buildCommand;
	
	public CommonBuildTargetOperation(IOperationMonitor opMonitor, 
			ToolManager toolManager, String buildTargetName, CommandInvocation buildCommand) {
		super(toolManager, buildCommand.getProject());
		assertNotNull(this.project);
		this.opMonitor = assertNotNull(opMonitor);
		this.buildTargetName = assertNotNull(buildTargetName);
		this.buildCommand = assertNotNull(buildCommand);
	}
	
	public String getBuildTargetName() {
		return buildTargetName;
	}
	
	@Override
	public void execute(IProgressMonitor parentPM) throws CommonException, OperationCancellation {
		try(ProgressSubTaskHelper pm = new ProgressSubTaskHelper(parentPM, getBuildOperationName())) {
			ProcessBuilder pb = getToolProcessBuilder();
			runBuildToolAndProcessOutput(pb, pm);
		}
	}
	
	protected String getBuildOperationName() {
		return "Building " + getBuildTargetName();
	}
	
	public Indexable<String> getEffectiveProccessCommandLine() throws CommonException {
		return buildCommand.getEffectiveCommandLine();
	}
	
	public ProcessBuilder getToolProcessBuilder() throws CommonException, OperationCancellation {
		return getProcessBuilder3(getEffectiveProccessCommandLine());
	}
	
	protected ProcessBuilder getProcessBuilder3(Indexable<String> commandLine) 
			throws CommonException, OperationCancellation {
		return getToolManager().createToolProcessBuilder(commandLine, getProjectLocation());
	}
	
	public void runBuildToolAndProcessOutput(ProcessBuilder pb, IProgressMonitor pm)
			throws CommonException, OperationCancellation {
		processBuildOutput(runBuildTool(opMonitor, pb, pm), pm);
	}
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm)
			throws CommonException, OperationCancellation;
			
}