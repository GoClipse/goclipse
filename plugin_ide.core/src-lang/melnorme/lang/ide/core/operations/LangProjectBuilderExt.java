/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.operations;


import java.util.Map;

import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class LangProjectBuilderExt extends LangProjectBuilder {
	
	public LangProjectBuilderExt() {
		super();
	}
	
	protected ExternalProcessResult runBuildTool(IProgressMonitor monitor, ProcessBuilder pb) throws CoreException {
		return getToolManager().runTool(getProject(), monitor, pb);
	}
	
	protected ExternalProcessResult runBuildTool(IProgressMonitor monitor, ArrayList2<String> toolCmdLine)
			throws CoreException {
		ProcessBuilder pb = ProcessUtils.createProcessBuilder(toolCmdLine, null);
		return runBuildTool(monitor, pb);
	}
	
	@Override
	protected void handleFirstOfKind() {
//		GoToolManager.getDefault().notifyBuildStarting(null, true);
	}
	
	@Override
	protected void handleLastOfKind() {
//		GoToolManager.getDefault().notifyBuildTerminated(null);
	}
	
	@Override
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		
//		GoToolManager.getDefault().notifyBuildStarting(project, false);
		
		ProcessBuilder pb = createBuildPB();
		
		ExternalProcessResult buildAllResult = runBuildTool(monitor, pb);
		processBuildResult(buildAllResult);
		
		return null;
	}
	
	protected abstract ProcessBuilder createBuildPB() throws CoreException;
	
	protected abstract void processBuildResult(ExternalProcessResult buildAllResult) throws CoreException;
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		deleteProjectBuildMarkers();
		
		ProcessBuilder pb = createCleanPB();
		runBuildTool(monitor, pb);
	}
	
	protected abstract ProcessBuilder createCleanPB() throws CoreException;
	
}