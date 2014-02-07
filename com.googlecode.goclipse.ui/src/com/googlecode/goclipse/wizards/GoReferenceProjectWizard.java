package com.googlecode.goclipse.wizards;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.googlecode.goclipse.Activator;

/**
 * This wizard creates a new Go Reference project - a project with a src folder linked to the
 * $GOROOT/src directory.
 */
public class GoReferenceProjectWizard extends Wizard implements INewWizard {
	private GoReferenceProjectWizardPage wizardPage;

	/**
	 * Create a new GoReferenceProjectWizard.
	 */
	public GoReferenceProjectWizard() {
		setWindowTitle("New Go Reference Project");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public void addPages() {
		wizardPage = new GoReferenceProjectWizardPage();
		addPage(wizardPage);
	}

	@Override
	public boolean performFinish() {
		final File srcFolder = wizardPage.getGoRootSrcFolder();

		try {
			getContainer().run(false, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					createProject(srcFolder, monitor);
				}
			});
		} catch (InterruptedException ie) {
			// ignore

		} catch (InvocationTargetException e) {
			if (!(e.getTargetException() instanceof OperationCanceledException)) {
				ErrorDialog.openError(getShell(), "Error Creating Project", e.getMessage(),
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e));
			}
		}

		return true;
	}

	protected void createProject(File srcFolder, IProgressMonitor monitor)
		throws InvocationTargetException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IProject project = getWellNamedProject(root);

		SubMonitor m = SubMonitor.convert(monitor, "Creating reference project", 100);

		try {
			project.create(m.newChild(10));
			project.open(m.newChild(10));

			IFolder folder = project.getFolder("src");

			folder.createLink(Path.fromOSString(srcFolder.getPath()), IResource.NONE,
				m.newChild(80));
		} catch (CoreException ce) {
			throw new InvocationTargetException(ce);
		} finally {
			monitor.done();
		}
	}

	private IProject getWellNamedProject(IWorkspaceRoot root) {
		String name = "GOROOT";

		IProject project = root.getProject(name);

		int i = 1;

		while (project.exists()) {
			i++;

			project = root.getProject(name + i);
		}

		return project;
	}

}
