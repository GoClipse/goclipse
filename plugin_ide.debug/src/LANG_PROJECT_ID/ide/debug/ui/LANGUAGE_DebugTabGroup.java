package LANG_PROJECT_ID.ide.debug.ui;

import melnorme.lang.ide.debug.ui.AbstractLangDebugTabGroup;

import org.eclipse.debug.ui.ILaunchConfigurationTab;

import LANG_PROJECT_ID.ide.ui.launch.LANGUAGE_MainLaunchConfigurationTab;


public class LANGUAGE_DebugTabGroup extends AbstractLangDebugTabGroup {
	
	@Override
	protected ILaunchConfigurationTab createMainLaunchConfigTab() {
		return new LANGUAGE_MainLaunchConfigurationTab();
	}
	
}