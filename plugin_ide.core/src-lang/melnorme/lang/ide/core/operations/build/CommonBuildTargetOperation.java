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

import java.nio.file.Path;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.utils.ProgressSubTaskHelper;
import melnorme.lang.tooling.bundle.BuildConfiguration;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final BuildManager buildManager;
	protected final IOperationConsoleHandler opHandler;
	protected final Path buildToolPath;
	
	protected final BuildConfiguration buildConfiguration;
	protected final BuildType buildType;
	protected final String[] evaluatedBuildArguments;
	
	public CommonBuildTargetOperation(BuildManager buildManager, BuildTarget buildTarget, 
			IOperationConsoleHandler opHandler, Path buildToolPath) throws CommonException, CoreException {
		super(assertNotNull(buildTarget).getProject());
		this.buildManager = assertNotNull(buildManager);
		this.buildToolPath = buildToolPath;
		this.opHandler = assertNotNull(opHandler);
		
		assertNotNull(buildTarget);
		this.buildConfiguration = assertNotNull(buildTarget.getBuildConfiguration());
		this.buildType = assertNotNull(buildTarget.getBuildType());
		
		this.evaluatedBuildArguments = buildTarget.getEffectiveEvaluatedBuildArguments();
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
		return buildManager.getBuildTargetName2(getConfigurationName(), getBuildTypeName());
	}
	
	protected Path getBuildToolPath() throws CommonException {
		return buildToolPath;
	}
	
	@Override
	public void execute(IProgressMonitor parentPM) throws CoreException, CommonException, OperationCancellation {
		try(ProgressSubTaskHelper pm = new ProgressSubTaskHelper(parentPM, getBuildOperationName())) {
			ProcessBuilder pb = getToolProcessBuilder();
			runBuildToolAndProcessOutput(pb, pm);
		}
	}
	
	protected String getBuildOperationName() {
		return "Building " + getBuildTargetName();
	}
	
	protected ProcessBuilder getToolProcessBuilder() throws CoreException, CommonException, OperationCancellation {
		return getToolProcessBuilder(getEffectiveEvaluatedArguments());
	}
	
	protected ProcessBuilder getToolProcessBuilder(String[] buildArguments) 
			throws CoreException, CommonException, OperationCancellation {
		return getProcessBuilder2(buildArguments);
	}
	
	protected String[] getEffectiveEvaluatedArguments() throws CoreException, CommonException {
		return evaluatedBuildArguments;
	}
	
	protected ProcessBuilder getProcessBuilder2(String[] toolArguments) 
			throws CommonException, OperationCancellation, CoreException {
		return getToolManager().createToolProcessBuilder(getBuildToolPath(), getProjectLocation(), toolArguments);
	}
	
	public void runBuildToolAndProcessOutput(ProcessBuilder pb, IProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation {
		processBuildOutput(runBuildTool(opHandler, pb, pm), pm);
	}
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult, IProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation;
			
}