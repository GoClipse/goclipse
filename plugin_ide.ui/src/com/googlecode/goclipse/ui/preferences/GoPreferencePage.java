package com.googlecode.goclipse.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.array;

import java.io.File;
import java.util.List;

import melnorme.lang.ide.ui.preferences.FieldEditorPreferencePageExt;
import melnorme.util.swt.SWTFactoryUtil;
import melnorme.util.swt.jface.preference.DirectoryFieldEditorExt;
import melnorme.utilbox.collections.ArrayList2;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.framework.Version;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.core.GoEnvironmentPrefs;
import com.googlecode.goclipse.core.GoEnvironmentUtils;
import com.googlecode.goclipse.tooling.env.GoArch;
import com.googlecode.goclipse.tooling.env.GoOs;
import com.googlecode.goclipse.ui.GoUIPlugin;

public class GoPreferencePage extends FieldEditorPreferencePageExt implements IWorkbenchPreferencePage {
	
	public static final String	 ID	= "com.googlecode.goclipse.ui.PreferencePage";
	
	protected DirectoryFieldEditor goRootEditor;
	protected GoPathFieldEditor goPathEditor;
	protected ComboFieldEditor goosEditor;
	protected ComboFieldEditor goarchEditor;
	protected FileFieldEditor goToolPathEditor;
	protected FileFieldEditor gofmtPathEditor;
	protected FileFieldEditor godocPathEditor;
	
	public GoPreferencePage() {
		super(GRID);
		
		setPreferenceStore(GoUIPlugin.getCorePrefStore());
		setDescription("GoClipse v" + getVersionText());
	}
	
	@Override
	public void createFieldEditors() {
		Group goSDK = createPreferenceGroup("Go SDK installation:");
		
		int numColumns = 3;
		
		addField(goRootEditor = new DirectoryFieldEditorExt(GoEnvironmentPrefs.GO_ROOT.key, 
			"GO&ROOT:", goSDK));
		addField(goosEditor = new ComboFieldEditor(GoEnvironmentPrefs.GO_OS.key, 
			"G&OOS:", getGoOSEntries(), goSDK));
		addField(goarchEditor = new ComboFieldEditor(GoEnvironmentPrefs.GO_ARCH.key, 
			"GO&ARCH:", getGoArchEntries(), goSDK));
		
		
		SWTFactoryUtil.createLabel(goSDK, 
			SWT.SEPARATOR | SWT.HORIZONTAL, "",
			fillGridDataFactory(1).indent(0, 5).span(numColumns, 1).create());
		
		addField(goToolPathEditor = new FileFieldEditor(GoEnvironmentPrefs.COMPILER_PATH.key, "go tool:", goSDK));
		addField(gofmtPathEditor = new FileFieldEditor(GoEnvironmentPrefs.FORMATTER_PATH.key, "gofmt:", goSDK));
		addField(godocPathEditor = new FileFieldEditor(GoEnvironmentPrefs.DOCUMENTOR_PATH.key, "godoc:", goSDK));
		
		applyDefaultGridLayout(goSDK, numColumns);
		
		/* -----------------  ----------------- */
		
		Group goPath = createPreferenceGroup("Default environment:");
		
		goPathEditor = new GoPathFieldEditor(GoEnvironmentPrefs.GO_PATH.key, "GO&PATH:", goPath);
		addField(goPathEditor);
		
		applyDefaultGridLayout(goPath, numColumns);
	}
	
	@Override
	protected void adjustGridLayoutForFieldsNumberofColumns(int numColumns) {
//		applyDefaultGridLayout(goSDK, numColumns);
//		applyDefaultGridLayout(goPath, numColumns);
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
	}
	
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		
		if (event.getSource() == goRootEditor) {
			handleGoRootChange();
		}
	}
	
	protected void handleGoRootChange() {
		IPath gorootPath = new Path(goRootEditor.getStringValue());
		File binPath = gorootPath.append("bin").toFile();
		
		if (goRootEditor.isValid()) {
			
			File compilerFile = findExistingFile(binPath.toPath(), 
				GoEnvironmentUtils.getSupportedCompilerNames());
			
			setValueIfFileExists(goToolPathEditor, compilerFile);
			
			String goFmtName = GoEnvironmentUtils.getDefaultGofmtName();
			setValueIfFileExists(gofmtPathEditor, gorootPath.append("bin").append(goFmtName).toFile());
			
			String goDocName = GoEnvironmentUtils.getDefaultGodocName();
			setValueIfFileExists(godocPathEditor, gorootPath.append("bin").append(goDocName).toFile());
			
			goosEditor.loadDefault();
			goarchEditor.loadDefault();
		}
	}
	
	protected void setValueIfFileExists(FileFieldEditor fileFieldEditor, File filePath) {
		if (filePath != null && filePath.exists() && filePath.isFile()) {
			fileFieldEditor.setStringValue(filePath.getAbsolutePath());
		} else {
			fileFieldEditor.setStringValue("");
		}
	}
	
	protected String[][] getGoOSEntries() {
		ArrayList2<String[]> goosEntries = new ArrayList2<>();
		goosEntries.add(array("<default>", ""));
		for (String goosValue : GoOs.GOOS_VALUES) {
			goosEntries.add(array(goosValue, goosValue));
		}
		String[][] goOSEntries_array = goosEntries.toArray(String[].class);
		return goOSEntries_array;
	}
	
	protected String[][] getGoArchEntries() {
		return new String[][] { 
				{ "<default>", "" },
				{ GoArch.ARCH_AMD64, GoArch.ARCH_AMD64 },
				{ GoArch.ARCH_386, GoArch.ARCH_386 },
				{ GoArch.ARCH_ARM, GoArch.ARCH_ARM } };
	}
	
	protected static File findExistingFile(java.nio.file.Path binPath, List<String> paths) {
		for (String strPath : paths) {
			java.nio.file.Path path = binPath.resolve(strPath);
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