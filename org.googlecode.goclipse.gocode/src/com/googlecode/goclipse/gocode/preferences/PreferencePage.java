/**
 * 
 */
package com.googlecode.goclipse.gocode.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
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
		FileFieldEditor fieldEditor = new FileFieldEditor(
				GOCODE_PATH, "Gocode path:", getFieldEditorParent());

		addField(fieldEditor);
	}

}
