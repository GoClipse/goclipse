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
package melnorme.lang.ide.core.operations;


import static melnorme.lang.ide.core.project_model.BuildManagerMessages.MSG_Starting_LANG_Build;
import static melnorme.lang.ide.core.utils.TextMessageUtils.headerVeryBig;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.text.MessageFormat;
import java.util.Map;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class LangProjectBuilder extends IncrementalProjectBuilder {
	
	public LangProjectBuilder() {
	}
	
	protected Location getProjectLocation() throws CoreException {
		return ResourceUtils.getProjectLocation(getProject());
	}
	
	/* ----------------- helpers ----------------- */
	
	protected void deleteProjectBuildMarkers() {
		try {
			getProject().deleteMarkers(LangCore_Actual.BUILD_PROBLEM_ID, true, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
	}
	
	protected String getBuildProblemId() {
		return LangCore_Actual.BUILD_PROBLEM_ID;
	}
	
	protected boolean isFirstProjectOfKind() throws CoreException {
		boolean firstOfKind = true;
		for (IBuildConfiguration buildConfig : getContext().getAllReferencedBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				firstOfKind = false;
			}
		}
		return firstOfKind;
	}
	
	protected boolean isLastProjectOfKind() throws CoreException {
		boolean lastOfKind = true;
		for (IBuildConfiguration buildConfig : getContext().getAllReferencingBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				lastOfKind = false;
			}
		}
		return lastOfKind;
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected void startupOnInitialize() {
		assertTrue(getProject() != null);
	}
	
	protected OperationInfo workspaceOpInfo;
	
	protected void prepareForBuild() throws CoreException {
		workspaceOpInfo = new OperationInfo(null, true, "");
		
		if(isFirstProjectOfKind()) {
			handleBeginWorkspaceBuild();
		}
		
		deleteProjectBuildMarkers();
	}
	
	protected void handleBeginWorkspaceBuild() {
		LangCore.getToolManager().notifyOperationStarted(workspaceOpInfo.createSubOperation(null, false, 
			headerVeryBig(MessageFormat.format(MSG_Starting_LANG_Build, LangCore_Actual.LANGUAGE_NAME))));
	}
	
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		assertTrue(kind != CLEAN_BUILD);
		
		IProject project = assertNotNull(getProject());
		
		prepareForBuild();
		
		try {
			return doBuild(project, kind, args, monitor);
		} 
		catch (OperationCancellation cancel) {
			forgetLastBuiltState();
			return null;
		} catch (CoreException ce) {
			forgetLastBuiltState();
			
			if(monitor.isCanceled()) {
				// This shouldn't usually happen, a OperationCancellation should have been thrown,
				// but sometimes its not wrapped correctly.
				return null;
			}
			LangCore.logStatus(ce);
			throw ce;
		}
		finally {
			getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
			
			if(isLastProjectOfKind()) {
				handleEndWorkspaceBuild();
			}
		}
		
	}
	
	protected void handleEndWorkspaceBuild() {
	}
	
	@SuppressWarnings("unused")
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException, OperationCancellation {
		try {
			createBuildOp(kind == IncrementalProjectBuilder.FULL_BUILD).execute(monitor);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
		return null;
	}
	
	protected IBuildTargetOperation createBuildOp(boolean fullBuild) throws CommonException {
		return createBuildOperationCreator(fullBuild).getBuildOperation();
	}
	
	protected BuildOperationCreator createBuildOperationCreator(boolean fullBuild) {
		return new BuildOperationCreator(getProject(), workspaceOpInfo, fullBuild);
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
		
		new AbstractRunToolOperation(getProject()) {
			@Override
			protected ProcessBuilder createToolProcessBuilder() throws CoreException, CommonException {
				return pb;
			}
		}.execute(monitor);
	}
	
	public static abstract class AbstractRunToolOperation extends AbstractToolManagerOperation {
		
		public AbstractRunToolOperation(IProject project) {
			super(project);
		}
		
		@Override
		public void execute(IProgressMonitor monitor) 
				throws CoreException, CommonException, OperationCancellation {
			
			ProcessBuilder pb = createToolProcessBuilder();
			ExternalProcessResult toolResult = runBuildTool(monitor, pb);
			processToolResult(toolResult);
		}
		
		protected abstract ProcessBuilder createToolProcessBuilder() throws CoreException, CommonException;
		
		@SuppressWarnings("unused") 
		protected void processToolResult(ExternalProcessResult buildAllResult) 
				throws CoreException, CommonException {
			
		}
		
	}
	
}