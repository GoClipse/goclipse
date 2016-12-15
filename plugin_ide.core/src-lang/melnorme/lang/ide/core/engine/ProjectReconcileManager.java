/*******************************************************************************
 * Copyright (c) 2016 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.core.engine;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;

import melnorme.lang.ide.core.engine.SourceModelManager.StructureUpdateTask;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.IToolOperationMonitor;
import melnorme.lang.ide.core.operations.ILangOperationsListener_Default.ProcessStartKind;
import melnorme.lang.ide.core.operations.build.BuildManager;
import melnorme.lang.ide.core.operations.build.BuildTarget;
import melnorme.lang.tooling.common.ops.IOperationMonitor.NullOperationMonitor;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.CompletableResult.CompletableLatch;
import melnorme.utilbox.concurrency.ICancelMonitor;
import melnorme.utilbox.concurrency.ICommonExecutor;
import melnorme.utilbox.concurrency.MonitorRunnableFuture;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

abstract class AbstractProjectReconcileManager {
	
	protected final ICommonExecutor executor;
	
	protected final Object projectInfosLock = new Object();
	protected final HashMap<IProject, ProjectReconcileTask> projectInfos = new HashMap<>();
	
	public AbstractProjectReconcileManager(ICommonExecutor executor) {
		this.executor = assertNotNull(executor);
	}
	
	public ICommonExecutor getExecutor() {
		return executor;
	}
	
	public void invalidateProjectModel(IProject project, StructureUpdateTask structureUpdateTask, 
			CompletableLatch fileSaveLatch) {
		
		assertNotNull(fileSaveLatch);
		
		synchronized (projectInfosLock) {
			ProjectReconcileTask currentReconcileTask = projectInfos.get(project);
			
			// Cancel the previous task
			if(currentReconcileTask != null) {
				currentReconcileTask.tryCancel(); 
			}
			
			ProjectReconcileTask newReconcileTask = 
					new ProjectReconcileTask(project, currentReconcileTask, structureUpdateTask, fileSaveLatch);
			
			projectInfos.put(project, newReconcileTask);
			
			getExecutor().submitTask(newReconcileTask);
		}
		
	}
	
	public void cancelPendingReconciliation(IProject project) {
		synchronized (projectInfosLock) {
			ProjectReconcileTask currentReconcileTask = projectInfos.get(project);
			
			// Cancel the previous task
			if(currentReconcileTask != null) {
				currentReconcileTask.tryCancel(); 
			}
		}
	}
	
	protected void removeProjectInfo(ProjectReconcileTask projectReconcileTask) {
		synchronized (projectInfosLock) {
			projectInfos.remove(projectReconcileTask.project, projectReconcileTask);
		}
	}
	
	public class ProjectReconcileTask extends MonitorRunnableFuture<Void> {
		
		protected final IProject project;
		protected final ProjectReconcileTask previousReconcileTask;
		protected final StructureUpdateTask structureUpdateTask;
		protected final CompletableLatch fileSaveLatch;
		
		public ProjectReconcileTask(IProject project, ProjectReconcileTask previousReconcileTask, 
				StructureUpdateTask structureUpdateTask, CompletableLatch fileSaveLatch) {
			
			this.project = assertNotNull(project);
			this.previousReconcileTask = previousReconcileTask;
			this.structureUpdateTask = assertNotNull(structureUpdateTask);
			this.fileSaveLatch = assertNotNull(fileSaveLatch);
		}
		
		public void awaitPreconditions() throws OperationCancellation, InterruptedException {
			fileSaveLatch.awaitResult();
			
			if(structureUpdateTask != null) {
				structureUpdateTask.structureInfo.awaitUpdatedData();
			}
		}
		
		@Override
		protected Void internalInvoke() {
			runTask();
			return null;
		}
		
		protected void runTask() {
			try {
				try {
					if(previousReconcileTask != null) {
						previousReconcileTask.awaitPreconditions();
					}
				} catch(OperationCancellation e) {
					// the preconditions of the previous task may have be cancelled, if so continue
				}
				
				awaitPreconditions();
			} catch(InterruptedException | OperationCancellation e) {
				return;
			}
			if(isCancelled()) {
				return;
			}
			doProjectReconcile(project, getCancelMonitor());
			
			removeProjectInfo(this);
		}
		
	}
	
	public abstract void doProjectReconcile(IProject project, ICancelMonitor opMonitor);
	
}

public class ProjectReconcileManager extends AbstractProjectReconcileManager {
	
	protected final BuildManager buildMgr;
	
	protected boolean checkBuildOnSave = false;
	
	public ProjectReconcileManager(ICommonExecutor executor, BuildManager buildMgr) {
		super(executor);
		this.buildMgr = assertNotNull(buildMgr);
	}
	
	@Override
	public void doProjectReconcile(IProject project, ICancelMonitor cm) {
		// We would actually like to clear the console, but due to Eclipse UI bug/limitation
		// clearing the console activates it. :(
		boolean clearConsole = false;
		NullOperationMonitor om = new NullOperationMonitor(cm);
		
		IToolOperationMonitor opMonitor = 
				buildMgr.getToolManager().startNewOperation(ProcessStartKind.CHECK_BUILD, clearConsole, false);
		
		try {
			ArrayList2<BuildTarget> enabledTargets = buildMgr.getValidBuildInfo(project).getEnabledTargets(!true);
			if(!enabledTargets.isEmpty()) {
				buildMgr.requestBuildOperation(opMonitor, project, true, enabledTargets).execute(om);
			}
		} catch(CommonException e) {
			opMonitor.writeInfoMessage("Error during auto-check:\n" + e.getSingleLineRender() + "\n");
		} catch(OperationCancellation e) {
			return;
		}
	}
	
}