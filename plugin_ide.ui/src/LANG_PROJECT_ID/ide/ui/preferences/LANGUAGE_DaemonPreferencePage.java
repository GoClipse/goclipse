package LANG_PROJECT_ID.ide.ui.preferences;


import melnorme.lang.ide.ui.LangUIPlugin_Actual;
import melnorme.lang.ide.ui.preferences.common.AbstractPreferencesBlock;
import melnorme.lang.ide.ui.preferences.pages.DaemonToolPreferencePage;

public class LANGUAGE_DaemonPreferencePage extends DaemonToolPreferencePage {
	
	@Override
	protected AbstractPreferencesBlock init_createPreferencesBlock() {
		return new ServerToolsBlock() {
			@Override
			protected String getDaemonToolName() {
				return LangUIPlugin_Actual.DAEMON_TOOL_Name;
			}
		};
	}
	
}