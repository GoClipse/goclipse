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
import melnorme.lang.ide.core.operations.MessageEventInfo;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.project_model.ProjectBuildInfo;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

public class BuildOperationCreator implements BuildManagerMessages {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	
	protected final IProject project;
	protected final OperationInfo opInfo;
	protected final boolean fullBuild;
	
	public BuildOperationCreator(IProject project, OperationInfo opInfo, boolean fullBuild) {
		this.project = project;
		this.fullBuild = fullBuild;
		this.opInfo = assertNotNull(opInfo);
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
		addOperation(newMessageOperation(opInfo, startMsg));
		
		addMarkerCleanOperation();
		
		if(targetsToBuild.isEmpty()) {
			addOperation(newMessageOperation(opInfo, 
				TextMessageUtils.headerSMALL(MSG_NoBuildTargetsEnabled)));
		}
		
		for(BuildTarget buildTarget : targetsToBuild) {
			addOperation(newBuildTargetOperation(project, buildTarget));
		}
		
		
		addOperation(newMessageOperation(opInfo, headerBIG(MSG_BuildTerminated)));
		
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
		return buildMgr.createBuildTargetSubOperation(opInfo, project, buildToolPath, buildTarget, fullBuild);
	}
	
	protected IBuildTargetOperation newMessageOperation(OperationInfo opInfo, String msg) {
		return new BuildMessageOperation(new MessageEventInfo(opInfo, msg));
	}
	
	protected class BuildMessageOperation implements IBuildTargetOperation, Callable<MessageEventInfo> {
		
		protected final MessageEventInfo msgInfo;
		
		public BuildMessageOperation(MessageEventInfo msgInfo) {
			this.msgInfo = msgInfo;
		}
		
		@Override
		public void execute(IProgressMonitor monitor) {
			executeDo();
		}
		
		protected void executeDo() {
			call();
		}
		
		@Override
		public MessageEventInfo call() throws RuntimeException {
			LangCore.getToolManager().notifyMessageEvent(msgInfo);
			return msgInfo;
		}
	}
	
}