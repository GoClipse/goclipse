package LANG_PROJECT_ID.ide.ui.preferences;


import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock2;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.preferences.pages.DaemonToolPreferencePage;

public class LANGUAGE_DaemonPreferencePage extends DaemonToolPreferencePage {
	
	@Override
	protected AbstractPreferencesBlock2 init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new ServerToolsBlock(prefContext) {
			@Override
			protected String getDaemonToolName() {
				return LangUIPlugin_Actual.DAEMON_TOOL_Name;
			}
		};
	}
	
}