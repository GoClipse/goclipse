package com.googlecode.goclipse.preferences;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
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
	@Override
  public void createFieldEditors() {
    Group group = new Group(getFieldEditorParent(), SWT.NONE);
    group.setText("Highlighting");
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(group);
    GridLayoutFactory.fillDefaults().margins(10, 4).applyTo(group);

    Composite fieldParent = new Composite(group, SWT.NONE);
    GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(fieldParent);

		addField(new BooleanFieldEditor(PreferenceConstants.FIELD_USE_HIGHLIGHTING,
				"Use highlighting", fieldParent));

		Label splitter = new Label(fieldParent, SWT.SEPARATOR | SWT.HORIZONTAL);
    GridDataFactory.swtDefaults().grab(true, false).span(3, 1).align(SWT.FILL, SWT.TOP).applyTo(splitter);

		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_KEYWORD_COLOR,          "Keyword:",           fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_VALUE_COLOR,            "Value:",             fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_PRIMITIVE_COLOR,        "Primitive:",         fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_COMMENT_COLOR,          "Comment:",           fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_BUILTIN_FUNCTION_COLOR, "Built-in function:", fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_STRING_COLOR,           "String:",            fieldParent));
		addField(new ColorFieldEditor(PreferenceConstants.FIELD_SYNTAX_MULTILINE_STRING_COLOR, "Multi-line string:", fieldParent));
	}

	@Override
  public void init(IWorkbench workbench) {

	}

}
