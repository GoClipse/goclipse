package com.googlecode.goclipse.preferences;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.Util;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;
import com.googlecode.goclipse.SysUtils;

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

public class GoPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
   
   
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
		setDescription("Goclipse Configuration for Go");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(gorootEditor = new DirectoryFieldEditor(PreferenceConstants.GOROOT,
				"GO&ROOT path:", getFieldEditorParent()));
		
		addField(goosEditor = new ComboFieldEditor(PreferenceConstants.GOOS,
				"G&OOS:", new String[][]{{"", ""}, {PreferenceConstants.OS_DARWIN, PreferenceConstants.OS_DARWIN}, {PreferenceConstants.OS_LINUX, PreferenceConstants.OS_LINUX}, {PreferenceConstants.OS_FREEBSD, PreferenceConstants.OS_FREEBSD}, {PreferenceConstants.OS_NACL, PreferenceConstants.OS_NACL}, {PreferenceConstants.OS_WINDOWS, PreferenceConstants.OS_WINDOWS}},getFieldEditorParent()));
		
		addField(goarchEditor = new ComboFieldEditor(PreferenceConstants.GOARCH,
				"GO&ARCH:", new String[][]{{"", ""}, {PreferenceConstants.ARCH_AMD64, PreferenceConstants.ARCH_AMD64}, {PreferenceConstants.ARCH_386, PreferenceConstants.ARCH_386}, {PreferenceConstants.ARCH_ARM, PreferenceConstants.ARCH_ARM}}, getFieldEditorParent()));
		
		addField(compilerEditor = new FileFieldEditor(PreferenceConstants.COMPILER_PATH,
				"Go &Compiler path:", getFieldEditorParent()));
		
		addField(linkerEditor = new FileFieldEditor(PreferenceConstants.LINKER_PATH,
				"Go &Linker path:", getFieldEditorParent()));

		addField(packerEditor = new FileFieldEditor(PreferenceConstants.PACKER_PATH,
				"Go &Packer path:", getFieldEditorParent()));
		
		addField(formatterEditor = new FileFieldEditor(PreferenceConstants.FORMATTER_PATH,
				"&Code formatter path (gofmt):", getFieldEditorParent()));
		
		addField(testerEditor = new FileFieldEditor(PreferenceConstants.TESTER_PATH,
				"&Testing tool path (gotest):", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		if(Environment.DEBUG){
			SysUtils.debug("Prefences Inited");
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// TODO Auto-generated method stub
		super.propertyChange(event);
		if (event.getSource() == gorootEditor && PreferenceInitializer.getDefaultCompilerName() != null) {
			IPath gorootPath = new Path(gorootEditor.getStringValue());
			File gorootFile = gorootPath.toFile();
			if (gorootFile.exists() && gorootFile.isDirectory()){
				IPath binPath = gorootPath.append("bin");
				File binFile = binPath.toFile();
				if (binFile.exists() && binFile.isDirectory()) {
					if ("".equals(compilerEditor.getStringValue())) {
						IPath compilerPath = binPath.append(PreferenceInitializer.getDefaultCompilerName());
						File compilerFile = compilerPath.toFile();
						if (compilerFile.exists() && ! compilerFile.isDirectory()){
							compilerEditor.setStringValue(compilerFile.getAbsolutePath());
						}
					}
					
					if ("".equals(linkerEditor.getStringValue())) {
						IPath linkerPath = binPath.append(PreferenceInitializer.getDefaultLinkerName());
						File linkerFile = linkerPath.toFile();
						if (linkerFile.exists() && ! linkerFile.isDirectory()){
							linkerEditor.setStringValue(linkerFile.getAbsolutePath());
						}
					}

					if ("".equals(packerEditor.getStringValue())) {
						IPath packerPath = binPath.append(PreferenceInitializer.getDefaultPackerName());
						File packerFile = packerPath.toFile();
						if (packerFile.exists() && ! packerFile.isDirectory()){
							packerEditor.setStringValue(packerFile.getAbsolutePath());
						}
					}

					if ("".equals(formatterEditor.getStringValue())) {
						String goFmtName = PreferenceInitializer.getDefaultGofmtName();
						IPath formatterPath = binPath.append(goFmtName);
						File formatterFile = formatterPath.toFile();
						if (formatterFile.exists() && ! formatterFile.isDirectory()){
							formatterEditor.setStringValue(formatterFile.getAbsolutePath());
						}
					}
					
					if ("".equals(testerEditor.getStringValue())) {
						String goTestName = PreferenceInitializer.getDefaultGotestName();
						IPath testerPath = binPath.append(goTestName);
						File testerFile = testerPath.toFile();
						if (testerFile.exists() && ! testerFile.isDirectory()){
							testerEditor.setStringValue(testerFile.getAbsolutePath());
						}
					}
				}
			}
		}
	}

}