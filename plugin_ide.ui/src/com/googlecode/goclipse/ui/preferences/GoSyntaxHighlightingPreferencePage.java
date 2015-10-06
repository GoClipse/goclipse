package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.LangUIPlugin;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage_Old;

public class GoSyntaxHighlightingPreferencePage extends AbstractPreferencesBlockPrefPage_Old {
	
	public GoSyntaxHighlightingPreferencePage() {
		super(LangUIPlugin.getInstance().getPreferenceStore());
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected GoSourceColoringConfigurationBlock createPreferencesComponent() {
		return new GoSourceColoringConfigurationBlock();
	}
	
}