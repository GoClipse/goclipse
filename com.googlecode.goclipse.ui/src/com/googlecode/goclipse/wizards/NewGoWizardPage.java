package com.googlecode.goclipse.wizards;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
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
import com.googlecode.goclipse.ui.navigator.GoPackage;
import com.googlecode.goclipse.ui.navigator.GoSourceFolder;
import com.googlecode.goclipse.wizards.NewSourceFileComposite.SourceFileType;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (go).
 */
public class NewGoWizardPage extends WizardPage implements DialogChangeListener{

	private NewSourceFileComposite sourceFileComposite;

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
	@Override
    public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		sourceFileComposite = new NewSourceFileComposite(container, SWT.NULL);
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
		String path = null;
		IPath prjPath = null;
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
				path = container.getFullPath().toString();
				prjPath = ((IResource) obj).getProject().getLocation();
			} else if (obj instanceof GoSourceFolder) {
				path = ((GoSourceFolder) obj).getFolder().getFullPath().toString();
				prjPath = ((GoSourceFolder) obj).getProject().getLocation();
			} else if (obj instanceof GoPackage) {
				path = ((GoPackage) obj).getFolder().getFullPath().toString();
				prjPath = ((GoPackage) obj).getProject().getLocation();
			}
			if (path != null) {
				sourceFileComposite.getSourceFolderName().setText(path);
			}
		}
		
		final String newFilePrefix = "new_file";
		String fName = newFilePrefix + ".go";
		if (path != null && prjPath != null) {
			sourceFileComposite.getSourceFolderName().setText(path);
			String fullPath = prjPath.removeLastSegments(1).append(path).toOSString();
			File f = new File(fullPath);
			
			String[] newFiles = f.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File arg0, String name) {
					if (name.startsWith(newFilePrefix) && name.endsWith(".go")) {
						return true;
					}
					return false;
				}
			});
			
			if (newFiles != null && newFiles.length > 0) {
				int i = newFiles.length + 1;
				while (true) {
					fName = newFilePrefix + "_" + i + ".go";
					File nf = new File(fullPath + File.separator + fName);
					if (nf.exists()) {
						i ++;
						continue;
					}
					break;
				}
			}
		}
		
		int pos = fName.indexOf(".go");
		sourceFileComposite.getSourceFilename().setText(fName);
		sourceFileComposite.getSourceFilename().setSelection(0, pos);
		sourceFileComposite.getSourceFilename().forceFocus();
	}

	/**
	 * Ensures that both text fields are set.
	 */
	@Override
    public void dialogChanged() {
		
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		
		String fileName = getFileName();
		
		IResource file = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getContainerName()+"/"+fileName));

		if(file!=null){
			updateStatus("File " + fileName + " already exists. Choose a new name.");
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
		
		if(sourceFileComposite.getSourceFileType()==SourceFileType.TEST){
			if(!sourceFileComposite.getSourceFilename().getText().endsWith("_test.go")){
				updateStatus("Tests must end with \"_test.go\" suffix");
				return;
			}
		}
		
		updateStatus(null);
	}

	/**
	 * @param message
	 */
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	/**
	 * @return name of the container
	 */
	public String getContainerName() {
		return sourceFileComposite.getSourceFolderName().getText();
	}

	/**
	 * @return name of the file
	 */
	public String getFileName() {
		return sourceFileComposite.getSourceFilename().getText();
	}
	
	/**
	 * @return {@link SourceFileType}
	 */
	public SourceFileType getSourFileType() {
		return sourceFileComposite.getSourceFileType();
	}
}