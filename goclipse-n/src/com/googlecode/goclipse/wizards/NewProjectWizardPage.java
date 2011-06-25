package com.googlecode.goclipse.wizards;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
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

public class NewProjectWizardPage extends WizardPage {

	private ProjectComposite projectComposite;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewProjectWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Go Project");
		setDescription("This wizard creates a new Go project.");
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
		
		setControl(container);
	}

	private void initialize() {
	   setImageDescriptor(Activator.getImageDescriptor("icons/go-icon-wizard.png"));
	}

   /**
    * @return the projectComposite
    */
   public ProjectComposite getProjectComposite() {
      return projectComposite;
   }

}