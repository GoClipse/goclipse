/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation (JDT)
 *     DLTK team ? - DLTK modifications 
 *     Bruno Medeiros - Lang modifications
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.array;
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.preferences.EditorAppearanceColorsComponent.EditorColorItem;
import melnorme.lang.ide.ui.preferences.fields.ComboBoxConfigField;
import melnorme.lang.ide.ui.preferences.fields.TextConfigField;
import melnorme.util.swt.components.IFieldValueListener;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

//originally from DLTK version 5.0.0
public class EditorConfigurationBlock extends AbstractPreferencesConfigComponent {
	
	public EditorConfigurationBlock(PreferencePage mainPreferencePage, IPreferenceStore store) {
		super(store, mainPreferencePage);
	}
	
	@Override
	public Composite createControl(Composite parent) {
		
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayout(new GridLayout());
		
		createIndentationGroup(control);
		createAppearanceGroup(control);
		
		return control;
	}
	
	protected void createIndentationGroup(Composite composite) {
		Composite generalGroup = createSubsection(composite, FormatterMessages.IndentationGroup_header);
		
		generalGroup.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		final String[] INDENT_MODE__LABELS = new String[] {
				FormatterMessages.IndentationGroup_tab_policy_TAB,
				FormatterMessages.IndentationGroup_tab_policy_SPACE 
		};
		final String[] INDENT_MODE__VALUES = array(
			CodeFormatterConstants.TAB, 
			CodeFormatterConstants.SPACES 
		);
		
		final ComboBoxConfigField indentModeField = 
		addConfigComponent(generalGroup, 0, new ComboBoxConfigField(
			FormatterMessages.IndentationGroup_tab_policy, 
			CodeFormatterConstants.FORMATTER_INDENT_MODE, 
			INDENT_MODE__LABELS, 
			INDENT_MODE__VALUES
		));
		
		addConfigComponent(generalGroup, 1, createNumberField(
			FormatterMessages.IndentationGroup_tab_size, 
			CodeFormatterConstants.FORMATTER_TAB_SIZE, 
			2
		));
		
		final TextConfigField indentationSizeField = 
		addConfigComponent(generalGroup, 1, createNumberField(
			FormatterMessages.IndentationGroup_indent_size, 
			CodeFormatterConstants.FORMATTER_INDENTATION_SPACES_SIZE, 
			2
		));
		IFieldValueListener indentModeValueListener = new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				boolean enabled = !indentModeField.getFieldValue().equals(CodeFormatterConstants.TAB); 
				indentationSizeField.setEnabled(enabled);
			}
		};
		indentModeField.addValueChangedListener(indentModeValueListener);
		indentModeValueListener.fieldValueChanged();
	}
	
	protected void createAppearanceGroup(Composite parent) {
		createEditorAppearanceColorsComponent(parent);
	}
	
	protected void createEditorAppearanceColorsComponent(Composite parent) {
		EditorColorItem[] editorColorItems = createEditorAppearanceColorEntries();
		if(editorColorItems.length == 0) {
			return;
		}
		EditorAppearanceColorsComponent appearanceItemsCp = new EditorAppearanceColorsComponent(editorColorItems);
		appearanceItemsCp.createComponent(parent);
		addConfigComponent(appearanceItemsCp);
	}
	
	protected EditorColorItem[] createEditorAppearanceColorEntries() {
		return new EditorColorItem[] { };
	}
	
}