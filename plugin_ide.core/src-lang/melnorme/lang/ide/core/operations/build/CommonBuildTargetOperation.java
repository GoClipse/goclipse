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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.launch.LaunchUtils;
import melnorme.lang.ide.core.operations.AbstractToolManagerOperation;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.StringUtil;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class CommonBuildTargetOperation extends AbstractToolManagerOperation {
	
	protected final BuildManager buildManager;
	protected final OperationInfo parentOperationInfo;
	protected final BuildTargetRunner buildTarget;
	protected final Path buildToolPath;
	protected final boolean fullBuild;
	
	public CommonBuildTargetOperation(BuildManager buildManager, OperationInfo parentOpInfo, IProject project,
			Path buildToolPath, BuildTargetRunner buildTarget, boolean fullBuild) {
		super(project);
		this.buildManager = assertNotNull(buildManager);
		this.buildToolPath = buildToolPath;
		this.fullBuild = fullBuild;
		this.parentOperationInfo = assertNotNull(parentOpInfo);
		this.buildTarget = assertNotNull(buildTarget);
	}
	
	protected Path getBuildToolPath() throws CommonException {
		return buildToolPath;
	}
	
	protected String getConfiguration() {
		return StringUtil.nullAsEmpty(buildTarget.getBuildConfigName());
	}
	
	public String getBuildType() {
		return StringUtil.nullAsEmpty(buildTarget.getBuildTypeName());
	}
	
	protected String getEffectiveBuildOptions() throws CommonException {
		return buildTarget.getEffectiveBuildOptions();
	}
	
	protected String[] getEvaluatedAndParserArguments() throws CoreException, CommonException {
		return LaunchUtils.getEvaluatedAndParsedArguments(getEffectiveBuildOptions());
	}
	
	@Override
	public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
		ArrayList2<String> commands = new ArrayList2<String>();
		addToolCommand(commands);
		
		addMainArguments(commands);
		
		commands.addElements(getEvaluatedAndParserArguments());
		
		ExternalProcessResult processResult = startProcess(pm, commands);
		processBuildOutput(processResult);
	}
	
	protected void addToolCommand(ArrayList2<String> commands) throws CommonException {
		commands.add(getBuildToolPath().toString());
	}
	
	protected abstract void addMainArguments(ArrayList2<String> commands);
	
	// TODO: write some default for this method, refactor associated code.
	protected abstract ExternalProcessResult startProcess(IProgressMonitor pm, ArrayList2<String> commands)
			throws CommonException, OperationCancellation;
	
	protected abstract void processBuildOutput(ExternalProcessResult processResult) throws CoreException;
	
}