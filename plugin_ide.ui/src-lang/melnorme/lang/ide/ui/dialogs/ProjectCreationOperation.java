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
package melnorme.lang.ide.ui.dialogs;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Display;

import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.core.utils.ResourceUtils.CoreOperation;
import melnorme.lang.ide.ui.utils.UIOperationsStatusHandler;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;

/**
 * Component to create (and removed) new Lang projects, usually used by New Project wizards.
 */
public abstract class ProjectCreationOperation {
	
	public static interface IRevertAction {
		
		public void run(IProgressMonitor monitor) throws CoreException;
		
	}
	
	protected final IRunnableContext context;
	
	protected final ArrayList2<IRevertAction> revertActions = new ArrayList2<>();
	protected final ArrayList2<Runnable> finishActions = new ArrayList2<>();
	
	protected IProject createdProject;
	
	public ProjectCreationOperation(IRunnableContext context) {
		this.context = context;
	}
	
	protected IRunnableContext getRunnableContext() {
		return context;
	}
	
	public IProject getCreatedProject() {
		return createdProject;
	}
	
	public abstract IProject getProject() throws CoreException;
	
	public abstract IPath getProjectLocation2();
	
	protected boolean runOperation(boolean isCancellabe, String errorTitle, CoreOperation operation) {
		try {
			ResourceUtils.runOperationInWorkspace(getRunnableContext(), isCancellabe, operation);
			return true;
		} catch(OperationCancellation e) {
			return false;
		} catch(CommonException e) {
			UIOperationsStatusHandler.handleStatus(true, errorTitle, e);
		}
		return false;
	}
	
	public boolean performCreateProject() {
		boolean success = runOperation(true, 
			WizardMessages.LangNewProject_createProjectError_title, this::doCreateProject);
		
		if(!success) {
			revertProjectCreation();
		}
		return success;
	}
	
	protected void doCreateProject(IProgressMonitor monitor) 
			throws CoreException, CommonException, OperationCancellation {
		createdProject = null;
		
		if(getProject() == null) {
			throw new CommonException("No project name specified.");
		}
		
		if(!getProject().exists()) {
			String projectName = getProject().getName();
			ResourceUtils.createAndOpenProject(projectName, getProjectLocation2(), false, monitor);
			
			revertActions.add(new IRevertAction() {
				@Override
				public void run(IProgressMonitor pm) throws CoreException {
					getProject().delete(true, pm);
				}
			});
		}
		if(!getProject().hasNature(LangNature.NATURE_ID)) {
			EclipseUtils.addNature(getProject(), LangNature.NATURE_ID);
			
			revertActions.add(new IRevertAction() {
				@Override
				public void run(IProgressMonitor pm) throws CoreException {
					EclipseUtils.removeNature(getProject(), LangNature.NATURE_ID);
				}
			});
		}
		createdProject = getProject();
		configureCreatedProject(monitor);
		ResourceUtils.refresh(getProject(), monitor);
	}
	
	/**
	 * Configure created Lang project. Don't forget to add revert actions.
	 */
	protected abstract void configureCreatedProject(IProgressMonitor monitor) 
			throws CommonException, OperationCancellation, CoreException;
	
	public boolean revertProjectCreation() {
		return runOperation(true, WizardMessages.LangNewProject_removeProjectError_title, 
			this::doRevertProjectCreation);
	}
	
	protected void doRevertProjectCreation(IProgressMonitor monitor) throws CoreException {
		ArrayList<IRevertAction> revertActionsToRun = new ArrayList<>(revertActions);
		
		revertActions.clear(); // Clear this before running any code that can throw exceptions.
		
		Collections.reverse(revertActionsToRun); // Run in reverse order.
		
		for(IRevertAction revertAction : revertActionsToRun) {
			revertAction.run(monitor);
		}
	}
	
	
	public void performFinishActions() {
		assertTrue(Display.getCurrent() != null);
		// Runs in UI thread.
		for(Runnable runnable : finishActions) {
			runnable.run();
		}
		finishActions.clear();
	}
	
}