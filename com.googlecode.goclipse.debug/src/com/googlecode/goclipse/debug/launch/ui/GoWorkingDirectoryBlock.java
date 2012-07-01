package com.googlecode.goclipse.debug.launch.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.WorkingDirectoryBlock;

import com.googlecode.goclipse.builder.GoConstants;
import com.googlecode.goclipse.debug.GoDebugPlugin;

public class GoWorkingDirectoryBlock extends WorkingDirectoryBlock {

  public GoWorkingDirectoryBlock() {
    super(GoConstants.GO_CONF_ATTRIBUTE_WORKING_DIRECTORY);
  }
  
  @Override
  protected IProject getProject(ILaunchConfiguration configuration) throws CoreException {
    String projectName = configuration.getAttribute(GoConstants.GO_CONF_ATTRIBUTE_PROJECT, (String)null);
    
    if (projectName == null || projectName.length() == 0) {
      return null;
    } else {
      IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
      
      return project;
    }
  }

  @Override
  protected void log(CoreException exception) {
    GoDebugPlugin.logError(exception);
    
    setErrorMessage(exception.getMessage());
  }

}
