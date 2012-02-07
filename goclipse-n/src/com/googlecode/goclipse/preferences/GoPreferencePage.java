package com.googlecode.goclipse.preferences;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Version;

import com.googlecode.goclipse.Activator;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By
 * subclassing <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace
 * that allows us to create a page that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that
 * belongs to the main plug-in class. That way, preferences can be accessed directly via the
 * preference store.
 */
public class GoPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
  public static final String ID = "com.googlecode.goclipse.preferences.GoPreferencePage";

  private DirectoryFieldEditor gorootEditor;
  private ComboFieldEditor goosEditor;
  private ComboFieldEditor goarchEditor;
  private FileFieldEditor compilerEditor;
  private FileFieldEditor linkerEditor;
  private FileFieldEditor packerEditor;
  private FileFieldEditor formatterEditor;
  private FileFieldEditor testerEditor;

  public GoPreferencePage() {
    super(GRID);

    setPreferenceStore(Activator.getDefault().getPreferenceStore());
    //setDescription("GoClipse v" + getVersionText());
  }

  /**
   * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to
   * manipulate various types of preferences. Each field editor knows how to save and restore
   * itself.
   */
  @Override
  public void createFieldEditors() {
    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("GOROOT and Platform Settings");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    addField(gorootEditor = new DirectoryFieldEditor(PreferenceConstants.GOROOT, "GO&ROOT path:",
        fieldParent));

    goosEditor = new ComboFieldEditor(PreferenceConstants.GOOS, "G&OOS:", new String[][] {
        {"", ""}, {PreferenceConstants.OS_DARWIN, PreferenceConstants.OS_DARWIN},
        {PreferenceConstants.OS_LINUX, PreferenceConstants.OS_LINUX},
        {PreferenceConstants.OS_FREEBSD, PreferenceConstants.OS_FREEBSD},
        {PreferenceConstants.OS_NACL, PreferenceConstants.OS_NACL},
        {PreferenceConstants.OS_WINDOWS, PreferenceConstants.OS_WINDOWS}}, fieldParent);
    addField(goosEditor);

    goarchEditor = new ComboFieldEditor(PreferenceConstants.GOARCH, "GO&ARCH:", new String[][] {
        {"", ""}, {PreferenceConstants.ARCH_AMD64, PreferenceConstants.ARCH_AMD64},
        {PreferenceConstants.ARCH_386, PreferenceConstants.ARCH_386},
        {PreferenceConstants.ARCH_ARM, PreferenceConstants.ARCH_ARM}}, fieldParent);
    addField(goarchEditor);

    ((GridLayout) fieldParent.getLayout()).numColumns = 3;

    group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("Paths");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

    addField(compilerEditor = new FileFieldEditor(PreferenceConstants.COMPILER_PATH,
        "Go &Compiler path:", fieldParent));

    addField(linkerEditor = new FileFieldEditor(PreferenceConstants.LINKER_PATH,
        "Go &Linker path:", fieldParent));

    addField(packerEditor = new FileFieldEditor(PreferenceConstants.PACKER_PATH,
        "Go &Packer path:", fieldParent));

    addField(formatterEditor = new FileFieldEditor(PreferenceConstants.FORMATTER_PATH,
        "&Code formatter path (gofmt):", fieldParent));

    addField(testerEditor = new FileFieldEditor(PreferenceConstants.TESTER_PATH,
        "&Testing tool path (gotest):", fieldParent));
  }

  @Override
  protected void adjustGridLayout() {
    super.adjustGridLayout();

    ((GridLayout) getFieldEditorParent().getLayout()).numColumns = 1;
  }

  @Override
  public void init(IWorkbench workbench) {

  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    super.propertyChange(event);

    if (event.getSource() == gorootEditor && PreferenceInitializer.getDefaultCompilerName() != null) {
      IPath gorootPath = new Path(gorootEditor.getStringValue());
      File gorootFile = gorootPath.toFile();
      if (gorootFile.exists() && gorootFile.isDirectory()) {
        IPath binPath = gorootPath.append("bin");
        File binFile = binPath.toFile();
        if (binFile.exists() && binFile.isDirectory()) {
          if ("".equals(compilerEditor.getStringValue())) {
            IPath compilerPath = binPath.append(PreferenceInitializer.getDefaultCompilerName());
            File compilerFile = compilerPath.toFile();
            if (compilerFile.exists() && !compilerFile.isDirectory()) {
              compilerEditor.setStringValue(compilerFile.getAbsolutePath());
            }
          }

          if ("".equals(linkerEditor.getStringValue())) {
            IPath linkerPath = binPath.append(PreferenceInitializer.getDefaultLinkerName());
            File linkerFile = linkerPath.toFile();
            if (linkerFile.exists() && !linkerFile.isDirectory()) {
              linkerEditor.setStringValue(linkerFile.getAbsolutePath());
            }
          }

          if ("".equals(packerEditor.getStringValue())) {
            IPath packerPath = binPath.append(PreferenceInitializer.getDefaultPackerName());
            File packerFile = packerPath.toFile();
            if (packerFile.exists() && !packerFile.isDirectory()) {
              packerEditor.setStringValue(packerFile.getAbsolutePath());
            }
          }

          if ("".equals(formatterEditor.getStringValue())) {
            String goFmtName = PreferenceInitializer.getDefaultGofmtName();
            IPath formatterPath = binPath.append(goFmtName);
            File formatterFile = formatterPath.toFile();
            if (formatterFile.exists() && !formatterFile.isDirectory()) {
              formatterEditor.setStringValue(formatterFile.getAbsolutePath());
            }
          }

          if ("".equals(testerEditor.getStringValue())) {
            String goTestName = PreferenceInitializer.getDefaultGotestName();
            IPath testerPath = binPath.append(goTestName);
            File testerFile = testerPath.toFile();
            if (testerFile.exists() && !testerFile.isDirectory()) {
              testerEditor.setStringValue(testerFile.getAbsolutePath());
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unused")
  private static String getVersionText() {
    Version version = Activator.getDefault().getBundle().getVersion();

    return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
  }

}
