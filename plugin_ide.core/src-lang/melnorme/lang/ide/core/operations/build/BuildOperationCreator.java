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
package melnorme.lang.ide.core.operations.build;

import static java.text.MessageFormat.format;
import static melnorme.lang.ide.core.utils.TextMessageUtils.headerBIG;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.ICoreOperation;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IOperationConsoleHandler;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.utils.ProgressSubTaskHelper;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/** 
 * A one-time {@link BuildOperationCreator} creator (is meant to be used once, immediately)
 */
public class BuildOperationCreator implements BuildManagerMessages {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	protected final String buildProblemId = LangCore_Actual.BUILD_PROBLEM_ID;
	
	protected final IProject project;
	protected final IOperationConsoleHandler opHandler;
	
	public BuildOperationCreator(IProject project, IOperationConsoleHandler opHandler) {
		this.project = project;
		this.opHandler = assertNotNull(opHandler);
	}
	
	protected ArrayList2<ICoreOperation> operations;
	
	public ICoreOperation newClearBuildMarkersOperation() {
		return doCreateClearBuildMarkersOperation();
	}
	
	public ICoreOperation newProjectBuildOperation(Collection2<BuildTarget> targetsToBuild, boolean clearMarkers) 
			throws CommonException {
		operations = ArrayList2.create();
		
		addCompositeBuildOperationMessage();
		
		if(clearMarkers) {
			addOperation(newClearBuildMarkersOperation());
		}
		
		if(targetsToBuild.isEmpty()) {
			addOperation(newMessageOperation( 
				TextMessageUtils.headerSMALL(MSG_NoBuildTargetsEnabled)));
		}
		
		for(BuildTarget buildTarget : targetsToBuild) {
			addOperation(newBuildTargetOperation(project, buildTarget));
		}
		
		// refresh project
		addOperation(new ICoreOperation() {
			@Override
			public void execute(IProgressMonitor pm) throws CoreException, CommonException, OperationCancellation {
				project.refreshLocal(IResource.DEPTH_INFINITE, pm);
			}
		});
		
		addOperation(newMessageOperation(headerBIG(MSG_BuildTerminated)));
		
		// Note: the locking rule has to be the whole workspace, because the build might read dependent projects
		// and also error markers can be created globally
		ISchedulingRule rule = ResourceUtils.getWorkspaceRoot();
		return new CompositeBuildOperation(operations, rule);
	}
	
	protected boolean addOperation(ICoreOperation toolOp) {
		return operations.add(toolOp);
	}
	
	protected void addCompositeBuildOperationMessage() throws CommonException {
		String startMsg = headerBIG(format(MSG_BuildingProject, LangCore_Actual.NAME_OF_LANGUAGE, project.getName()));
		addOperation(newMessageOperation(startMsg));
	}
	
	protected ICoreOperation doCreateClearBuildMarkersOperation() {
		return (pm) -> {
			boolean hadDeletedMarkers = doDeleteProjectMarkers(buildProblemId, pm);
			if(hadDeletedMarkers) {
				opHandler.writeInfoMessage(
					format(MSG_ClearingMarkers, project.getName()) + "\n");
			}
		};
	}
	
	protected boolean doDeleteProjectMarkers(String markerType, IProgressMonitor parentPM) {
		
		try(ProgressSubTaskHelper pm 
				= new ProgressSubTaskHelper(parentPM, LangCoreMessages.BUILD_ClearingProblemMarkers)) {
			
			try {
				IMarker[] findMarkers = project.findMarkers(markerType, true, IResource.DEPTH_INFINITE);
				if(findMarkers.length != 0) {
					project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
					return true;
				}
			} catch (CoreException ce) {
				LangCore.logStatus(ce);
			}
		}
		return false;
	}
	
	protected ICoreOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		Path buildToolPath = LangCore.getToolManager().getSDKToolPath(project);
		try {
			return doCreateBuildTargetOperation(opHandler, buildToolPath, buildTarget);
		} catch(CoreException e) {
			throw new CommonException(e.getMessage(), e.getCause());
		}
	}
	
	public ICoreOperation doCreateBuildTargetOperation(IOperationConsoleHandler opHandler,
			Path buildToolPath, BuildTarget buildTarget
	) throws CommonException, CoreException {
		BuildType buildType = buildTarget.getBuildType();
		return buildType.getBuildOperation(buildTarget, opHandler, buildToolPath);
	}
	
	protected ICoreOperation newMessageOperation(String msg) {
		return new BuildMessageOperation(msg);
	}
	
	protected class BuildMessageOperation implements ICoreOperation, Callable<Void> {
		
		protected final String msg;
		
		public BuildMessageOperation(String msg) {
			this.msg = msg;
		}
		
		@Override
		public void execute(IProgressMonitor monitor) {
			executeDo();
		}
		
		protected void executeDo() {
			call();
		}
		
		@Override
		public Void call() throws RuntimeException {
			opHandler.writeInfoMessage(msg);
			return null;
		}
	}
	
}