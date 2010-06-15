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

import com.googlecode.goclipse.Activator;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (go).
 */
public class NewGoWizardPage extends WizardPage implements DialogChangeListener{

	private SourceFileComposite sourceFileComposite;

	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewGoWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Go Source File");
		setDescription("This wizard creates a new source file.");
		this.selection = selection;
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		sourceFileComposite = new SourceFileComposite(container, SWT.NULL);
		sourceFileComposite.addDialogChangedListener(this);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		sourceFileComposite.setLayoutData(gd);
		
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		layout.verticalSpacing = 9;
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		setImageDescriptor(Activator.getImageDescriptor("icons/sourceicon.png"));
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
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
				sourceFileComposite.getSourceFolderText().setText(container.getFullPath().toString());
			}
		}
		sourceFileComposite.getSourceFileText().setText("new_file.go");
	}

	/**
	 * Ensures that both text fields are set.
	 */
	public void dialogChanged() {
		
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		
		String fileName = getFileName();
		
		IResource file = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()+"/"+fileName));

		if(file!=null){
			updateStatus("File must not already exist");
			return;
		}
		
		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("go") == false) {
				updateStatus("File extension must be \"go\"");
				return;
			}
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return sourceFileComposite.getSourceFolderText().getText();
	}

	public String getFileName() {
		return sourceFileComposite.getSourceFileText().getText();
	}
}