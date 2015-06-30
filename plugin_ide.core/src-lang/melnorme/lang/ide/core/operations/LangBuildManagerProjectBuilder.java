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

import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangCore_Actual;
import melnorme.lang.ide.core.project_model.BuildManager;
import melnorme.lang.ide.core.utils.TextMessageUtils;
import melnorme.utilbox.collections.ArrayList2;

public abstract class LangBuildManagerProjectBuilder extends LangProjectBuilder {
	
	protected final BuildManager buildMgr = LangCore.getBuildManager();
	
	public LangBuildManagerProjectBuilder() {
		super();
	}
	
	protected OperationInfo workspaceOpInfo;
	
	@Override
	protected void handleBeginWorkspaceBuild() {
		workspaceOpInfo = new OperationInfo(null, true, 
			TextMessageUtils.headerHASH("Starting " + LangCore_Actual.LANGUAGE_NAME +" build"));
		LangCore.getToolManager().notifyOperationStarted(workspaceOpInfo);
	}
	
	@Override
	protected void handleEndWorkspaceBuild() {
	}
	
	/* ----------------- Build ----------------- */
	
	@Override
	protected IBuildTargetOperation createBuildOp() {
		return getCompositeBuildOperation(getProject(), this);
	}
	
	protected abstract IBuildTargetOperation newBuildOperation(OperationInfo parentOpInfo, IProject project,
			LangProjectBuilder projectBuilder, BuildTarget buildConfig);
	
	public IBuildTargetOperation getCompositeBuildOperation(IProject project, LangProjectBuilder projectBuilder) {
		
		ArrayList2<IBuildTargetOperation> operations = ArrayList2.create();
		
		String startMsg = headerBIG(" Building " + LangCore_Actual.LANGUAGE_NAME + " project: " + project.getName());
		operations.add(newOperationMessageTask(startMsg, false));
		
		for (BuildTarget buildConfig : buildMgr.getBuildTargets(project)) {
			if(buildConfig.isEnabled()) {
				operations.add(newBuildOperation(workspaceOpInfo, project, projectBuilder, buildConfig));
			}
		}
		
		operations.add(newOperationMessageTask(
			headerBIG("Build terminated."), false));
		
		return new CompositeBuildOperation(project, projectBuilder, operations);
	}
	
	protected IBuildTargetOperation newOperationMessageTask(String msg, boolean clearConsole) {
		return new BuildMessageOperation(workspaceOpInfo, clearConsole, msg);
	}
	
	protected class BuildMessageOperation implements IBuildTargetOperation, Callable<OperationInfo> {
		
		protected final OperationInfo parentOperationInfo;
		protected final boolean clearConsole;
		protected final String msg;
		
		private IProject project;
		
		public BuildMessageOperation(OperationInfo workspaceOpInfo, boolean clearConsole, String msg) {
			this.parentOperationInfo = workspaceOpInfo;
			this.clearConsole = clearConsole;
			this.msg = msg;
		}
		
		@Override
		public IProject[] execute(IProject project, int kind, Map<String, String> args, IProgressMonitor monitor) {
			this.project = project;
			executeDo();
			return null;
		}
		
		protected void executeDo() {
			call();
		}
		
		@Override
		public OperationInfo call() throws RuntimeException {
			OperationInfo opInfo = parentOperationInfo.createSubOperation(project, clearConsole, msg);
			LangCore.getToolManager().notifyOperationStarted(opInfo);
			return opInfo;
		}
	}
	
}