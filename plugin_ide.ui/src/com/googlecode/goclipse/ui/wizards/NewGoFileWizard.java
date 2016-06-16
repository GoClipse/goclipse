package com.googlecode.goclipse.ui.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.googlecode.goclipse.ui.wizards.NewSourceFileComposite.SourceFileType;

import melnorme.lang.ide.core.LangCore;

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
public class NewGoFileWizard extends Wizard implements INewWizard {
	private NewGoWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewGoFileWizard.
	 */
	public NewGoFileWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	@Override
    public void addPages() {
		page = new NewGoWizardPage(selection);
		addPage(page);
	}

  /**
   * This method is called when 'Finish' button is pressed in the wizard. We will create an
   * operation and run it using wizard as execution context.
   */
  @Override
  public boolean performFinish() {
    final String containerName = page.getContainerName();
    final String fileName = page.getFileName();
    IRunnableWithProgress op = new IRunnableWithProgress() {
      @Override
      public void run(IProgressMonitor monitor) throws InvocationTargetException {
        try {
          doFinish(containerName, fileName, monitor);
        } catch (CoreException e) {
          throw new InvocationTargetException(e);
        } finally {
          monitor.done();
        }
      }
    };
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
   * The worker method. It will find the container, create the file if missing or just replace its
   * contents, and open the editor on the newly created file.
   */
  private void doFinish(String containerName, String fileName, IProgressMonitor monitor)
      throws CoreException {
    // create a sample file
    monitor.beginTask("Creating " + fileName, 2);
    
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IResource resource = root.findMember(new Path(containerName));
    
    if (!resource.exists() || !(resource instanceof IContainer)) {
      throwCoreException("Container \"" + containerName + "\" does not exist.");
    }
    
    IContainer container = (IContainer) resource;
    
    final IFile file = container.getFile(new Path(fileName));
    InputStream stream = openContentStream(file);
    
    if (file.exists()) {
      file.setContents(stream, true, true, monitor);
    } else {
      file.create(stream, true, monitor);
    }
    
    monitor.worked(1);
    monitor.setTaskName("Opening file for editing...");
    
    getShell().getDisplay().asyncExec(new Runnable() {
      @Override
      public void run() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
          IDE.openEditor(page, file, true);
        } catch (PartInitException e) {
          LangCore.logError("Error opening editor", e);
        }
      }
    });
    
    monitor.worked(1);
  }
	
	/**
	 * We will initialize file contents with a sample text.
	 */
	private InputStream openContentStream(IFile file) {
		SourceFileType type    = page.getSourFileType();
		StringBuilder  sb      = new StringBuilder();
		
		switch (type) {
			case PACKAGE_FILE:
				sb.append("package ").append(file.getParent().getName()).append("\n");
				sb.append("\n");
				sb.append("import (\n\n");
				sb.append(")\n\n");
				break;
			case MAIN_DEFAULT:
				sb.append("package main \n\n");
				sb.append("import (\n\n");
				sb.append(")\n\n");
				sb.append("func main() {\n\n");
				sb.append("}\n\n");
				break;
			case MAIN_WITH_PARAMETERS:
				sb.append("package main \n\n");
				sb.append("import (\n");
				sb.append("    \"flag\"\n");
				sb.append("    \"fmt\"\n");
				sb.append(")\n\n");
				sb.append("const APP_VERSION = \"0.1\"\n\n");
				sb.append("// The flag package provides a default help printer via -h switch\n");
				sb.append("var versionFlag *bool = flag.Bool(\"v\", false, \"Print the version number.\")\n\n");
				sb.append("func main() {\n");
				sb.append("    flag.Parse() // Scan the arguments list \n\n");
				sb.append("    if *versionFlag {\n");
				sb.append("        fmt.Println(\"Version:\", APP_VERSION)\n");
				sb.append("    }\n");
				sb.append("}\n\n");
				break;
			case MAIN_WEBSERVER:
				sb.append("package main \n\n");
				sb.append("import (\n");
				sb.append("    \"net/http\"\n");
				sb.append("    \"fmt\"\n");
				sb.append(")\n\n");
				sb.append("// Default Request Handler\n");
				sb.append("func defaultHandler(w http.ResponseWriter, r *http.Request) {\n");
				sb.append("    fmt.Fprintf(w, \"<h1>Hello %s!</h1>\", r.URL.Path[1:])\n");
				sb.append("}\n\n");
				sb.append("func main() {\n");
				sb.append("    http.HandleFunc(\"/\", defaultHandler)\n");
				sb.append("    http.ListenAndServe(\":8080\", nil)\n");
				sb.append("}\n\n");
				break;
			case TEST:
				sb.append("package ").append(file.getParent().getName()).append("\n");
				sb.append("\n");
				sb.append("import (\n");
				sb.append("    \"testing\"\n");
				sb.append(")\n\n");
				sb.append("func TestXYZ(t *testing.T) {\n\n");
				sb.append("}\n\n");
				break;
		}
		
		return new ByteArrayInputStream(sb.toString().getBytes());
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "goclipse", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
	
}
