package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;

public class GoSyntaxHighlightingPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public GoSyntaxHighlightingPreferencePage() {
		super(LangUIPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected GoSourceColoringConfigurationBlock createPreferencesBlock() {
		return new GoSourceColoringConfigurationBlock();
	}
	
}