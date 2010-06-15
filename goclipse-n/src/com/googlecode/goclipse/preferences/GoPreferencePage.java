package com.googlecode.goclipse.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;

import com.googlecode.goclipse.Activator;
import com.googlecode.goclipse.Environment;

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
		addField(new DirectoryFieldEditor(PreferenceConstants.GOROOT,
				"GO&ROOT path:", getFieldEditorParent()));
		
		addField(new ComboFieldEditor(PreferenceConstants.GOOS,
				"G&OOS:", new String[][]{{"", ""}, {"darwin", "darwin"}, {"linux", "linux"}, {"freebsd", "freebsd"}, {"nacl", "nacl"}, {"windows", "windows"}},getFieldEditorParent()));
		
		addField(new ComboFieldEditor(PreferenceConstants.GOARCH,
				"GO&ARCH:", new String[][]{{"", ""}, {"amd64", "amd64"}, {"386", "386"}, {"arm", "arm"}}, getFieldEditorParent()));
		
		addField(new FileFieldEditor(PreferenceConstants.COMPILER_PATH,
				"Go &Compiler path:", getFieldEditorParent()));
		
		addField(new FileFieldEditor(PreferenceConstants.LINKER_PATH,
				"Go &Linker path:", getFieldEditorParent()));
		
		addField(new FileFieldEditor(PreferenceConstants.FORMATTER_PATH,
				"&Code formatter path (gofmt):", getFieldEditorParent()));
		
		addField(new FileFieldEditor(PreferenceConstants.TESTER_PATH,
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
			System.out.println("Prefences Inited");
		}
	}

}