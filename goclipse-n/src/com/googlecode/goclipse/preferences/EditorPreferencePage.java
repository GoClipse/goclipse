package com.googlecode.goclipse.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.googlecode.goclipse.Activator;

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

public class EditorPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public EditorPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Configuration for the Go Editor");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		
		addField(new RadioGroupFieldEditor(PreferenceConstants.FIELD_USE_HIGHLIGHTING,
				"Highlighting Options:", 1,
				new String[][] { {  "&Use highlighting.", PreferenceConstants.VALUE_HIGHLIGHTING_TRUE },
						{ "&Do not use highlighting.", PreferenceConstants.VALUE_HIGHLIGHTING_FALSE } }, 
						getFieldEditorParent()));
		
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR,          "Keyword",          getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR,            "Value",            getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR,        "Primitive",        getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR,          "Comment",          getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, "Builtin Function", getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_STRING_COLOR,           "String",           getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR, "Multiline String", getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
//		if(Environment.DEBUG){
//			System.out.println("Prefences Inited");
//		}
	}

}