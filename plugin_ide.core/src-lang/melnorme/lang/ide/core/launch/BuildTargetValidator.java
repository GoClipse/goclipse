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

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.tooling.data.AbstractValidator2;
import melnorme.utilbox.core.CommonException;

public class BuildTargetValidator extends AbstractValidator2 {
	
	public BuildTarget getBuildTarget_nonNull(IProject project, String buildTargetAttribute)
			throws CommonException {
		BuildTarget buildTarget = getBuildTarget(project, buildTargetAttribute);
		if(buildTarget == null) {
			throw error(LaunchMessages.PROCESS_LAUNCH_NoBuildTargetSpecified);
		}
		return buildTarget;
	}
	
	public BuildTarget getBuildTarget(IProject project, String buildTargetName) 
			throws CommonException {
		if(project == null || buildTargetName == null) {
			return null;
		}
		
		ProjectBuildInfo buildInfo = LangCore.getBuildManager().getBuildInfo_NonNull(project);
		
		BuildTarget buildTarget = buildInfo.getBuildTarget(buildTargetName);
		if(buildTarget == null) {
			throw error(LaunchMessages.PROCESS_LAUNCH_NoSuchBuildTarget);
		}
		return buildTarget;
	}
	
}