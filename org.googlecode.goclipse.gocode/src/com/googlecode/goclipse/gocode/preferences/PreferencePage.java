
package com.googlecode.goclipse.gocode.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;

/**
 * @author steel
 *
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String GOCODE_PATH = "com.googlecode.goclipse.gocode.path";
	
	public PreferencePage() {
		super(GRID);

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("Gocode Path");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    FileFieldEditor fieldEditor = new FileFieldEditor(
				GOCODE_PATH, "Gocode path:", fieldParent);

		addField(fieldEditor);
	}

}
