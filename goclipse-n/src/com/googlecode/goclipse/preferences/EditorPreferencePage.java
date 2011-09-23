package com.googlecode.goclipse.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;

public class EditorPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public EditorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.FIELD_USE_HIGHLIGHTING,
				"Use Highlighting", getFieldEditorParent()));
		
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR,          "Keyword",          getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR,            "Value",            getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR,        "Primitive",        getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR,          "Comment",          getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, "Builtin Function", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_STRING_COLOR,           "String",           getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR, "Multiline String", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {

	}

}