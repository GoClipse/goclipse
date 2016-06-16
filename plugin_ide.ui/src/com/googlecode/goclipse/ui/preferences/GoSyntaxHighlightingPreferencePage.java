package com.googlecode.goclipse.ui.preferences;

import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlockPrefPage;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;

public class GoSyntaxHighlightingPreferencePage extends AbstractPreferencesBlockPrefPage {
	
	public GoSyntaxHighlightingPreferencePage() {
		super();
	}
	
	@Override
	protected String getHelpId() {
		return null;
	}
	
	@Override
	protected GoSourceColoringConfigurationBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new GoSourceColoringConfigurationBlock(prefContext);
	}
	
}