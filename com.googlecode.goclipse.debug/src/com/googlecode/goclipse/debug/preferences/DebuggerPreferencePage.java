package com.googlecode.goclipse.debug.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.preferences.PreferenceConstants;

// TODO: show the version of gdb? see if it'll work w/ Go?
// gdb --version prints the intro text + version and quits

/**
 * The preference page for the Go debugger.
 * 
 * @author devoncarew
 */
public class DebuggerPreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {
  private FileFieldEditor gdbFileFieldEditor;

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

    gdbFileFieldEditor = new FileFieldEditor(PreferenceConstants.GDB_PATH, "GDB path:", fieldParent);
    //gdbFileFieldEditor.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
    
    addField(gdbFileFieldEditor);
    
    ((GridData)gdbFileFieldEditor.getTextControl(fieldParent).getLayoutData()).widthHint = 150;
  }

}
