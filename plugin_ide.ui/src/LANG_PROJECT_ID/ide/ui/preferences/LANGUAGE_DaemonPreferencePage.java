package LANG_PROJECT_ID.ide.ui.preferences;


import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.preferences.common.PreferencesPageContext;
import melnorme.lang.ide.ui.preferences.pages.EngineToolsPreferencePage;
import melnorme.lang.ide.ui.preferences.pages.LanguageToolsBlock;

public class LANGUAGE_DaemonPreferencePage extends EngineToolsPreferencePage {
	
	@Override
	protected LanguageToolsBlock init_createPreferencesBlock(PreferencesPageContext prefContext) {
		return new LanguageToolsBlock(prefContext, LangCore.get().languageServerHandler().getLanguageToolPathValidator());
	}
	
}