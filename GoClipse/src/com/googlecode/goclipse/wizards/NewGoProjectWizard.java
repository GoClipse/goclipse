package com.googlecode.goclipse.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import com.googlecode.goclipse.builder.GoNature;

/**
 * This is a sample new wizard. Its role is to create a new file 
 * resource in the provided container. If the container resource
 * (a folder or a project) is selected in the workspace 
 * when the wizard is opened, it will accept it as the target
 * container. The wizard creates one file with the extension
 * "go". If a sample multi-page editor (also available
 * as a template) is registered for the same extension, it will
 * be able to open it.
 */

public class NewGoProjectWizard extends Wizard implements INewWizard {
	private WizardNewProjectCreationPage page;

	/**
	 * Constructor for NewGoProjectWizard.
	 */
	public NewGoProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() { 
		page = new WizardNewProjectCreationPage("basicNewProjectPage");
		page.setTitle("Project");
		page.setDescription("Create a new project resource.");
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String projectName = page.getProjectName();
		final URI projectURI = ( page.useDefaults() ) ? null : page.getLocationURI();
		
		// Make the operation
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(projectName, projectURI, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		
		// Run it
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */

	private void doFinish(String projectName, URI projectURI, IProgressMonitor monitor)
		throws CoreException {

	    createProject(projectName, projectURI, monitor);
	}
	
	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
//		this.selection = selection;
	}

    /**
     * For this marvelous project we need to:
     * - create the default Eclipse project
     * - add the custom project nature
     * - create the folder structure
     *
     * @param projectName
     * @param location
     * @param monitor 
     * @param natureId
     * @return
     */
    public static IProject createProject(String projectName, URI location, IProgressMonitor monitor) {
    	
        IProject project = createBaseProject(projectName, location);
        try {
        	
        	monitor.setTaskName("Adding nature...");
            addNature(project);
            monitor.worked(1);

        	monitor.setTaskName("Adding directories...");
            String[] paths = { }; //$NON-NLS-1$ //$NON-NLS-2$
            addToProjectStructure(project, paths, monitor);
            monitor.worked(1);

        	monitor.setTaskName("Adding files...");
            String[] files = { "main.go" };
            addToProjectFiles(project, files, monitor);
            monitor.worked(1);
        } catch (CoreException e) {
            e.printStackTrace();
            project = null;
        }

        return project;
    }

    /**
     * Just do the basics: create a basic project.
     *
     * @param location
     * @param projectName
     */
    private static IProject createBaseProject(String projectName, URI location) {
        // it is acceptable to use the ResourcesPlugin class
        IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);

        if (!newProject.exists()) {
            URI projectLocation = location;
            IProjectDescription desc = newProject.getWorkspace().newProjectDescription(newProject.getName());
            if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
                projectLocation = null;
            }

            desc.setLocationURI(projectLocation);
            try {
                newProject.create(desc, null);
                if (!newProject.isOpen()) {
                    newProject.open(null);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }

        return newProject;
    }

    private static void createFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
        IContainer parent = folder.getParent();
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent, monitor);
        }
        if (!folder.exists()) {
            folder.create(false, true, monitor);
        }
    }

    private static void createFile(IFile file, IProgressMonitor monitor) throws CoreException {
        IContainer parent = file.getParent();
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent, monitor);
        }
        if (!file.exists()) {
            file.create(openContentStream(), false, monitor);
        }
    }
    
	private static InputStream openContentStream() {
		String contents = "";

		contents += "// Copyright (C) <YEAR>, <YOUR NAME AND EMAIL>. All rights reserved.\n";
		contents += "\n";
		contents += "package main\n";
		contents += "\n";
		contents += "import (\n";
		contents += ")\n";
		contents += "\n";
		contents += "const (\n";
		contents += ")\n";
		contents += "\n";
		contents += "var (\n";
		contents += ")\n";
		contents += "\n";
		contents += "// Application entry point is always main.main\n";
		contents += "func main() {\n";
		contents += "}\n";
		contents += "\n";
		contents += "// File created with GoClipse";
			
		return new ByteArrayInputStream(contents.getBytes());
	}

    /**
     * Create a folder structure with a parent root, overlay, and a few child
     * folders.
     *
     * @param newProject
     * @param paths
     * @param monitor 
     * @throws CoreException
     */
    private static void addToProjectStructure(IProject newProject, String[] paths, IProgressMonitor monitor) throws CoreException {
        for (String path : paths) {
            IFolder etcFolders = newProject.getFolder(path);
            createFolder(etcFolders, monitor);
        }
    }

    /**
     * Create a folder structure with a parent root, overlay, and a few child
     * folders.
     *
     * @param newProject
     * @param paths
     * @param monitor 
     * @throws CoreException
     */
    private static void addToProjectFiles(IProject newProject, String[] paths, IProgressMonitor monitor) throws CoreException {
        for (String path : paths) {
            IFile etcFile = newProject.getFile(path);
            createFile(etcFile, monitor);
        }
    }

    private static void addNature(IProject project) throws CoreException {
        if (!project.hasNature(GoNature.NATURE_ID)) {
            IProjectDescription description = project.getDescription();
            String[] prevNatures = description.getNatureIds();
            String[] newNatures = new String[prevNatures.length + 1];
            System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
            newNatures[prevNatures.length] = GoNature.NATURE_ID;
            description.setNatureIds(newNatures);

            IProgressMonitor monitor = null;
            project.setDescription(description, monitor);
        }
    }

}
