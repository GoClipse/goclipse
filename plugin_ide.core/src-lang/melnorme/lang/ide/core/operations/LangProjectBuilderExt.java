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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.utils.ProcessUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class LangProjectBuilderExt extends LangProjectBuilder {
	
	public LangProjectBuilderExt() {
		super();
	}
	
	protected ExternalProcessResult runBuildTool_2(IProgressMonitor monitor, ProcessBuilder pb) 
			throws CommonException, OperationCancellation {
		return getToolManager().newRunToolTask(pb, getProject(), monitor).runProcess();
	}
	
	protected ExternalProcessResult runBuildTool(IProgressMonitor monitor, ArrayList2<String> toolCmdLine)
			throws CoreException, CommonException, OperationCancellation {
		ProcessBuilder pb = createProcessBuilder(toolCmdLine);
		return runBuildTool_2(monitor, pb);
	}
	
	protected ProcessBuilder createProcessBuilder(ArrayList2<String> toolCmdLine) throws CoreException {
		return ProcessUtils.createProcessBuilder(toolCmdLine, getProjectLocation());
	}
	
	protected Location getProjectLocation() throws CoreException {
		return ResourceUtils.getProjectLocation(getProject());
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected void handleFirstOfKind() {
		LangCore.getToolManager().notifyBuildStarting(null, true);
	}
	
	@Override
	protected void handleLastOfKind() {
		LangCore.getToolManager().notifyBuildTerminated(null);
	}
	
	@Override
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException, OperationCancellation {
		return createBuildOp().execute(project, monitor);
	}
	
	protected abstract AbstractRunBuildOperation createBuildOp();
	
	public abstract class AbstractRunBuildOperation {
		
		public IProject[] execute(IProject project, IProgressMonitor monitor) 
				throws CoreException, OperationCancellation {
			LangCore.getToolManager().notifyBuildStarting(project, false);
			
			try {
				ProcessBuilder pb = createBuildPB();
				
				ExternalProcessResult buildAllResult = runBuildTool_2(monitor, pb);
				doBuild_processBuildResult(buildAllResult);
			} catch (CommonException ce) {
				throw LangCore.createCoreException(ce);
			}
			
			return null;
		}
		
		protected abstract ProcessBuilder createBuildPB() throws CoreException, CommonException;
		
		protected abstract void doBuild_processBuildResult(ExternalProcessResult buildAllResult) 
				throws CoreException, CommonException;
		
	}
	
	/* ----------------- Clean ----------------- */
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		deleteProjectBuildMarkers();
		
		try {
			ProcessBuilder pb = createCleanPB();
			EclipseUtils.checkMonitorCancelation(monitor);
			doClean(monitor, pb);
		} catch (OperationCancellation e) {
			// return
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected abstract ProcessBuilder createCleanPB() throws CoreException, CommonException;
	
	protected void doClean(IProgressMonitor monitor, ProcessBuilder pb) 
			throws CoreException, CommonException, OperationCancellation {
		runBuildTool_2(monitor, pb);
	}
	
}