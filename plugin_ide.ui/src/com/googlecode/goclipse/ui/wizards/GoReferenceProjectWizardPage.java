package com.googlecode.goclipse.ui.wizards;

import java.io.File;

import melnorme.utilbox.core.CommonException;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.tooling.env.GoRoot;
import com.googlecode.goclipse.ui.GoPluginImages;

/**
 * The wizard page for the Go Reference project wizard.
 * 
 * @see GoReferenceProjectWizard
 */
public class GoReferenceProjectWizardPage extends WizardPage {

	/**
	 * Create a new GoReferenceProjectWizardPage.
	 */
	public GoReferenceProjectWizardPage() {
		super("GoReferenceProjectWizardPage");

		setTitle("Create a Go Reference Project");
		setDescription("Create a new project containing a link to GOROOT/src; this is useful for browsing the source for the Go libraries");
		setImageDescriptor(GoPluginImages.WIZARD_ICON.getDescriptor());
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayoutFactory.swtDefaults().applyTo(composite);

		Label label = new Label(composite, SWT.WRAP);
		label.setText("Clicking finish will create a GOROOT reference project in your workspace. "
			+ "This is useful for browsing the source for the Go libraries. "
			+ "It is only ever necessary to create one of these reference projects.");
		GridDataFactory.fillDefaults().grab(true, false).hint(100, 100).applyTo(label);

		setControl(composite);
	}

	@Override
  public void setVisible(boolean visible) {
		super.setVisible(visible);

		updateStatus();
	}

	/**
	 * Updates the status line and the OK button according to the given status
	 * 
	 * @param status
	 *            status to apply
	 */
	protected void updateStatus() {
		// validate
		boolean valid = true;

		GoRoot goRoot = getEnvironment().getGoRoot();
		if (goRoot.isEmpty()) {
			// TODO: provide a hyperlink to the preference page

			setErrorMessage("GOROOT has not been set. This can be done from the Go preference page.");

			valid = false;
		} else {
			try {
				if (!getGoRootSrcFolder().exists()) {
					// TODO: provide a hyperlink to the preference page

					setErrorMessage("GOROOT/src folder does not exist. Please check the preference setting for the GOROOT path.");

					valid = false;
				}
			} catch (CommonException e) {
				setErrorMessage(e.getMessage());
				valid = false;
			}
		}

		setPageComplete(valid);
	}
	
	protected GoEnvironment getEnvironment() {
		return GoProjectEnvironment.getGoEnvironment(null); //XXX: review if this is correct
	}
	
	protected File getGoRootSrcFolder() throws CommonException {
		return getEnvironment().getGoRoot().getSourceRootLocation().toFile();
	}
	
}
