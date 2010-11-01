package com.googlecode.goclipse.wizards;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;

import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;
import com.googlecode.goclipse.perspectives.GoPerspective;

/**
 * 
 * @author steel
 */
public class NewProjectWizard extends Wizard implements INewWizard, IWizard {
	private NewProjectWizardPage page;
	private ISelection           selection;
	private ProjectComposite     projectComposite;

	/**
	 * Constructor for NewGoFileWizard.
	 */
	public NewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);

	}

	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewProjectWizardPage(selection);
		addPage(page);
		projectComposite = page.getProjectComposite();
	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We
	 * will create an operation and run it using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		final String projectname = page.getProjectComposite().getNameField()
				.getText().trim();

		if (!(projectname.length() > 0)) {
			return false;
		}

		getShell().getDisplay().asyncExec(new Runnable() {
			@SuppressWarnings("restriction")
			public void run() {

				try {
					IProgressMonitor progressMonitor = new NullProgressMonitor();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
							.getRoot();
					IProject project = root.getProject(projectname);
					project.create(progressMonitor);
					project.open(progressMonitor);

					IProjectDescription description = project.getDescription();
					String[] natures = description.getNatureIds();
					String[] newNatures = new String[natures.length + 1];
					System.arraycopy(natures, 0, newNatures, 0, natures.length);
					newNatures[natures.length] = "goclipse.goNature";
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
					
					// Check the active window first
					final IWorkbench workBench = PlatformUI.getWorkbench();
					Display display = workBench.getDisplay();

					IWorkbenchPage[] pages = null;
					IWorkbenchWindow window = workBench.getActiveWorkbenchWindow();
					String rtPerspectiveId = GoPerspective.ID;

					// Get the perspective 'X's descriptor.
					IPerspectiveRegistry reg = WorkbenchPlugin.getDefault().getPerspectiveRegistry();
					PerspectiveDescriptor rtPerspectiveDesc =(PerspectiveDescriptor) reg.findPerspectiveWithId(rtPerspectiveId);

					// Now set it as the active perspective.
					// Set the perspective
					if (window != null)
					{
					   IWorkbenchPage page = window.getActivePage();
					   page.setPerspective(rtPerspectiveDesc);
					    			   	
					   // Do again
					   window = workBench.getActiveWorkbenchWindow();
					}
					
					IFolder srcPkgFolder = project.getFolder("src/pkg");
					srcPkgFolder.getRawLocation().toFile().mkdirs();

					IFolder srcCmdFolder = project.getFolder("src/cmd");
					srcCmdFolder.getRawLocation().toFile().mkdirs();

					Environment.INSTANCE.setSourceFolders(project, new String[]{"src/pkg", "src/cmd"});
					
					/**
					 * 
					 */
					IFolder binFolder = project.getFolder(Environment.INSTANCE.getBinOutputFolder(project));
					binFolder.getRawLocation().toFile().mkdirs();
					
					IFolder pkgFolder = project.getFolder(Environment.INSTANCE.getPkgOutputFolder(project));
					pkgFolder.getRawLocation().toFile().mkdirs();
               
	               Environment.INSTANCE.setBinOutputFolder(project, binFolder.getProjectRelativePath());
	               Environment.INSTANCE.setPkgOutputFolder(project, pkgFolder.getProjectRelativePath());
					
				} catch (CoreException e) {
				   SysUtils.debug(e);
				}
			}
		});

		final String containerName = page.getContainerName();
		// final String fileName = page.getFileName();
		// IRunnableWithProgress op = new IRunnableWithProgress() {
		// public void run(IProgressMonitor monitor) throws
		// InvocationTargetException {
		// // try {
		// // doFinish(containerName, fileName, monitor);
		// // } catch (CoreException e) {
		// // throw new InvocationTargetException(e);
		// // } finally {
		// // monitor.done();
		// // }
		// }
		// };

		// try {
		// getContainer().run(true, false, op);
		// }
		// catch (InterruptedException e) {
		// return false;
		// }
		// catch (InvocationTargetException e) {
		// Throwable realException = e.getTargetException();
		// MessageDialog.openError(getShell(), "Error",
		// realException.getMessage());
		// return false;
		// }
		return true;
	}

	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 */

	private void doFinish(String containerName, String fileName,
			IProgressMonitor monitor) throws CoreException {
		// create a sample file
		monitor.beginTask("Creating " + fileName, 2);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName
					+ "\" does not exist.");
		}

		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));

		try {
			InputStream stream = openContentStream();
			if (file.exists()) {
				file.setContents(stream, true, true, monitor);
			} else {
				file.create(stream, true, monitor);
			}
			stream.close();
		} catch (IOException e) {
		   SysUtils.debug(e);
		}

		monitor.worked(1);
		monitor.setTaskName("Creating Project...");
		getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				String projectname = "default";

				String projectTemplate = "<?xml version=\"1.0\"  encoding=\"UTF-8\" ?>\n"
						+ "<projectDescription>\n"
						+ "    <name>"
						+ projectname
						+ "</name>\n"
						+ "    <projects>\n"
						+ "        ...\n"
						+ "    </projects>\n"
						+ "    <buildSpec>\n"
						+ "        <buildCommand>\n"
						+ "            <name>com.googlecode.goclipse.builder.GoBuilder</name>\n"
						+ "            <arguments> </arguments>\n"
						+ "        </buildCommand>\n"
						+ "    </buildSpec>\n"
						+ "    <natures>\n"
						+ "        <nature>com.googlecode.goclipse.builder.GoNature</nature>\n"
						+ "    </natures>\n" + "</projectDescription>";

			}
		});
		monitor.worked(1);
	}

	/**
	 * We will initialize file contents with a sample text.
	 */

	private InputStream openContentStream() {
		String contents = "This is the initial file contents for *.go file that should be word-sorted in the Preview page of the multi-page editor";
		return new ByteArrayInputStream(contents.getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "goclipse", IStatus.OK,
				message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize
	 * from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
