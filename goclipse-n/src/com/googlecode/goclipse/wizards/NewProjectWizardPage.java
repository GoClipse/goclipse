package com.googlecode.goclipse.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import com.googlecode.goclipse.Activator;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (go).
 */

public class NewProjectWizardPage extends WizardPage {

	private ProjectComposite projectComposite;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewProjectWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Go Project");
		setDescription("This wizard creates a new Go project.");
		this.selection = selection;
	}

	
	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 1;
		projectComposite = new ProjectComposite(container, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectComposite.setLayoutData(gd);
		
		initialize();
		dialogChanged();
		setControl(container);
		
		
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
	   setImageDescriptor(Activator.getImageDescriptor("icons/go-icon-wizard.png"));
		if (selection != null && selection.isEmpty() == false	&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
//				containerText.setText(container.getFullPath().toString());
			}
		}
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
//				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */
	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
//		String fileName = getFileName();

//		if (getContainerName().length() == 0) {
//			updateStatus("File container must be specified");
//			return;
//		}
//		if (container == null
//				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
//			updateStatus("File container must exist");
//			return;
//		}
//		if (!container.isAccessible()) {
//			updateStatus("Project must be writable");
//			return;
//		}
//		if (fileName.length() == 0) {
//			updateStatus("File name must be specified");
//			return;
//		}
//		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
//			updateStatus("File name must be valid");
//			return;
//		}
//		int dotLoc = fileName.lastIndexOf('.');
//		if (dotLoc != -1) {
//			String ext = fileName.substring(dotLoc + 1);
//			if (ext.equalsIgnoreCase("go") == false) {
//				updateStatus("File extension must be \"go\"");
//				return;
//			}
//		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return "";
	}

   /**
    * @return the projectComposite
    */
   public ProjectComposite getProjectComposite() {
      return projectComposite;
   }

}