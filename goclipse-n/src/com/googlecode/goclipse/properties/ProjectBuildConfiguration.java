package com.googlecode.goclipse.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

import com.googlecode.goclipse.Environment;

/**
 * 
 */
public class ProjectBuildConfiguration extends PropertyPage {

	private ProjectBuildConfigurationComposite composite;

	/**
	 * Constructor for SamplePropertyPage.
	 */
	public ProjectBuildConfiguration() {
		super();
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	@Override
  protected Control createContents(Composite parent) {
		composite = new ProjectBuildConfigurationComposite(parent, this,
				SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);
		return composite;
	}

	/**
	 * 
	 */
	@Override
  protected void performDefaults() {
		composite.setSourceFolders(new String[] {});
	}

	/**
	 * 
	 */
	@Override
  public boolean performOk() {
		String[] sourcefolders = composite.getSourceFolders();
		if (sourcefolders != null) {
			Environment.INSTANCE.setSourceFolders(sourcefolders);
		}

		String pkgOutputfolder = composite.getPkgOutputFolder();
		if (pkgOutputfolder != null) {
			Environment.INSTANCE.setPkgOutputFolder(Path.fromOSString(pkgOutputfolder));
		}

		String binOutputfolder = composite.getBinOutputFolder();
		if (binOutputfolder != null) {
			Environment.INSTANCE.setBinOutputFolder(Path.fromOSString(binOutputfolder));
		}
		
		return true;
	}
	
	protected IProject getProject() {
	  return (IProject)getElement();
	}

}