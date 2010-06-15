/**
 * 
 */
package com.googlecode.goclipse.debug;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;

import com.googlecode.goclipse.builder.GoLauncher;

/**
 * @author steel
 */
public class LaunchConfigurationDelegate implements ILaunchConfigurationDelegate2{

   @Override
   public boolean buildForLaunch(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
      String project = configuration.getAttribute("PROJECT_NAME", "");
      String mainfile = configuration.getAttribute("MAIN_FILE", "");
      if (project.isEmpty()||mainfile.isEmpty()) {
    	  return false;
      }
      return GoLauncher.createExecutable(project, mainfile);
   }

   @Override
   public boolean finalLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
         throws CoreException {
      
      return true;
   }

   @Override
   public ILaunch getLaunch(ILaunchConfiguration configuration, String mode) throws CoreException {
      Launch launch = new Launch(configuration, mode, null);
      return launch;
   }

   @Override
   public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor) throws CoreException {
      return true;
   }

   @Override
   public void launch(final ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
         throws CoreException {
	  String mainfile = configuration.getAttribute("MAIN_FILE", "");
      new Thread(new Runnable() {
    	  public void run() {
    		  GoLauncher.execute(configuration);	  
    	  }
      }, mainfile).start();
      
   }

}
