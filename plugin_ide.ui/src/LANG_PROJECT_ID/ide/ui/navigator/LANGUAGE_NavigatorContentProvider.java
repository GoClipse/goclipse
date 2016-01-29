package LANG_PROJECT_ID.ide.ui.navigator;

import melnorme.lang.ide.ui.navigator.AbstractNavigatorContentProvider;

public class LANGUAGE_NavigatorContentProvider extends AbstractNavigatorContentProvider {
	
	@Override
	protected LangNavigatorSwitcher_HasChildren hasChildren_switcher() {
		return new LangNavigatorSwitcher_HasChildren() {
		};
	}
	
	@Override
	protected LangNavigatorSwitcher_GetChildren getChildren_switcher() {
		return new LangNavigatorSwitcher_GetChildren() {
		};
	}
	
	@Override
	protected LangNavigatorSwitcher_GetParent getParent_switcher() {
		return new LangNavigatorSwitcher_GetParent() {
		};
	}
	
}