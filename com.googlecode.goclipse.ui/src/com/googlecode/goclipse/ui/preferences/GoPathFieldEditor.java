package com.googlecode.goclipse.ui.preferences;

import java.io.File;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.util.Util;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * A field editor for multiple directory paths preference. A standard directory
 * dialog appears when the user presses the change button.
 */
public class GoPathFieldEditor extends DirectoryFieldEditor {
	private String delim;

	/**
	 * Creates a GoPath field editor.
	 * 
	 * @param name
	 *            the name of the preference this field editor works on
	 * @param labelText
	 *            the label text of the field editor
	 * @param parent
	 *            the parent of the field editor's control
	 */
	public GoPathFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);

		this.setChangeButtonText("Add Folder");

		delim = ":";
		if (Util.isWindows()) {
			delim = ";";
		}
	}

	@Override
	protected String changePressed() {
		File f = new File(getTextControl().getText());
		if (!f.exists()) {
			f = null;
		}
		File d = getDirectory(f);
		if (d == null) {
			return null;
		}

		String text = getTextControl().getText();
		if(text != null && !text.equals("")) {
			return text + delim + d.getAbsolutePath();
		}

		return d.getAbsolutePath();
	}

	/**
	 * Checks, whether the GoPath is correct
	 **/
	@Override
	protected boolean doCheckState() {
		String[] folders = getTextControl().getText().split(delim);

		// If the text is empty and empty path is allowed, return true
		if (folders.length == 1 && folders[0].equals("")
				&& isEmptyStringAllowed()) {
			return true;
		}

		// Check, if every single folder exists
		for (int i = 0; i < folders.length; i++) {
			File tmp = new File(folders[i].trim());
			if (!tmp.isDirectory()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Displays a DirectoryDialog and returns the folder, if the user has chosen
	 * any, otherwise returns null
	 * 
	 * @param startingDirectory
	 *            The directory, which the DirectoryDialog is at when opening
	 *            the dialog
	 * @return Returns the directory, which the user has selected
	 */
	private File getDirectory(File startingDirectory) {
		DirectoryDialog fileDialog = new DirectoryDialog(getShell(), SWT.SINGLE);

		// If the user has set a starting directory, use it
		if (startingDirectory != null) {
			fileDialog.setFilterPath(startingDirectory.getPath());
		}
		String dir = fileDialog.open();
		if (dir != null) {
			dir = dir.trim();
			if (dir.length() > 0) {
				return new File(dir);
			}
		}

		return null;
	}
}
