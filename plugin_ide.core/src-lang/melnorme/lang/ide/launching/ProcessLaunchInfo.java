/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.launching;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

import melnorme.lang.ide.core.operations.build.BuildTarget;

public class ProcessLaunchInfo {
	
	public final IPath programPath;
	public final String[] programArguments;
	public final IPath workingDir;
	public final Map<String, String> environment;
	public final boolean appendEnv;
	
	public final IProject project;
	public final BuildTarget buildTarget;
	
	public ProcessLaunchInfo(IPath programPath, String[] programArguments, IPath workingDir, 
			Map<String, String> environment, boolean appendEnv, IProject project, BuildTarget buildTarget) {
		this.programPath = assertNotNull(programPath);
		this.programArguments = assertNotNull(programArguments);
		this.workingDir = assertNotNull(workingDir);
		this.environment = environment;
		this.appendEnv = appendEnv;
		
		this.project = project;
		this.buildTarget = buildTarget;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public BuildTarget getBuildTarget() {
		return buildTarget;
	}
	
}