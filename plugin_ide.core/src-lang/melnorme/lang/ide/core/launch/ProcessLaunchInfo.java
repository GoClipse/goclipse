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
package melnorme.lang.ide.core.launch;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;

import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.utilbox.misc.Location;

public class ProcessLaunchInfo {
	
	public final IProject project;
	public final BuildTarget buildTarget;
	public final Location programFileLocation;
	public final String[] programArguments;
	public final IPath workingDir;
	public final Map<String, String> environment;
	public final boolean appendEnv;
	
	
	public ProcessLaunchInfo(
			IProject project, 
			BuildTarget buildTarget, 
			Location programLocation, 
			String[] programArguments, 
			IPath workingDir, 
			Map<String, String> environment, 
			boolean appendEnv
	) {
		this.project = assertNotNull(project);
		this.buildTarget = buildTarget;
		this.programFileLocation = assertNotNull(programLocation);
		
		this.programArguments = assertNotNull(programArguments);
		this.workingDir = assertNotNull(workingDir);
		this.environment = environment;
		this.appendEnv = appendEnv;
	}
	
	public IProject getProject() {
		return project;
	}
	
	public BuildTarget getBuildTarget() {
		return buildTarget;
	}
	
	public String getProgramArgumentsString() {
		return DebugPlugin.renderArguments(programArguments, null);
	}
	
}