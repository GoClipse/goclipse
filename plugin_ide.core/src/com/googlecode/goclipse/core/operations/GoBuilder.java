/*******************************************************************************
 * Copyright (c) 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.core.operations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.googlecode.goclipse.tooling.env.GoEnvironment;

import melnorme.lang.ide.core.operations.LangProjectBuilder;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.core.CommonException;


public class GoBuilder extends LangProjectBuilder {
	
	public GoBuilder() {
	}
	
	/* -----------------  ----------------- */
	
	@Override
	protected ProcessBuilder createCleanPB() throws CoreException, CommonException {
		IProject project = getProject();
		GoEnvironment goEnv = GoBuildManager.getValidGoEnvironment(project);
		
		String compilerPath = getToolManager().getSDKToolPath(project).toString();
		ArrayList2<String> goBuildCmdLine = ArrayList2.create(compilerPath);
		goBuildCmdLine.addElements("clean", "-i", "-x");
		GoBuildManager.addSourcePackagesToCmdLine(project, goBuildCmdLine, goEnv);
		return goEnv.createProcessBuilder(goBuildCmdLine, getProjectLocation(), true);
	}
	
}