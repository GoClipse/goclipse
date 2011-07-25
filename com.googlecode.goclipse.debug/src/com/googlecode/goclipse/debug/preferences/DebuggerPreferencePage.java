package com.googlecode.goclipse.debug.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;

/**
 * The preference page for the Go debugger.
 * 
 * @author devoncarew
 */
public class DebuggerPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public DebuggerPreferencePage() {
		super(GRID);

		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		FileFieldEditor fieldEditor = new FileFieldEditor(
			"com.googlecode.goclipse.gdb", "GDB path:", getFieldEditorParent());

		addField(fieldEditor);
	}

}
