package com.googlecode.goclipse.gocode.preferences;

import com.googlecode.goclipse.gocode.GocodePlugin;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author steel
 */
public class GocodePreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {

  public GocodePreferencePage() {
    super(GRID);

    setPreferenceStore(GocodePlugin.getPlugin().getPreferenceStore());
  }

  @Override
  public void init(IWorkbench workbench) {

  }

  @Override
  protected void createFieldEditors() {
    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("Gocode");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    final BooleanFieldEditor runServerEditor = new BooleanFieldEditor(
        GocodePlugin.RUN_SERVER_PREF, "Start Gocode server automatically", fieldParent);
    addField(runServerEditor);
    
    Label label = new Label(fieldParent, SWT.NONE);
    label.setText("(to start manually: gocode -s" + (GocodePlugin.USE_TCP ? " -sock=tcp" : "") + ")");
    GridDataFactory.swtDefaults().span(3, 1).indent(18, 0).applyTo(label);
    
    FileFieldEditor fieldEditor = new FileFieldEditor(GocodePlugin.GOCODE_PATH_PREF, "Gocode path:", fieldParent);
    addField(fieldEditor);

    ((GridData) fieldEditor.getTextControl(fieldParent).getLayoutData()).widthHint = 150;
  }

}
