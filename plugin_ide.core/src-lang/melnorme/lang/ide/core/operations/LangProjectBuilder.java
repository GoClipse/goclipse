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


import static melnorme.lang.ide.core.operations.build.BuildManagerMessages.MSG_Starting_LANG_Build;
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
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.IBuildTargetOperation;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.Location;

public abstract class LangProjectBuilder extends IncrementalProjectBuilder {
	
	protected final BuildManager buildManager = LangCore.getBuildManager();
	
	public LangProjectBuilder() {
	}
	
	protected Location getProjectLocation() throws CoreException {
		return ResourceUtils.getProjectLocation(getProject());
	}
	
	protected AbstractToolManager getToolManager() {
		return LangCore.getToolManager();
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
		for (IBuildConfiguration buildConfig : getContext().getAllReferencedBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				return false;
			}
		}
		return true;
	}
	
	protected boolean isLastProjectOfKind() throws CoreException {
		for (IBuildConfiguration buildConfig : getContext().getAllReferencingBuildConfigs()) {
			if(buildConfig.getProject().hasNature(LangCore.NATURE_ID)) {
				return false;
			}
		}
		return true;
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected void startupOnInitialize() {
		assertTrue(getProject() != null);
	}
	
	protected static OperationInfo workspaceOpInfo;
	
	protected void prepareForBuild() throws CoreException {
		if(isFirstProjectOfKind()) {
			handleBeginWorkspaceBuild();
		} else {
			assertTrue(workspaceOpInfo != null && workspaceOpInfo.isStarted());
		}
	}
	
	protected void handleBeginWorkspaceBuild() {
		workspaceOpInfo = getToolManager().startNewToolOperation();
		
		getToolManager().notifyMessageEvent(new MessageEventInfo(workspaceOpInfo, 
			headerVeryBig(MessageFormat.format(MSG_Starting_LANG_Build, LangCore_Actual.LANGUAGE_NAME))));
	}
	
	protected void handleEndWorkspaceBuild2() {
		workspaceOpInfo = null;
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
				handleEndWorkspaceBuild2();
			}
		}
		
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
		return buildManager.newProjectBuildOperation(workspaceOpInfo, getProject(), fullBuild);
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
	
	protected void doClean(IProgressMonitor pm, ProcessBuilder pb) 
			throws CoreException, CommonException, OperationCancellation {
		getToolManager().newRunToolOperation2(pb, pm).runProcess();
	}
	
}