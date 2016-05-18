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
package melnorme.lang.ide.core.launch;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.tooling.common.ops.ICommonOperation;
import melnorme.lang.tooling.utils.ArgumentsParser;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public class ProcessLaunchInfoValidator {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	protected final CompositeBuildTargetSettings buildTargetSettings;
	
	protected final String programArguments;
	protected final String workingDirectory;
	protected final Map<String, String> environmentVars;
	protected final boolean appendEnvironmentVars;
	
	public ProcessLaunchInfoValidator(CompositeBuildTargetSettings buildTargetSettings, String programArguments,
			String workingDirectory, Map<String, String> environmentVars, boolean appendEnvironmentVars) {
		this.buildTargetSettings = buildTargetSettings;
		this.programArguments = programArguments;
		this.workingDirectory = workingDirectory;
		this.environmentVars = environmentVars;
		this.appendEnvironmentVars = appendEnvironmentVars;
	}
	
	/* -----------------  ----------------- */
	
	protected IProject getProject() throws CommonException {
		return buildTargetSettings.btSupplier.getValidProject();
	}
	
	protected BuildTarget getBuildTarget() throws CommonException {
		return buildTargetSettings.getValidBuildTarget();
	}
	
	protected Location getValidExecutableFileLocation() throws CommonException {
		return getBuildTarget().getValidExecutableLocation();
	}
	
	/* -----------------  ----------------- */
	
	public IPath getWorkingDirectory() throws CommonException {
		IPath path = getDefinedWorkingDirectory();
		if(path == null) {
			return getDefaultWorkingDirectory();
		}
		return path;
	}
	
	public IPath getDefinedWorkingDirectory() throws CommonException {
		return workingDirectory.isEmpty() ? null : new org.eclipse.core.runtime.Path(workingDirectory);
	}
	
	public IPath getDefaultWorkingDirectory() throws CommonException {
		return getProject().getLocation();
	}
	
	/* -----------------  ----------------- */

	public Indexable<String> getProgramArguments() throws CoreException {
		return ArgumentsParser.parse(programArguments);
	}
	
	/* -----------------  ----------------- */
	
	public Map<String, String> getValidEnvironmentVars() throws CoreException {
		return environmentVars;
	}
	
	/* ======================   ====================== */
	
	public ProcessLaunchInfo getValidProcessLaunchInfo() throws CommonException, CoreException {
		
		IProject project = getProject();
		BuildTarget buildTarget = getBuildTarget();
		ICommonOperation buildOperation = buildTarget == null ? 
				null : buildManager.newBuildTargetOperation(getProject(), buildTarget);
		
		Location programLoc = getValidExecutableFileLocation(); // not null
		
		String[] processArgs = getProgramArguments().toArray(String.class);
		IPath workingDirectory = getWorkingDirectory();
		Map<String, String> configEnv = getValidEnvironmentVars();
		
		return new ProcessLaunchInfo(
			project, 
			buildOperation, 
			programLoc,
			processArgs, 
			workingDirectory, 
			configEnv, 
			appendEnvironmentVars);
	}
	
}