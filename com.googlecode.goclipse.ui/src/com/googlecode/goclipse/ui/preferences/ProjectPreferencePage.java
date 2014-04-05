/**
 * 
 */
package com.googlecode.goclipse.ui.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;

import com.googlecode.goclipse.preferences.PreferenceConstants;

/**
 * @author steel
 *
 */
public class ProjectPreferencePage extends FieldEditorPreferencePage {

	@Override
	protected void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.GOROOT,
				"GO&ROOT path:", getFieldEditorParent()));
	}

}
