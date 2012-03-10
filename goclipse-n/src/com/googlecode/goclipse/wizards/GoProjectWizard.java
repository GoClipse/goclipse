package com.googlecode.goclipse.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.builder.GoNature;
import com.googlecode.goclipse.perspective.GoPerspective;

/**
 * 
 * @author steel
 */
@SuppressWarnings("restriction")
public class GoProjectWizard extends Wizard implements INewWizard, IWizard {

	private IWorkbench 			workbench;
	private GoProjectWizardPage page;
	private ISelection 			selection;

	/**
	 * Constructor for NewGoFileWizard.
	 */
	public GoProjectWizard() {
		setWindowTitle("New Go Project");
		setNeedsProgressMonitor(true);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		page = new GoProjectWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String projectName = page.getProjectComposite().getNameField()
				.getText().trim();

		if (!(projectName.length() > 0)) {
			return false;
		}

		CreateProjectOperation operation = new CreateProjectOperation(
				projectName);

		try {
			getContainer().run(false, false, operation);
		} catch (InvocationTargetException e) {
			Activator.logError(e);

			return false;
		} catch (InterruptedException e) {
			return false;
		}

		IStatus status = operation.getResult();

		if (status.isOK()) {
			switchToGoPerspective();

			BasicNewResourceWizard.selectAndReveal(operation.getProject(),
					workbench.getActiveWorkbenchWindow());

			return true;
		} else {
			ErrorDialog.openError(getShell(), "Error Creating Project", status.getMessage(), status);
			return false;
		}
	}

	/**
	 * 
	 */
	private void switchToGoPerspective() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();

		IPerspectiveRegistry reg = WorkbenchPlugin.getDefault()
				.getPerspectiveRegistry();
		PerspectiveDescriptor rtPerspectiveDesc = (PerspectiveDescriptor) reg
				.findPerspectiveWithId(GoPerspective.ID);

		// Now set it as the active perspective.
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			page.setPerspective(rtPerspectiveDesc);
		}
	}

	/**
	 * 
	 */
	private static class CreateProjectOperation extends	WorkspaceModifyOperation {
		private String projectName;
		private IStatus result;
		private IProject project;

		public CreateProjectOperation(String projectName) {
			this.projectName = projectName;
		}

		@Override
		protected void execute(IProgressMonitor monitor) throws CoreException,
				InvocationTargetException, InterruptedException {
			try {
				createProject(monitor);

				result = Status.OK_STATUS;
			} catch (CoreException ce) {
				result = ce.getStatus();
			}
		}

		void createProject(IProgressMonitor monitor) throws CoreException {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(projectName);
			project.create(new NullProgressMonitor());
			project.open(new NullProgressMonitor());

			this.project = project;

			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = GoNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);

			IFolder srcFolder = project.getFolder("src");
			createFolder(srcFolder);

			Environment.INSTANCE.setSourceFolders(project, new String[] {"src"});

			IFolder binFolder = project.getFolder(Environment.INSTANCE.getBinOutputFolder(project));
			createFolder(binFolder);

			IFolder pkgFolder = project.getFolder(Environment.INSTANCE.getPkgOutputFolder(project));
			createFolder(pkgFolder);

			Environment.INSTANCE.setBinOutputFolder(project, binFolder.getProjectRelativePath());
			Environment.INSTANCE.setPkgOutputFolder(project, pkgFolder.getProjectRelativePath());
		}

		private void createFolder(IFolder folder) throws CoreException {
			if (!folder.exists()) {
				if (folder.getParent() instanceof IFolder) {
					createFolder((IFolder) folder.getParent());
				}

				folder.create(false, true, new NullProgressMonitor());
			}
		}

		public IProject getProject() {
			return project;
		}

		public IStatus getResult() {
			return result;
		}
	}
}
