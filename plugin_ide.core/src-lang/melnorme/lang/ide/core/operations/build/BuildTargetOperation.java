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
import static melnorme.utilbox.core.CoreUtil.option;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationMonitor;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.utils.ProgressSubTaskHelper;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class BuildTargetOperation extends AbstractToolManagerOperation {
	
	public static class BuildOperationParameters {
		
		public IOperationMonitor opMonitor;
		public ToolManager toolManager;
		public IProject project;
		public String buildTargetName;
		public CommandInvocation buildCommand;
		
		public BuildOperationParameters(IOperationMonitor opMonitor, ToolManager toolManager, IProject project,
				String buildTargetName, CommandInvocation buildCommand) {
			this.opMonitor = opMonitor;
			this.toolManager = toolManager;
			this.project = project;
			this.buildTargetName = buildTargetName;
			this.buildCommand = buildCommand;
		}
		
	}
	protected final IOperationMonitor opMonitor;
	protected final String buildTargetName;
	protected final CommandInvocation buildCommand;
	
	public BuildTargetOperation(BuildOperationParameters buildOpParams) {
		super(buildOpParams.toolManager, buildOpParams.project);
		assertNotNull(this.project);
		this.opMonitor = assertNotNull(buildOpParams.opMonitor);
		this.buildTargetName = assertNotNull(buildOpParams.buildTargetName);
		this.buildCommand = assertNotNull(buildOpParams.buildCommand);
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
	
	public Iterable<String> getEffectiveProccessCommandLine() throws CommonException {
		return getConfiguredProcessBuilder2().command();
	}
	
	public ProcessBuilder getConfiguredProcessBuilder2() throws CommonException {
		VariablesResolver variablesManager = toolManager.getVariablesManager(option(getProject()));
		ProcessBuilder pb = buildCommand.getProcessBuilder(variablesManager);
		pb.directory(getProjectLocation().toFile());
		return pb;
	}
	
	public ProcessBuilder getToolProcessBuilder() throws CommonException, OperationCancellation {
		return getToolManager().modifyToolProcessBuilder(getConfiguredProcessBuilder2());
	}
	
	public void runBuildToolAndProcessOutput(ProcessBuilder pb, IProgressMonitor pm)
			throws CommonException, OperationCancellation {
		processBuildOutput(runBuildTool(opMonitor, pb, pm), pm);
	}
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm)
			throws CommonException, OperationCancellation;
			
}