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
import melnorme.lang.ide.ui.CodeFormatterConstants;
import melnorme.lang.ide.ui.editor.text.LangAutoEditPreferenceConstants;
import melnorme.lang.ide.ui.preferences.fields.ComboBoxConfigField;
import melnorme.lang.ide.ui.preferences.fields.TextConfigField;
import melnorme.util.swt.components.IFieldValueListener;
import melnorme.utilbox.core.DevelopmentCodeMarkers;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;

public class LangEditorSmartTypingConfigurationBlock extends AbstractPreferencesConfigComponent {
	
	public LangEditorSmartTypingConfigurationBlock(PreferencePage prefPage) {
		super(prefPage);
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
		addCheckBox(group,
				PreferencesMessages.LangSmartTypingConfigurationBlock_closeStrings,
				LangAutoEditPreferenceConstants.AE_CLOSE_STRINGS, 0);
		
		addCheckBox(group,
				PreferencesMessages.LangSmartTypingConfigurationBlock_closeBrackets,
				LangAutoEditPreferenceConstants.AE_CLOSE_BRACKETS, 0);
		}
		
		addCheckBox(group,
				PreferencesMessages.LangSmartTypingConfigurationBlock_closeBraces,
				LangAutoEditPreferenceConstants.AE_CLOSE_BRACES, 0);
		
	}
	
	protected Composite createAutoEditGroup(Composite parent) {
		Composite group = createSubsection(parent, 
			PreferencesMessages.EditorPreferencePage_AutoEdits);
		
		group.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		
		addCheckBox(group,
				PreferencesMessages.EditorPreferencePage_smartIndent,
				LangAutoEditPreferenceConstants.AE_SMART_INDENT, 2);
		
		addCheckBox(group,
				PreferencesMessages.EditorPreferencePage_smartDeIndent,
				LangAutoEditPreferenceConstants.AE_SMART_DEINDENT, 2);
		
		addCheckBox(group,
				PreferencesMessages.EditorPreferencePage_considerParenthesesAsBlocks,
				LangAutoEditPreferenceConstants.AE_PARENTHESES_AS_BLOCKS, 2);
		
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
				boolean enabled = !areEqual(indentModeField.getFieldValue(), CodeFormatterConstants.TAB);
				indentationSizeField.setEnabled(enabled);
			}
		};
		indentModeField.addValueChangedListener(indentModeValueListener);
		indentModeValueListener.fieldValueChanged();
	}
	
}