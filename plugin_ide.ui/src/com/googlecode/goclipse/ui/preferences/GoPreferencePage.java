package com.googlecode.goclipse.ui.preferences;

import java.io.File;
import java.util.List;

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
import com.googlecode.goclipse.preferences.PreferenceConstants;
import com.googlecode.goclipse.preferences.PreferenceInitializer;
import com.googlecode.goclipse.ui.GoUIPlugin;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */
public class GoPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	
	public static final String	 ID	= "com.googlecode.goclipse.preferences.GoPreferencePage";

	private DirectoryFieldEditor goRootEditor;
	private GoPathFieldEditor goPathEditor;
	private ComboFieldEditor	 goosEditor;
	private ComboFieldEditor	 goarchEditor;
	private FileFieldEditor	     compilerEditor;
	private FileFieldEditor	     formatterEditor;
	private FileFieldEditor	     documentorEditor;

	/**
	 * 
	 */
	public GoPreferencePage() {
		super(GRID);

		setPreferenceStore(GoUIPlugin.getCorePrefStore());
		setDescription("GoClipse v" + getVersionText());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		Group group = new Group(getFieldEditorParent(), SWT.NONE);
		group.setText("GOROOT and Platform Settings");
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
		GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

		Composite fieldParent = new Composite(group, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).hint(150, -1).applyTo(fieldParent);

		goRootEditor = new DirectoryFieldEditor(PreferenceConstants.GO_ROOT.key, "GO&ROOT:", fieldParent);
		addField(goRootEditor);

    goPathEditor = new GoPathFieldEditor(PreferenceConstants.GO_PATH.key, "GO&PATH:", fieldParent);
    addField(goPathEditor);
    
		goosEditor = new ComboFieldEditor(PreferenceConstants.GO_OS.key, "G&OOS:", new String[][] { { "", "" },
		        { PreferenceConstants.OS_DARWIN, PreferenceConstants.OS_DARWIN },
		        { PreferenceConstants.OS_LINUX, PreferenceConstants.OS_LINUX },
		        { PreferenceConstants.OS_FREEBSD, PreferenceConstants.OS_FREEBSD },
		        { PreferenceConstants.OS_NACL, PreferenceConstants.OS_NACL },
		        { PreferenceConstants.OS_WINDOWS, PreferenceConstants.OS_WINDOWS } }, fieldParent);
		addField(goosEditor);

		goarchEditor = new ComboFieldEditor(PreferenceConstants.GO_ARCH.key, "GO&ARCH:", new String[][] { { "", "" },
		        { PreferenceConstants.ARCH_AMD64, PreferenceConstants.ARCH_AMD64 },
		        { PreferenceConstants.ARCH_386, PreferenceConstants.ARCH_386 },
		        { PreferenceConstants.ARCH_ARM, PreferenceConstants.ARCH_ARM } }, fieldParent);
		addField(goarchEditor);

		((GridLayout) fieldParent.getLayout()).numColumns = 3;

		group = new Group(getFieldEditorParent(), SWT.NONE);
		group.setText("Go Tools");
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
		GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

		fieldParent = new Composite(group, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

		addField(compilerEditor = new FileFieldEditor(PreferenceConstants.GO_TOOL_PATH, "Go &tool path (go):",
		        fieldParent));

		addField(formatterEditor = new FileFieldEditor(PreferenceConstants.FORMATTER_PATH,
		        "Go &formatter (gofmt):", fieldParent));

		addField(documentorEditor = new FileFieldEditor(PreferenceConstants.DOCUMENTOR_PATH,
		        "Go &documentor (godoc):", fieldParent));
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

		if (event.getSource() == goRootEditor && PreferenceInitializer.getDefaultCompilerName() != null) {

			IPath gorootPath = new Path(goRootEditor.getStringValue());
			File gorootFile  = gorootPath.toFile();

			if (gorootFile.exists() && gorootFile.isDirectory()) {
				IPath binPath = gorootPath.append("bin");
				File binFile = binPath.toFile();

				if (binFile.exists() && binFile.isDirectory()) {
					if ("".equals(compilerEditor.getStringValue())) {
						File compilerFile = findExistingFile(binPath, PreferenceInitializer.getSupportedCompilerNames());
						if (compilerFile != null && !compilerFile.isDirectory()) {
							compilerEditor.setStringValue(compilerFile.getAbsolutePath());
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

					if ("".equals(documentorEditor.getStringValue())) {
						String goDocName = PreferenceInitializer.getDefaultGodocName();
						IPath testerPath = binPath.append(goDocName);
						File testerFile = testerPath.toFile();
						if (testerFile.exists() && !testerFile.isDirectory()) {
							documentorEditor.setStringValue(testerFile.getAbsolutePath());
						}
					}
				}
			}
		}
	}

	private File findExistingFile(IPath binPath, List<String> paths) {
		for (String strPath : paths) {
			IPath path = binPath.append(strPath);
			File file = path.toFile();
			if (file.exists()) {
				return file;
			}
		}

		return null;
	}

	private static String getVersionText() {
		Version version = Activator.getDefault().getBundle().getVersion();

		return version.getMajor() + "." + version.getMinor() + "." + version.getMicro();
	}

}
