package com.googlecode.goclipse.debug.preferences;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.debug.utils.GDBUtils;
import com.googlecode.goclipse.preferences.PreferenceConstants;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The preference page for the Go debugger.
 * 
 * @author devoncarew
 */
public class DebuggerPreferencePage extends FieldEditorPreferencePage implements
    IWorkbenchPreferencePage {
  private FileFieldEditor gdbFileFieldEditor;
  private Label infoLabel;
  private Group macInfoGroup;
  
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
    GridLayoutFactory.fillDefaults().margins(10, 4).extendedMargins(0, 0, 0, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    gdbFileFieldEditor = new FileFieldEditor(PreferenceConstants.GDB_PATH, "GDB path:", fieldParent) {
      @Override
      protected boolean doCheckState() {
        Double version = GDBUtils.getGDBVersion(gdbFileFieldEditor.getStringValue());

        if (version == null) {
          infoLabel.setText("Unable to get gdb version.");
          clearMacInfo();
        } else if (version >= GDBUtils.MIN_VERSION) {
          infoLabel.setText("GDB version " + version);
          clearMacInfo();
        } else {
          infoLabel.setText("GDB version " + version + "; Go debugging requires at least version "
              + GDBUtils.MIN_VERSION);
          displayMacInfo();
        }

        return super.doCheckState();
      }
    };

    addField(gdbFileFieldEditor);

    infoLabel = new Label(group, SWT.NONE);
    infoLabel.setText("");
    
    // mac info group
    macInfoGroup = new Group(group.getParent(), SWT.NONE);
    macInfoGroup.setText("Mac GDB Info");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(macInfoGroup);
    GridLayoutFactory.fillDefaults().margins(10, 10).extendedMargins(0, 0, 0, 4).applyTo(macInfoGroup);
    
    StyledText text = new StyledText(macInfoGroup, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER);
    text.setText(
          "In order to install the latest gdb, first install brew from:\n\n"
        + "\thttp://mxcl.github.com/homebrew/\n\n"
        + "Then run the following command:\n\n"
        + "\tbrew install https://raw.github.com/Homebrew/homebrew-dupes/master/gdb.rb\n\n"
        + "The latest gdb will be available at /usr/local/bin/gdb.");
    
    macInfoGroup.setVisible(false);
  }

  @Override
  protected void adjustGridLayout() {
    
  }
  
  protected void clearMacInfo() {
    macInfoGroup.setVisible(false);
  }

  protected void displayMacInfo() {
    if (Activator.isMac()) {
      macInfoGroup.setVisible(true);
    }
  }

}
