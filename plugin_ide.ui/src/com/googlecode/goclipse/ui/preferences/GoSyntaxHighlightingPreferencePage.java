package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.AbstractPreferencesComponentPrefPage;
import melnorme.lang.ide.ui.preferences.IPreferencesBlock;

public class GoSyntaxHighlightingPreferencePage extends AbstractPreferencesComponentPrefPage {
	
	public GoSyntaxHighlightingPreferencePage() {
		super(LangUIPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected void setDescription() {
		setDescription(null);
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected IPreferencesBlock createPreferencesComponent() {
		return new GoSourceColoringConfigurationBlock(getPreferenceStore());
	}
	
}