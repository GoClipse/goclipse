/*******************************************************************************
 * Copyright (c) 2015, 2015 IBM Corporation and others.
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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

public class BuildOperationCreator implements BuildManagerMessages {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final OperationInfo parentOpInfo;
	protected final boolean fullBuild;
	
	public BuildOperationCreator(IProject project, OperationInfo parentOpInfo, boolean fullBuild) {
		this.project = project;
		this.fullBuild = fullBuild;
		this.parentOpInfo = assertNotNull(parentOpInfo);
	}
	
	public IBuildTargetOperation newProjectBuildOperation() throws CommonException {
		ProjectBuildInfo buildInfo = buildMgr.getValidBuildInfo(project);
		return newProjectBuildOperation(buildInfo.getEnabledTargets());
	}
	
	protected ArrayList2<IBuildTargetOperation> operations;
	
	public IBuildTargetOperation newProjectBuildOperation(Collection2<BuildTarget> targetsToBuild) 
			throws CommonException {
		operations = ArrayList2.create();
		
		String startMsg = headerBIG(format(MSG_BuildingProject, LangCore_Actual.LANGUAGE_NAME, project.getName()));
		addOperation(newMessageOperation(project, startMsg, true));
		
		addMarkerCleanOperation();
		
		if(targetsToBuild.isEmpty()) {
			addOperation(newMessageOperation(project, 
				TextMessageUtils.headerSMALL(MSG_NoBuildTargetsEnabled), false));
		}
		
		for(BuildTarget buildTarget : targetsToBuild) {
			addOperation(newBuildTargetOperation(project, buildTarget));
		}
		
		
		addOperation(newMessageOperation(project, headerBIG(MSG_BuildTerminated), false));
		
		return new CompositeBuildOperation(operations);
	}
	
	protected boolean addOperation(IBuildTargetOperation buildOp) {
		return operations.add(buildOp);
	}
	
	protected void addMarkerCleanOperation() {
		addOperation((pm) -> deleteProjectMarkers(LangCore_Actual.BUILD_PROBLEM_ID));
	}
	
	protected void deleteProjectMarkers(String markerType) {
		try {
			project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
	}
	
	protected IBuildTargetOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		Path buildToolPath = LangCore.getToolManager().getSDKToolPath();
		return buildMgr.createBuildTargetSubOperation(parentOpInfo, project, buildToolPath, buildTarget, fullBuild);
	}
	
	protected IBuildTargetOperation newMessageOperation(IProject project, String msg, boolean clearConsole) {
		return new BuildMessageOperation(parentOpInfo.createSubOperation(project, clearConsole, msg));
	}
	
	protected class BuildMessageOperation implements IBuildTargetOperation, Callable<OperationInfo> {
		
		protected final OperationInfo opInfo;
		
		public BuildMessageOperation(OperationInfo opInfo) {
			this.opInfo = opInfo;
		}
		
		@Override
		public void execute(IProgressMonitor monitor) {
			executeDo();
		}
		
		protected void executeDo() {
			call();
		}
		
		@Override
		public OperationInfo call() throws RuntimeException {
			LangCore.getToolManager().notifyOperationStarted(opInfo);
			return opInfo;
		}
	}
	
}