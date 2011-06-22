package com.googlecode.goclipse.wizards;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * The wizard page for the Go Reference project wizard.
 * 
 * @see GoReferenceProjectWizard
 */
public class GoReferenceProjectWizardPage extends WizardPage {

	public GoReferenceProjectWizardPage() {
		super("GoReferenceProjectWizardPage");

		setTitle("New Go Reference Project");
		setDescription("Create a new project containing a link to GOROOT/src; this is useful for browsing the source for the Go libraries");
		setImageDescriptor(Activator.getImageDescriptor("icons/go-icon-wizard.png"));
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

		if (!isGoRootSet()) {
			// TODO: provide a hyperlink to the preference page

			setErrorMessage("GOROOT has not been set. This can be done from the Go preference page.");

			valid = false;
		} else if (!getGoRootSrcFolder().exists()) {
			// TODO: provide a hyperlink to the preference page

			setErrorMessage("GOROOT/src folder does not exist. Please check the preference setting for the GOROOT path.");

			valid = false;
		}

		setPageComplete(valid);
	}

	protected File getGoRootSrcFolder() {
		String goRoot = Activator.getDefault().getPreferenceStore()
			.getString(PreferenceConstants.GOROOT);

		File srcFolder = Path.fromOSString(goRoot).append("src").toFile();

		return srcFolder;
	}

	private boolean isGoRootSet() {
		String goRoot = Activator.getDefault().getPreferenceStore()
			.getString(PreferenceConstants.GOROOT);

		return !"".equals(goRoot);
	}

}
