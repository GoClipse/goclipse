package com.googlecode.goclipse.debug.preferences;

import com.googlecode.goclipse.Activator;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

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
    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("GDB Path");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    FileFieldEditor fieldEditor = new FileFieldEditor("com.googlecode.goclipse.gdb", "GDB path:",
        fieldParent);

    addField(fieldEditor);
  }

}
