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

import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.core.utils.ResourceUtils;
import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.utilbox.core.CommonException;

public abstract class LangNewProjectWizard extends Wizard 
	implements INewWizard, IExecutableExtension, IPageChangingListener {
	
	public static final String WIZARD_ID = LangUIPlugin.PLUGIN_ID + ".wizards.NewProjectWizard";
	
	protected IWorkbench workbench;
	protected IStructuredSelection selection;
	
	protected IConfigurationElement fConfigElement;
	
	public LangNewProjectWizard() {
		setWindowTitle(WizardMessages.LangNewProject_wizardTitle);
		
		setDialogSettings(LangUIPlugin.getInstance().getDialogSettings());
		
		setNeedsProgressMonitor(true);
	}
	
	public abstract LangProjectWizardFirstPage getFirstPage();
	
	/** @return the second page of the wizard. Can be null if wizard has no second page. */
	public abstract WizardPage getSecondPage();
	
	public IProject getProject() throws CoreException {
		try {
			return getFirstPage().getProjectHandle2();
		} catch(CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	public URI getProjectLocation() {
		return getFirstPage().getProjectLocationUri();
	}
	
	/**
	 * Stores the configuration for the wizard. 
	 * this will be be used in {@link #performFinish()} to possible change the perspective after wizard completion. 
	 */
	@Override
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data) {
		fConfigElement = cfig;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		
		if(selection != null && selection.getFirstElement() instanceof IProject) {
			IProject selectedProject = (IProject) selection.getFirstElement();
			getFirstPage().getNameGroup().getNameField().setFieldValue(selectedProject.getName());
		}
	}
	
	public IWorkbench getWorkbench() {
		return workbench;
	}
	
	public IStructuredSelection getSelection() {
		return selection;
	}
	
	@Override
	public void setContainer(IWizardContainer wizardContainer) {
		super.setContainer(wizardContainer);
		
//		if(wizardContainer != null) {
//			WizardDialog wizardDialog = assertInstance(wizardContainer, WizardDialog.class);
//			wizardDialog.addPageChangingListener(this);
//		}
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public void addPages() {
		super.addPages();
	}
	
	@Override
	public void handlePageChanging(PageChangingEvent event) {
		Object currentPage = event.getCurrentPage();
		Object nextPage = event.getTargetPage();
		
		if(currentPage == getFirstPage() && nextPage == getSecondPage()) {
			event.doit = changeToNewProject();
		} else if(currentPage == getSecondPage() && nextPage == getFirstPage()) {
			removeProject();
		}
	}
	
	protected boolean changeToNewProject() {
		return getProjectCreator().performCreateProject();
	}
	
	public void removeProject() {
		getProjectCreator().revertProjectCreation();
	}
	
	private ProjectCreationOperation projectCreationOperation;
	
	public ProjectCreationOperation getProjectCreator() {
		if(projectCreationOperation == null) {
			projectCreationOperation = createProjectCreator();
		}
		return projectCreationOperation;
	}
	
	protected ProjectCreator_ForWizard createProjectCreator() {
		return new ProjectCreator_ForWizard();
	}
	
	protected class ProjectCreator_ForWizard extends ProjectCreationOperation {
		
		public ProjectCreator_ForWizard() {
			super(LangNewProjectWizard.this.getContainer());
		}
		
		@Override
		public IProject getProject() throws CoreException {
			return LangNewProjectWizard.this.getProject();
		}
		
		@Override
		public URI getProjectLocation() {
			return LangNewProjectWizard.this.getProjectLocation();
		}
		
		@Override
		protected void configureCreatedProject(IProgressMonitor monitor) throws CoreException {
			LangNewProjectWizard.this.configureCreatedProject(this, monitor);
		}
		
		public void createFolder(IContainer container, IProgressMonitor monitor) throws CoreException {
			
			if(container.exists() || !(container instanceof IFolder)) {
				return;
			}
			
			createFolder(container.getParent(), monitor);
			
			IFolder folder = (IFolder) container;
			ResourceUtils.createFolder(folder, true, true, monitor);
			
			revertActions.add(new IRevertAction() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					if(folder.members().length == 0) {
						folder.delete(false, monitor);
					}
				}
			});

		}
		
		public void createFile(IFile file, String contents, boolean openEditor, IProgressMonitor monitor) 
				throws CoreException {
			assertNotNull(contents);
			
			if(file.exists()) {
				return;
			}
			
			createFolder(file.getParent(), monitor);
			
			ResourceUtils.writeStringToFile(file, contents, monitor);
			
			Runnable openEditorRunnable = new Runnable() {
				@Override
				public void run() {
					selectAndReveal(file);
					openEditorOnFile(file);
				}
			};
			
			revertActions.add(new IRevertAction() {
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {
					finishActions.remove(openEditorRunnable);
					file.delete(false, monitor);
				}
			});
			if(openEditor) {
				finishActions.add(openEditorRunnable);
			}
		}

	}
	
	protected abstract void configureCreatedProject(ProjectCreator_ForWizard projectCreator, IProgressMonitor monitor)
			throws CoreException;
	
	/* -----------------  ----------------- */
	
	@Override
	public boolean performFinish() {
		ProjectCreationOperation projectCreator = getProjectCreator();
		boolean success = projectCreator.performCreateProject();
		if(success) {
			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
			selectAndReveal(projectCreator.getCreatedProject());
			projectCreator.performFinishActions();
		}
		return success;
	}
	
	protected void selectAndReveal(IResource newResource) {
		BasicNewResourceWizard.selectAndReveal(newResource, workbench.getActiveWorkbenchWindow());
	}
	
	@Override
	public boolean performCancel() {
		return getProjectCreator().revertProjectCreation();
	}
	
	
	/* ----------------- util: ----------------- */
	
	protected void openEditorOnFile(IFile file) {
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(page, file, true);
		} catch(PartInitException e) {
			LangCore.logInternalError(e);
		}
	}
	
}