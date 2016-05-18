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

import java.util.concurrent.Callable;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCoreMessages;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.lang.tooling.common.ops.ICommonOperation;
import melnorme.lang.tooling.common.ops.IOperationMonitor;
import melnorme.lang.tooling.common.ops.IOperationMonitor.IOperationSubMonitor;
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
	protected final IToolOperationMonitor opMonitor;
	
	public BuildOperationCreator(IProject project, IToolOperationMonitor opMonitor) {
		this.project = project;
		this.opMonitor = assertNotNull(opMonitor);
	}
	
	protected ArrayList2<ICommonOperation> operations;
	
	public ICommonOperation newClearBuildMarkersOperation() {
		return doCreateClearBuildMarkersOperation();
	}
	
	public CompositeBuildOperation newProjectBuildOperation(Collection2<ICommonOperation> buildOps, boolean clearMarkers) 
			throws CommonException {
		operations = ArrayList2.create();
		
		if(buildOps.isEmpty()) {
			return new CompositeBuildOperation(opMonitor, operations, null);
		}
		
		addCompositeBuildOperationMessage();
		
		if(clearMarkers) {
			addOperation(newClearBuildMarkersOperation());
		}
		
		if(buildOps.isEmpty()) {
			addOperation(newMessageOperation( 
				TextMessageUtils.headerSMALL(MSG_NoBuildTargetsEnabled)));
		}
		
		for(ICommonOperation buildOp : buildOps) {
			addOperation(buildOp);
		}
		
		// refresh project
		addOperation(new ICommonOperation() {
			@Override
			public void execute(IOperationMonitor om) throws CommonException, OperationCancellation {
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, EclipseUtils.pm(om));
				} catch(CoreException e) {
					throw LangCore.createCommonException(e);
				}
			}
		});
		
		addOperation(newMessageOperation(headerBIG(MSG_BuildTerminated)));
		
		// Note: the locking rule has to be the whole workspace, because the build might read dependent projects
		// and also error markers can be created globally
		ISchedulingRule rule = ResourceUtils.getWorkspaceRoot();
		return new CompositeBuildOperation(opMonitor, operations, rule);
	}
	
	protected boolean addOperation(ICommonOperation toolOp) {
		return operations.add(toolOp);
	}
	
	protected void addCompositeBuildOperationMessage() throws CommonException {
		String startMsg = headerBIG(format(MSG_BuildingProject, LangCore_Actual.NAME_OF_LANGUAGE, project.getName()));
		addOperation(newMessageOperation(startMsg));
	}
	
	protected ICommonOperation doCreateClearBuildMarkersOperation() {
		return (om) -> {
			boolean hadDeletedMarkers = doDeleteProjectMarkers(buildProblemId, om);
			if(hadDeletedMarkers) {
				opMonitor.writeInfoMessage(
					format(MSG_ClearingMarkers, project.getName()) + "\n");
			}
		};
	}
	
	protected boolean doDeleteProjectMarkers(String markerType, IOperationMonitor parentOM) 
			throws OperationCancellation {
		
		try(IOperationSubMonitor om = parentOM.enterSubTask(LangCoreMessages.BUILD_ClearingProblemMarkers)) {
			
			try {
				IMarker[] findMarkers = project.findMarkers(markerType, true, IResource.DEPTH_INFINITE);
				parentOM.checkCancellation();
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
	
	protected ICommonOperation newMessageOperation(String msg) {
		return new BuildMessageOperation(msg);
	}
	
	protected class BuildMessageOperation implements ICommonOperation, Callable<Void> {
		
		protected final String msg;
		
		public BuildMessageOperation(String msg) {
			this.msg = msg;
		}
		
		@Override
		public void execute(IOperationMonitor monitor) {
			executeDo();
		}
		
		protected void executeDo() {
			call();
		}
		
		@Override
		public Void call() throws RuntimeException {
			opMonitor.writeInfoMessage(msg);
			return null;
		}
	}
	
}