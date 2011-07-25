package com.googlecode.goclipse.debug.launch.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

/**
 * 
 * @author steel
 */
public class GoLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	/**
     * 
     */
	public GoLaunchConfigurationTabGroup() {

	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new GoLaunchConfigurationTab(), new SourceLookupTab(), new CommonTab() };

		setTabs(tabs);
	}

}
