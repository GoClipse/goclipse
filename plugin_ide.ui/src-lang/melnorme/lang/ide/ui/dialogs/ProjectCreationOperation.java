/*******************************************************************************
 * Copyright (c) 2015, 2015 Bruno Medeiros and other Contributors.
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

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.LangNature;
import melnorme.lang.ide.core.utils.EclipseUtils;
import melnorme.lang.ide.ui.utils.UIOperationsHelper;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * Component to create (and removed) new Lang projects, usually used by New Project wizards.
 */
public abstract class ProjectCreationOperation {
	
	protected final IRunnableContext context;
	
	protected final ArrayList2<IRevertAction> revertActions = new ArrayList2<>();
	protected final ArrayList2<Runnable> finishActions = new ArrayList2<>();
	
	public static interface IRevertAction {
		
		public void run(IProgressMonitor monitor) throws CoreException;
		
	}
	
	public ProjectCreationOperation(IRunnableContext context) {
		this.context = context;
	}
	
	protected IRunnableContext getRunnableContext() {
		return context;
	}
	
	public abstract IProject getProject();
	
	public abstract URI getProjectLocation();
	
	protected boolean runOperation(WorkspaceModifyOperation op, boolean isCancellable, String errorTitle) {
		return UIOperationsHelper.runAndHandle(getRunnableContext(), op, isCancellable, errorTitle);
	}
	
	public boolean performCreateProject() {
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {
				doCreateProject(monitor);
			}
		};
		return runOperation(op, true, WizardMessages.LangNewProject_createProjectError_title);
	}
	
	protected void doCreateProject(IProgressMonitor monitor) throws CoreException {
		if(getProject() == null) {
			throw LangCore.createCoreException("No project name specified.", null);
		}
		
		if(!getProject().exists()) {
			String projectName = getProject().getName();
			EclipseUtils.createAndOpenProject(projectName, getProjectLocation(), false, monitor);
			
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
		configureCreatedProject(monitor);
	}
	
	/**
	 * Configure created Lang project. Don't forget to add revert actions.
	 */
	protected abstract void configureCreatedProject(IProgressMonitor monitor) throws CoreException;
	
	public boolean revertProjectCreation() {
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException {
				doRevertProjectCreation(monitor);
			}
		};
		return runOperation(op, false, WizardMessages.LangNewProject_removeProjectError_title);
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