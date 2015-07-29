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

import melnorme.lang.ide.core.launch.LaunchUtils;
import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildConfiguration;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final BuildManager buildManager;
	protected final OperationInfo opInfo;
	protected final Path buildToolPath;
	protected final boolean fullBuild;
	
	protected final BuildConfiguration buildConfiguration;
	protected final BuildType buildType;
	protected final String effectiveBuildOptions;
	
	public CommonBuildTargetOperation(BuildManager buildManager, BuildTargetValidator buildTargetValidator, 
			OperationInfo opInfo, Path buildToolPath, boolean fullBuild) throws CommonException, CoreException {
		super(assertNotNull(buildTargetValidator).getProject());
		this.buildManager = assertNotNull(buildManager);
		this.buildToolPath = buildToolPath;
		this.fullBuild = fullBuild;
		this.opInfo = assertNotNull(opInfo);
		
		assertNotNull(buildTargetValidator);
		this.buildConfiguration = buildTargetValidator.getBuildConfiguration();
		this.buildType = buildTargetValidator.getBuildType();
		this.effectiveBuildOptions = buildTargetValidator.getEffectiveBuildOptions();
	}
	
	protected BuildConfiguration getConfiguration() {
		return buildConfiguration;
	}
	
	protected String getConfigurationName() {
		return buildConfiguration.getName();
	}
	
	protected BuildType getBuildType() {
		return buildType;
	}
	
	protected String getBuildTypeName() {
		return buildType.getName();
	}
	
	protected Path getBuildToolPath() throws CommonException {
		return buildToolPath;
	}
	
	protected String getEffectiveBuildOptions() throws CommonException, CoreException {
		return effectiveBuildOptions;
	}
	
	@Override
	public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		ProcessBuilder pb = getToolProcessBuilder();
		runBuildToolAndProcessOutput(pb, pm);
	}
	
	protected ProcessBuilder getToolProcessBuilder() throws CoreException, CommonException, OperationCancellation {
		return getToolProcessBuilder(getMainArguments(), getEvaluatedAndParsedArguments());
	}
	
	protected ProcessBuilder getToolProcessBuilder(String[] mainArguments, String[] extraArguments) 
			throws CoreException, CommonException, OperationCancellation {
		ArrayList2<String> commands = new ArrayList2<String>();
		addToolCommand(commands);
		commands.addElements(mainArguments);
		commands.addElements(extraArguments);
		return getProcessBuilder(commands);
	}
	
	protected void addToolCommand(ArrayList2<String> commands) 
			throws CoreException, CommonException, OperationCancellation {
		commands.add(getBuildToolPath().toString());
	}
	
	protected abstract String[] getMainArguments()
			throws CoreException, CommonException, OperationCancellation;
	
	protected String[] getEvaluatedAndParsedArguments() throws CoreException, CommonException {
		return LaunchUtils.getEvaluatedAndParsedArguments(getEffectiveBuildOptions());
	}
	
	protected abstract ProcessBuilder getProcessBuilder(ArrayList2<String> commands) 
			throws CommonException, OperationCancellation, CoreException;
	
	public void runBuildToolAndProcessOutput(ProcessBuilder pb, IProgressMonitor pm)
			throws CoreException, CommonException, OperationCancellation {
		processBuildOutput(runBuildTool(opInfo, pb, pm));
	}
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult)
			throws CoreException, CommonException, OperationCancellation;
			
}