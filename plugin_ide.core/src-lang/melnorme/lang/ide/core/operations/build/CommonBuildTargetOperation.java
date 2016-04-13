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
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.ToolManager;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.utils.ProgressSubTaskHelper;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final String buildTargetName;
	protected final IOperationConsoleHandler opHandler;
	
	protected final BuildConfiguration buildConfiguration;
	protected final BuildType buildType;
	protected final String buildCommand;
	
	public CommonBuildTargetOperation(
			ToolManager toolManager, BuildTarget buildTarget, IOperationConsoleHandler opHandler
	) throws CommonException {
		super(toolManager, assertNotNull(buildTarget).getProject());
		this.buildTargetName = buildTarget.getBuildTargetName();
		this.opHandler = assertNotNull(opHandler);
		
		assertNotNull(buildTarget);
		this.buildConfiguration = assertNotNull(buildTarget.getBuildConfiguration());
		this.buildType = assertNotNull(buildTarget.getBuildType());
		this.buildCommand = assertNotNull(buildTarget.getEffectiveBuildCommand());
	}
	
	public BuildConfiguration getConfiguration() {
		return buildConfiguration;
	}
	
	public String getConfigurationName() {
		return buildConfiguration.getName();
	}
	
	public BuildType getBuildType() {
		return buildType;
	}
	
	public String getBuildTypeName() {
		return buildType.getName();
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
		return evaluateBuildCommand(buildCommand);
	}
	
	protected ArrayList2<String> evaluateBuildCommand(String toolCommandLine) throws CommonException {
		VariablesResolver variablesManager = getToolManager().getVariablesManager(project);
		
		toolCommandLine = variablesManager.performStringSubstitution(toolCommandLine);
		
		String[] evaluatedArguments = DebugPlugin.parseArguments(toolCommandLine);
		return new ArrayList2<>(evaluatedArguments);
	}
	
	protected ProcessBuilder getToolProcessBuilder() throws CommonException, OperationCancellation {
		return getProcessBuilder3(getEffectiveProccessCommandLine());
	}
	
	protected ProcessBuilder getProcessBuilder3(Indexable<String> commandLine) 
			throws CommonException, OperationCancellation {
		return getToolManager().createToolProcessBuilder(commandLine, getProjectLocation());
	}
	
	public void runBuildToolAndProcessOutput(ProcessBuilder pb, IProgressMonitor pm)
			throws CommonException, OperationCancellation {
		processBuildOutput(runBuildTool(opHandler, pb, pm), pm);
	}
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm)
			throws CommonException, OperationCancellation;
			
}