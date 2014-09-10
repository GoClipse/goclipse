package LANG_PROJECT_ID.ide.ui.launch;

import org.eclipse.debug.ui.ILaunchConfigurationTab;

import melnorme.lang.ide.ui.launch.AbstractLangTabGroup;


public class LANGUAGE_TabGroup extends AbstractLangTabGroup {
	
	@Override
	protected ILaunchConfigurationTab createMainLaunchConfigTab() {
		return new LANGUAGE_MainLaunchConfigurationTab();
	}
	
}