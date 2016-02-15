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
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.osgi.service.prefs.BackingStoreException;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.operation.OperationUtils;
import melnorme.lang.tooling.data.StatusLevel;
import melnorme.utilbox.collections.HashMap2;
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
	
	protected static HashMap2<String, IOperationConsoleHandler> workspaceOpHandlerMap = new HashMap2<>();
	protected IOperationConsoleHandler workspaceOpHandler;
	
	protected void prepareForBuild(IProgressMonitor pm) throws CoreException, OperationCancellation {
		handleBeginWorkspaceBuild(pm);
	}
	
	protected void handleBeginWorkspaceBuild(IProgressMonitor pm) throws CoreException, OperationCancellation {
		workspaceOpHandler = workspaceOpHandlerMap.get(LangCore.NATURE_ID);
		
		if(workspaceOpHandler != null) {
			return;
		}
		workspaceOpHandler = getToolManager().startNewBuildOperation();
		workspaceOpHandlerMap.put(LangCore.NATURE_ID, workspaceOpHandler);
		
		ResourceUtils.getWorkspace().addResourceChangeListener(new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				int type = event.getType();
				if(type == IResourceChangeEvent.POST_BUILD || type == IResourceChangeEvent.PRE_BUILD) {
					workspaceOpHandler = null;
					workspaceOpHandlerMap.remove(LangCore.NATURE_ID);
					ResourceUtils.getWorkspace().removeResourceChangeListener(this);
				}
			}
		}, IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.PRE_BUILD);
		
		workspaceOpHandler.writeInfoMessage(
			headerVeryBig(MessageFormat.format(MSG_Starting_LANG_Build, LangCore_Actual.LANGUAGE_NAME))
		);
		
		clearWorkspaceErrorMarkers(pm);
	}
	
	protected void clearWorkspaceErrorMarkers(IProgressMonitor pm) throws CoreException, OperationCancellation {
		clearErrorMarkers(getProject(), pm);
		
		for(IBuildConfiguration buildConfig : getContext().getAllReferencingBuildConfigs()) {
			clearErrorMarkers(buildConfig.getProject(), pm);
		}
	}
	
	protected void clearErrorMarkers(IProject project, IProgressMonitor pm) 
			throws CoreException, OperationCancellation {
		ICoreOperation clearMarkersOp = buildManager.newProjectClearMarkersOperation(workspaceOpHandler, project);
		try {
			clearMarkersOp.execute(pm);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected void handleEndWorkspaceBuild2() {
		workspaceOpHandler = null;
	}
	
	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {
		assertTrue(kind != CLEAN_BUILD);
		
		if(kind == AUTO_BUILD && !isAutoBuildSupported()
				&& !ToolchainPreferences.PROJ_AUTO_BUILD_DISABLED.getStoredValue(getProject())
		) {
			try {
				LangNature.disableAutoBuildMode(getProject(), getCommand().getBuilderName(), monitor);
			} catch(CoreException e) {
				CommonException ce = CommonException.fromMsgFormat(
					"Error, could not disable AutoBuild for project `{0}`.\n"
					+ "The project description is out-dated or invalid, "
					+ "please remove the project from the workspace and re-add it." , getProject().getName());
				getToolManager().notifyMessage(StatusLevel.ERROR, "Error", ce.getMessage());
				throw e;
			}
			
			try {
				ToolchainPreferences.PROJ_AUTO_BUILD_DISABLED.setValue(getProject(), true);
			} catch(BackingStoreException e) {
				// Ignore
			}
			
			return null;
		}
		
		IProject project = assertNotNull(getProject());
		
		try {
			prepareForBuild(monitor);
			
			return doBuild(project, kind, args, monitor);
		} 
		catch(OperationCancellation cancel) {
			forgetLastBuiltState();
			return null;
		} catch(CoreException ce) {
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
	
	protected boolean isAutoBuildSupported() {
		return false;
	}
	
	@SuppressWarnings("unused")
	protected IProject[] doBuild(final IProject project, int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException, OperationCancellation {
		try {
			createBuildOp().execute(monitor);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
		return null;
	}
	
	protected ICoreOperation createBuildOp() throws CommonException {
		return buildManager.newProjectBuildOperation(workspaceOpHandler, getProject(), false);
	}
	
	/* ----------------- Clean ----------------- */
	
	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		deleteProjectBuildMarkers();
		
		try {
			ProcessBuilder pb = createCleanPB();
			OperationUtils.checkMonitorCancelation(monitor);
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
		getToolManager().newRunBuildToolOperation(pb, pm).runProcess();
	}
	
}