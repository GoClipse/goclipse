package com.googlecode.goclipse.ui.wizards;

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.ui.GoPluginImages;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (go).
 */
public class GoProjectWizardPage extends WizardPage {

	private ProjectComposite projectComposite;

	/**
	 * Create a new NewProjectWizardPage.
	 */
	public GoProjectWizardPage(ISelection selection) {
		super("wizardPage");

		setTitle("Create a Go Project");
		setDescription("This wizard creates a new Go project.");
		setImageDescriptor(GoPluginImages.WIZARD_ICON.getDescriptor());
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 1;
		container.setLayout(layout);

		projectComposite = new ProjectComposite(container, SWT.NONE);
		projectComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Link link = new Link(container, SWT.NONE);
		link.setText("<a>Configure Go settings...</a>");
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openGoPreferencePage();
			}
		});

		validate();

		setControl(container);
	}

	protected void openGoPreferencePage() {
		PreferenceDialog pref = PreferencesUtil.createPreferenceDialogOn(getShell(),
			"com.googlecode.goclipse.preferences.GoPreferencePage", null, null);
		
		if (pref != null) {
			pref.open();
			
			validate();
		}
	}

	protected ProjectComposite getProjectComposite() {
		return projectComposite;
	}

	private void validate() {
		if (GoEnvironmentPrefs.isValid()) {
			setErrorMessage(null);
		} else {
			setErrorMessage("GOROOT has not been set. This can be done from the Go preference page.");
		}
	}

}
