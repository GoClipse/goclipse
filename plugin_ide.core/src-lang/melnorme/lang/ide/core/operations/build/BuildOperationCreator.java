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

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.MessageEventInfo;
import melnorme.lang.ide.core.operations.OperationInfo;
import melnorme.lang.ide.core.operations.build.BuildManager.BuildType;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Collection2;
import melnorme.utilbox.core.CommonException;

public class BuildOperationCreator implements BuildManagerMessages {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	protected final String buildProblemId = LangCore_Actual.BUILD_PROBLEM_ID;
	
	protected final IProject project;
	protected final OperationInfo opInfo;
	
	public BuildOperationCreator(IProject project, OperationInfo opInfo) {
		this.project = project;
		this.opInfo = assertNotNull(opInfo);
	}
	
	protected ArrayList2<IToolOperation> operations;
	
	public IToolOperation newClearBuildMarkersOperation() {
		return doCreateClearBuildMarkersOperation();
	}
	
	public IToolOperation newProjectBuildOperation(Collection2<BuildTarget> targetsToBuild, boolean clearMarkers) 
			throws CommonException {
		operations = ArrayList2.create();
		
		addCompositeBuildOperationMessage();
		
		if(clearMarkers) {
			addOperation(newClearBuildMarkersOperation());
		}
		
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
	
	protected boolean addOperation(IToolOperation toolOp) {
		return operations.add(toolOp);
	}
	
	protected void addCompositeBuildOperationMessage() {
		String startMsg = headerBIG(format(MSG_BuildingProject, LangCore_Actual.LANGUAGE_NAME, project.getName()));
		addOperation(newMessageOperation(opInfo, startMsg));
	}
	
	protected IToolOperation doCreateClearBuildMarkersOperation() {
		return (pm) -> {
			boolean hadDeletedMarkers = doDeleteProjectMarkers(buildProblemId, pm);
			if(hadDeletedMarkers) {
				LangCore.getToolManager().notifyMessageEvent(new MessageEventInfo(opInfo, 
					format(MSG_ClearingMarkers, project.getName()) + "\n"));
			}
		};
	}
	
	@SuppressWarnings("unused")
	protected boolean doDeleteProjectMarkers(String markerType, IProgressMonitor pm) {
		try {
			IMarker[] findMarkers = project.findMarkers(markerType, true, IResource.DEPTH_INFINITE);
			if(findMarkers.length != 0) {
				project.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
				return true;
			}
		} catch (CoreException ce) {
			LangCore.logStatus(ce);
		}
		return false;
	}
	
	protected IToolOperation newBuildTargetOperation(IProject project, BuildTarget buildTarget) 
			throws CommonException {
		Path buildToolPath = LangCore.getToolManager().getSDKToolPath();
		try {
			return createBuildTargetOperation(opInfo, project, buildToolPath, buildTarget);
		} catch(CoreException e) {
			throw new CommonException(e.getMessage(), e.getCause());
		}
	}
	
	public CommonBuildTargetOperation createBuildTargetOperation(OperationInfo opInfo,
			IProject project, Path buildToolPath, BuildTarget buildTarget
	) throws CommonException, CoreException {
		ValidatedBuildTarget validatedBuildTarget = buildMgr.getValidatedBuildTarget(project, buildTarget);
		BuildType buildType = validatedBuildTarget.getBuildType();
		return buildType.getBuildOperation(validatedBuildTarget, opInfo, buildToolPath);
	}
	
	protected IToolOperation newMessageOperation(OperationInfo opInfo, String msg) {
		return new BuildMessageOperation(new MessageEventInfo(opInfo, msg));
	}
	
	protected class BuildMessageOperation implements IToolOperation, Callable<MessageEventInfo> {
		
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