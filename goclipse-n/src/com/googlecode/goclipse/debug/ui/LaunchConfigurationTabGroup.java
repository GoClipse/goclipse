package com.googlecode.goclipse.debug.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

/**
 * 
 * @author steel
 *
 */
@Deprecated
public class LaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

   /**
    * 
    */
   public LaunchConfigurationTabGroup() {
      // TODO Auto-generated constructor stub
   }

   @Override
   public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
      
      ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
            new MainLaunchConfigurationTab()
      };
      setTabs(tabs);

   }

}
