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

import org.eclipse.swt.widgets.Composite;

import melnorme.lang.ide.ui.ContentAssistPreferences;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.util.swt.components.fields.CheckBoxField;

public class EditorContentAssistConfigurationBlock extends AbstractPreferencesBlock {
	
	public EditorContentAssistConfigurationBlock() {
		super();
	}
	
	@Override
	public int getPreferredLayoutColumns() {
		return 1;
	}
	
	@Override
	protected void createContents(Composite topControl) {
		createInsertionGroup(topControl);
		createAutoActivationGroup(topControl);
	}
	
	protected void createInsertionGroup(Composite parent) {
		Composite group = createSubsection(parent, 
			PreferencesMessages.LangPrefs_ContentAssist_Insertion_group, 2);
		
		createAndBindComponent(group, 
			ContentAssistPreferences.AUTO_INSERT__SingleProposals,
			new CheckBoxField(PreferencesMessages.LangPrefs_ContentAssist_Insertion_AutomaticSingleProposals_Label));
		createAndBindComponent(group, 
			ContentAssistPreferences.AUTO_INSERT__CommonPrefixes,
			new CheckBoxField(PreferencesMessages.LangPrefs_ContentAssist_Insertion_AutomaticCommonPrefixes_Label));
		
	}
	
	protected void createAutoActivationGroup(Composite parent) {
		Composite group = createSubsection(parent, 
			PreferencesMessages.LangPrefs_ContentAssist_AutoActivation_group, 2);
		
		createAndBindComponent(group, 
			ContentAssistPreferences.AUTO_ACTIVATE__DotTrigger,
			new CheckBoxField(PreferencesMessages.LangPrefs_ContentAssist_AutoActivation_DotTrigger_Label));
		if(createAutoActivation_DoubleColonOption()) {
			createAndBindComponent(group, 
				ContentAssistPreferences.AUTO_ACTIVATE__DoubleColonTrigger,
				new CheckBoxField(PreferencesMessages.LangPrefs_ContentAssist_AutoActivation_DoubleColonTrigger_Label));
		}
		createAndBindComponent(group, 
			ContentAssistPreferences.AUTO_ACTIVATE__AlphaNumericTrigger,
			new CheckBoxField(PreferencesMessages.LangPrefs_ContentAssist_AutoActivation_AlphanumericTrigger_Label));
		createIntField(group, 
			ContentAssistPreferences.AUTO_ACTIVATE__Delay,
			createNumberField(PreferencesMessages.LangPrefs_ContentAssist_AutoActivation_Delay_Label, 5));
	}
	
	protected boolean createAutoActivation_DoubleColonOption() {
		return false;
	}
	
}