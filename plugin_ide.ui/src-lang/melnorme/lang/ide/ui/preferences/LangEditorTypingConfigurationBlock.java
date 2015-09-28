/*******************************************************************************
 * Copyright (c) 2011, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package melnorme.lang.ide.ui.preferences;

import static melnorme.utilbox.core.CoreUtil.areEqual;
import static melnorme.utilbox.core.CoreUtil.array;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.core.text.format.FormatterIndentMode;
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.LangAutoEditPreferenceConstants;
import melnorme.lang.ide.ui.preferences.common.AbstractComponentsPrefPage;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.util.swt.components.fields.CheckBoxField;
import melnorme.util.swt.components.fields.ComboBoxField;
import melnorme.util.swt.components.fields.NumberField;
import melnorme.utilbox.core.DevelopmentCodeMarkers;
import melnorme.utilbox.fields.IFieldValueListener;

public class LangEditorTypingConfigurationBlock extends AbstractPreferencesBlock {
	
	public LangEditorTypingConfigurationBlock(AbstractComponentsPrefPage prefPage) {
		super(prefPage);
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		createAutoClosingGroup(topControl);
		createAutoEditGroup(topControl);
		createIndentationGroup(topControl);
	}
	
	protected void createAutoClosingGroup(Composite parent) {
		Composite group = createSubsection(parent, 
			PreferencesMessages.LangSmartTypingConfigurationBlock_autoclose_title);
		
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		if(DevelopmentCodeMarkers.UNIMPLEMENTED_FUNCTIONALITY) {
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_CLOSE_STRINGS.key, 
			new CheckBoxField(PreferencesMessages.LangSmartTypingConfigurationBlock_closeStrings));
		
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_CLOSE_BRACKETS.key,
			new CheckBoxField(PreferencesMessages.LangSmartTypingConfigurationBlock_closeBrackets));
		}
		
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_CLOSE_BRACES.key,
			new CheckBoxField(PreferencesMessages.LangSmartTypingConfigurationBlock_closeBraces));
		
	}
	
	protected Composite createAutoEditGroup(Composite parent) {
		Composite group = createSubsection(parent, 
			PreferencesMessages.EditorPreferencePage_AutoEdits);
		
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_SMART_INDENT.key, 
			new CheckBoxField(PreferencesMessages.EditorPreferencePage_smartIndent));
		
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_SMART_DEINDENT.key, 
			new CheckBoxField(PreferencesMessages.EditorPreferencePage_smartDeIndent));
		
		createBooleanField(group, 
			LangAutoEditPreferenceConstants.AE_PARENTHESES_AS_BLOCKS.key,
			new CheckBoxField(PreferencesMessages.EditorPreferencePage_considerParenthesesAsBlocks));
		
		return group;
	}
	
	protected void createIndentationGroup(Composite composite) {
		Composite generalGroup = createSubsection(composite, FormatterMessages.IndentationGroup_header);
		
		generalGroup.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		final String[] INDENT_MODE__LABELS = new String[] {
				FormatterMessages.IndentationGroup_tab_policy_TAB,
				FormatterMessages.IndentationGroup_tab_policy_SPACE 
		};
		final String[] INDENT_MODE__VALUES = array(
			FormatterIndentMode.TAB.toString(), 
			FormatterIndentMode.SPACES.toString() 
		);
		
		final ComboBoxField indentModeField = new ComboBoxField(
			FormatterMessages.IndentationGroup_tab_policy, 
			INDENT_MODE__LABELS, 
			INDENT_MODE__VALUES);
		createCheckboxField(generalGroup, 
			CodeFormatterConstants.FORMATTER_INDENT_MODE.key,
			indentModeField
		);
		
		createStringField(generalGroup, 
			CodeFormatterConstants.FORMATTER_TAB_SIZE.key,
			createNumberField(FormatterMessages.IndentationGroup_tab_size, 2)
		);
		
		final NumberField indentationSizeField = createNumberField(
			FormatterMessages.IndentationGroup_indent_size, 2);
		createStringField(generalGroup,
			CodeFormatterConstants.FORMATTER_INDENTATION_SPACES_SIZE.key, 
			indentationSizeField
		);
		IFieldValueListener indentModeValueListener = new IFieldValueListener() {
			@Override
			public void fieldValueChanged() {
				boolean enabled = areEqual(indentModeField.getFieldStringValue(), 
					FormatterIndentMode.SPACES.toString());
				indentationSizeField.setEnabled(enabled);
			}
		};
		indentModeField.addValueChangedListener(indentModeValueListener);
		indentModeValueListener.fieldValueChanged();
	}
	
}