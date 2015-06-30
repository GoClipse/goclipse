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
package melnorme.lang.ide.core.operations;

import static melnorme.lang.ide.core.utils.TextMessageUtils.headerBIG;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.project_model.BuildManager;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

public abstract class LangBuildManagerProjectBuilder extends LangProjectBuilder {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	
	public LangBuildManagerProjectBuilder() {
		super();
	}
	
	protected OperationInfo workspaceOpInfo;
	
	@Override
	protected void prepareForBuild() throws CoreException {
		workspaceOpInfo = new OperationInfo(null, true, "");
		
		super.prepareForBuild();
	}
	
	@Override
	protected void handleBeginWorkspaceBuild() {
		LangCore.getToolManager().notifyOperationStarted(workspaceOpInfo.createSubOperation(null, false, 
			TextMessageUtils.headerHASH("Starting " + LangCore_Actual.LANGUAGE_NAME +" build")));
	}
	
	@Override
	protected void handleEndWorkspaceBuild() {
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected IBuildTargetOperation createBuildOp() {
		return getCompositeBuildOperation(getProject());
	}
	
	public IBuildTargetOperation getCompositeBuildOperation(IProject project) {
		ArrayList2<IBuildTargetOperation> operations = createBuildOperations(project);
		return new CompositeBuildOperation(project, this, operations);
	}

	protected ArrayList2<IBuildTargetOperation> createBuildOperations(IProject project) {
		ArrayList2<IBuildTargetOperation> operations = ArrayList2.create();
		
		String startMsg = headerBIG(" Building " + LangCore_Actual.LANGUAGE_NAME + " project: " + project.getName());
		operations.add(newOperationMessageTask(project, startMsg, true));
		
		Indexable<BuildTarget> buildTargets = buildMgr.getBuildTargets(project);
		if(buildTargets == null) {
			operations.add(newOperationMessageTask(project, headerBIG("No build targets configured."), false));
			return operations;
		}
		
		for(BuildTarget buildTarget : buildTargets) {
			if(buildTarget.isEnabled()) {
				operations.add(newBuildTargetOperation(workspaceOpInfo, project, buildTarget));
			}
		}
		
		operations.add(newOperationMessageTask(project, headerBIG("Build terminated."), false));
		
		return operations;
	}
	
	protected IBuildTargetOperation newOperationMessageTask(IProject project, String msg, boolean clearConsole) {
		return new BuildMessageOperation(workspaceOpInfo.createSubOperation(project, clearConsole, msg));
	}
	
	protected class BuildMessageOperation implements IBuildTargetOperation, Callable<OperationInfo> {
		
		protected final OperationInfo opInfo;
		
		public BuildMessageOperation(OperationInfo opInfo) {
			this.opInfo = opInfo;
		}
		
		@Override
		public IProject[] execute(IProject project, int kind, Map<String, String> args, IProgressMonitor monitor) {
			executeDo();
			return null;
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
	
	protected abstract CommonBuildTargetOperation newBuildTargetOperation(OperationInfo parentOpInfo, IProject project,
			BuildTarget buildTarget);
	
	public abstract class CommonBuildTargetOperation implements IBuildTargetOperation {
		
		protected final OperationInfo parentOperationInfo;
		protected final BuildTarget buildTarget;
		
		public CommonBuildTargetOperation(OperationInfo parentOperationInfo, BuildTarget buildTarget) {
			this.parentOperationInfo = assertNotNull(parentOperationInfo);
			this.buildTarget = assertNotNull(buildTarget);
		}
		
		protected Path getBuildToolPath() throws CommonException {
			return LangBuildManagerProjectBuilder.this.getBuildToolPath();
		}
		
		protected String getBuildTargetName() {
			return buildTarget.getTargetName();
		}
		
		@Override
		public abstract IProject[] execute(IProject project, int kind, Map<String, String> args,
				IProgressMonitor monitor) throws CoreException, CommonException, OperationCancellation;
						
	}
	
}